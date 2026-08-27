package com.lmalecic.lovefinderzz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.lmalecic.lovefinderzz.api.RickAndMortyWorker
import com.lmalecic.lovefinderzz.framework.DATA_IMPORTED
import com.lmalecic.lovefinderzz.framework.getBooleanPreference
import com.lmalecic.lovefinderzz.framework.isOnline
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val IMPORT_WORK_NAME = "rick_and_morty_inital_import"

sealed interface StartupState {
    data object Checking : StartupState
    data object Importing : StartupState
    data object NoInternet : StartupState
    data object Ready : StartupState
    data class Failed(val message: String) : StartupState
}

class StartupViewModel(application: Application) : AndroidViewModel(application) {

    private val app = getApplication<Application>()
    private val workManager = WorkManager.getInstance(app)
    private val _state = MutableStateFlow<StartupState>(StartupState.Checking)
    private var workObservationJob: Job? = null

    val state: StateFlow<StartupState> = _state.asStateFlow()

    init {
        checkStartupState()
    }

    private fun checkStartupState(existingWorkPolicy: ExistingWorkPolicy = ExistingWorkPolicy.KEEP) {
        _state.value = StartupState.Checking

        when {
            app.getBooleanPreference(DATA_IMPORTED) -> {
                _state.value = StartupState.Ready
            }

            !app.isOnline() -> {
                _state.value = StartupState.NoInternet
            }

            else -> {
                enqueueImportWorker(existingWorkPolicy)
            }
        }
    }

    private fun enqueueImportWorker(existingWorkPolicy: ExistingWorkPolicy) {
        _state.value = StartupState.Importing

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<RickAndMortyWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork(IMPORT_WORK_NAME, existingWorkPolicy, request)
        observeImportWork()
    }

    private fun observeImportWork() {
        workObservationJob?.cancel()

        workObservationJob = viewModelScope.launch {
            workManager.getWorkInfosForUniqueWorkFlow(IMPORT_WORK_NAME)
                .collectLatest { workInfos ->
                    val workInfo = workInfos.firstOrNull {
                        !it.state.isFinished
                    } ?: workInfos.lastOrNull()

                    _state.value = when(workInfo?.state) {
                        WorkInfo.State.ENQUEUED, WorkInfo.State.BLOCKED, WorkInfo.State.RUNNING -> StartupState.Importing
                        WorkInfo.State.SUCCEEDED -> StartupState.Ready
                        WorkInfo.State.FAILED -> StartupState.Failed("Data import failed")
                        WorkInfo.State.CANCELLED -> StartupState.Failed("Data import was cancelled")
                        null -> StartupState.Importing
                    }
                }
        }
    }

    fun retry() {
        checkStartupState(existingWorkPolicy = ExistingWorkPolicy.REPLACE)
    }
}
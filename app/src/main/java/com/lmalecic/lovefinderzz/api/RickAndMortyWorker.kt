package com.lmalecic.lovefinderzz.api

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lmalecic.lovefinderzz.database.RickAndMortyDatabase
import com.lmalecic.lovefinderzz.framework.DATA_IMPORTED
import com.lmalecic.lovefinderzz.framework.setBooleanPreference
import retrofit2.HttpException
import java.io.IOException

class RickAndMortyWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = RickAndMortyDatabase.getInstance(applicationContext)

        val fetcher = RickAndMortyFetcher(
            api = RickAndMortyClient.api,
            database = database
        )

        return try {
            fetcher.fetchAll()

            applicationContext.setBooleanPreference(
                key = DATA_IMPORTED,
                value = true
            )

            Result.success()
        } catch (exception: IOException) {
            Result.retry()
        } catch (exception: HttpException) {
            if (exception.code() == 429 || exception.code() >= 500) {
                Result.retry()
            } else {
                Result.failure()
            }
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}

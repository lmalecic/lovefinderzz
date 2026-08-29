package com.lmalecic.lovefinderzz.framework

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import com.lmalecic.lovefinderzz.formatter.US_LONG_DATE
import kotlinx.coroutines.delay
import java.time.LocalDate
import kotlin.time.Duration

fun <T : Enum<T>> Enum<T>.toTitleCase(): String =
    this.name.lowercase().replaceFirstChar { it.uppercase() }

fun String.parseUsLongDate(): LocalDate = LocalDate.parse(trim(), US_LONG_DATE)

fun Context.setBooleanPreference(key: String, value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit { putBoolean(key, value) }
}

fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

fun callDelayed(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(work, delay)
}

@Composable
fun DelayedContent(
    delay: Duration,
    waitingContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var finished by remember { mutableStateOf(false) }

    LaunchedEffect(delay) {
        delay(delay)
        finished = true
    }

    if (finished) {
        content()
    } else {
        waitingContent?.invoke()
    }
}
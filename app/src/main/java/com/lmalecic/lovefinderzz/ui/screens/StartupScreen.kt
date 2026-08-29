package com.lmalecic.lovefinderzz.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lmalecic.lovefinderzz.LovefinderzzApp
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.framework.DelayedContent
import com.lmalecic.lovefinderzz.framework.callDelayed
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.Typography
import com.lmalecic.lovefinderzz.viewmodel.StartupState
import com.lmalecic.lovefinderzz.viewmodel.StartupViewModel
import kotlinx.coroutines.Runnable
import kotlin.time.Duration.Companion.seconds

@Composable
fun StartupScreen(viewModel: StartupViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        StartupState.Checking, StartupState.Importing -> ImportScreen()
        StartupState.NoInternet -> NoInternetContent(onRetry = viewModel::retry)
        StartupState.Ready -> /* DelayedContent(
            delay = 1.5.seconds,
            waitingContent = { ImportScreen() }
        ) {
            LovefinderzzApp()
        } */
            LovefinderzzApp()

        is StartupState.Failed -> ImportErrorScreen(
            message = currentState.message,
            onRetry = viewModel::retry
        )
    }
}

@Composable
fun ImportScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier.width(128.dp)
                .height(128.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 8.dp,
            )

            Image(
                painter = painterResource(R.drawable.lovefinderzz),
                contentDescription = "Lovefinderzz App Icon",
                modifier = Modifier.fillMaxSize()
                    .scale(0.75f)
            )
        }

        Text(
            text = "Please wait...",
            style = Typography.titleLarge
        )
    }
}

@Composable
fun ImportErrorScreen(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = "Error Image"
        )

        Text(
            text = message,
            style = Typography.titleLarge
        )

        Button(
            onClick = onRetry
        ) {
            Text(
                text = stringResource(R.string.retry)
            )
        }
    }
}

@Composable
fun NoInternetContent(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_no_internet),
            contentDescription = "No Internet Image",
        )

        Text(
            text = stringResource(R.string.no_internet_connection),
            style = Typography.titleLarge
        )

        Button(
            onClick = onRetry
        ) {
            Text(
                text = stringResource(R.string.retry)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImportScreenPreview() {
    LovefinderzzTheme {
        ImportScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ImportErrorScreenPreview() {
    LovefinderzzTheme {
        ImportErrorScreen("Error Preview", {})
    }
}

@Preview(showBackground = true)
@Composable
fun NoInternetContentPreview() {
    LovefinderzzTheme {
        NoInternetContent({})
    }
}
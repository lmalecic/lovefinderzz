package com.lmalecic.lovefinderzz.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.Typography
import kotlinx.serialization.Serializable

@Serializable
data object FavoritesRoute: AppRoute

@Composable
fun FavoritesContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.favorites_title),
            style = Typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesContentPreview() {
    LovefinderzzTheme {
        FavoritesContent()
    }
}
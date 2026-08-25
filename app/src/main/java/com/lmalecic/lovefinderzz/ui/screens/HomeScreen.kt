package com.lmalecic.lovefinderzz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.handler.getGifEnabledLoader
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import androidx.compose.ui.text.TextStyle
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.Typography
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute: AppRoute

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
            .padding(32.dp, 24.dp)
    ) {
        Text(
            text = stringResource(R.string.home_title),
            style = Typography.titleLarge
        )

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.home)
                .build(),
            contentDescription = "Animated GIF",
            imageLoader = getGifEnabledLoader(context),
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            text = stringResource(R.string.home_body),
            style = TextStyle(fontStyle = FontStyle.Italic)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    LovefinderzzTheme {
        HomeContent()
    }
}
package com.lmalecic.lovefinderzz.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.handler.getGifEnabledLoader
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em

@Composable
fun HomeView(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
            .padding(32.dp, 24.dp)
    ) {
        Text(
            text = "Welcome to Lovefinderzz!",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 6.em)
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
            text = "Yes, we developed an app.",
            style = TextStyle(fontStyle = FontStyle.Italic)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    LovefinderzzTheme {
        HomeView()
    }
}
package com.lmalecic.lovefinderzz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterStatus
import com.lmalecic.lovefinderzz.entity.Gender
import com.lmalecic.lovefinderzz.entity.ReminderDetails
import com.lmalecic.lovefinderzz.entity.ReminderEntity
import com.lmalecic.lovefinderzz.entity.ReminderMode
import com.lmalecic.lovefinderzz.ui.components.ReminderCard
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.pageContentPadding
import com.lmalecic.lovefinderzz.ui.theme.Typography
import com.lmalecic.lovefinderzz.viewmodel.HomeViewModel
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data object HomeRoute: AppRoute

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val upcomingReminderDetails by viewModel.observeUpcomingReminderDetails()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    HomeContent(
        upcomingReminderDetails = upcomingReminderDetails
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    upcomingReminderDetails: List<ReminderDetails>
) {
    val context = LocalContext.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
            .pageContentPadding()
    ) {
        item {
            Text(
                text = stringResource(R.string.home_title),
                style = Typography.titleLarge
            )
        }

        item {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.home)
                    .build(),
                contentDescription = "Animated GIF",
                imageLoader = getGifEnabledLoader(context),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(400f / 225f)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        item {
            Text(
                text = stringResource(R.string.home_body),
                style = TextStyle(fontStyle = FontStyle.Italic),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = stringResource(R.string.upcomingRemindersTitle),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (upcomingReminderDetails.isNotEmpty()) {
            items(
                items = upcomingReminderDetails,
                key = { reminder -> reminder.reminder.id }
            ) { reminder ->
                ReminderCard(reminder)
            }
        } else {
            item {
                Text(
                    text = "All clear, view a character to schedule a reminder",
                    style = MaterialTheme.typography.bodyMedium.merge(
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    LovefinderzzTheme {
        HomeContent(
            upcomingReminderDetails = listOf(
                ReminderDetails(
                    reminder = ReminderEntity(
                        id = 1,
                        characterId = 1,
                        triggerAtEpochMillis = System.currentTimeMillis(),
                        mode = ReminderMode.NOTIFICATION,
                        enabled = true,
                        message = null
                    ),

                    character = CharacterEntity(
                        id = 1,
                        name = "Character Name",
                        status = CharacterStatus.UNKNOWN,
                        species = "Android",
                        gender = Gender.GENDERLESS,
                        imageUrl = ""
                    )
                ),

                ReminderDetails(
                    reminder = ReminderEntity(
                        id = 2,
                        characterId = 2,
                        triggerAtEpochMillis = System.currentTimeMillis(),
                        mode = ReminderMode.NOTIFICATION,
                        enabled = true,
                        message = "WAKE UP"
                    ),

                    character = CharacterEntity(
                        id = 2,
                        name = "Character Name",
                        status = CharacterStatus.UNKNOWN,
                        species = "Android",
                        gender = Gender.GENDERLESS,
                        imageUrl = ""
                    )
                )
            )
        )
    }
}
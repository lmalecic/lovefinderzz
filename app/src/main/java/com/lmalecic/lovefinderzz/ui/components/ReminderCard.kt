package com.lmalecic.lovefinderzz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterStatus
import com.lmalecic.lovefinderzz.entity.Gender
import com.lmalecic.lovefinderzz.entity.ReminderDetails
import com.lmalecic.lovefinderzz.entity.ReminderEntity
import com.lmalecic.lovefinderzz.entity.ReminderMode
import com.lmalecic.lovefinderzz.formatter.toLocalizedString
import com.lmalecic.lovefinderzz.framework.toZonedDateTime
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.extendedColors
import java.time.format.FormatStyle

@Composable
fun ReminderCard(reminderDetails: ReminderDetails, onClick: () -> Unit = {}) {
    val triggerAt = reminderDetails.reminder.triggerAtEpochMillis.toZonedDateTime()

    val content: @Composable ColumnScope.() -> Unit = {
        Row {
            AsyncImage(
                model = reminderDetails.character.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(50))
            )

            Column(
                modifier = Modifier.weight(1f)
                    .padding(2.dp, 6.dp)
            ) {
                Text(
                    text = reminderDetails.character.name,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )

                if (reminderDetails.reminder.message?.isNotBlank() ?: false) {
                    Text(
                        text = reminderDetails.reminder.message,
                        style = MaterialTheme.typography.labelMedium.merge(
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(6.dp, 6.dp, 16.dp, 6.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Banner(
                    text = triggerAt.toLocalDate().toLocalizedString(FormatStyle.SHORT),
                    iconPainter = painterResource(R.drawable.ic_calendar),
                    containerColor = MaterialTheme.extendedColors.bannerOpaque
                )

                Text(
                    text = triggerAt.toLocalTime().toLocalizedString(FormatStyle.SHORT),
                    style = MaterialTheme.typography.bodyLarge.merge(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }

    val cardModifier = Modifier.fillMaxWidth()
        .height(50.dp)
        .clip(RoundedCornerShape(50))

    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )

    Card(
        modifier = cardModifier,
        colors = cardColors,
        content = content,
        onClick = onClick,
    )
}

@Preview(showBackground = true)
@Composable
fun ReminderCardPreview() {
    LovefinderzzTheme {
        ReminderCard(
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
            )
        )
    }
}
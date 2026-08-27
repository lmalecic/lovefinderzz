package com.lmalecic.lovefinderzz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import com.lmalecic.lovefinderzz.formatter.toLocalizedString
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.extendedColors
import java.time.LocalDate
import java.time.format.FormatStyle

@Composable
fun EpisodeCard(
    episode: EpisodeEntity,
    onClick: (() -> Unit)? = null,
) {
    val content: @Composable ColumnScope.() -> Unit = {
        Row {
            Column(
                modifier = Modifier.padding(6.dp, 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    Banner(
                        text = episode.episode,
                        containerColor = MaterialTheme.extendedColors.bannerOpaque
                    )

                    Banner(
                        text = episode.airDate.toLocalizedString(FormatStyle.MEDIUM),
                        iconPainter = painterResource(R.drawable.ic_calendar),
                        containerColor = MaterialTheme.extendedColors.bannerOpaque
                    )
                }

                Text(
                    text = episode.name,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }

    val cardModifier = Modifier.fillMaxWidth()

    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )

    if (onClick == null) {
        Card(
            modifier = cardModifier,
            colors = cardColors,
            content = content
        )
    } else {
        Card(
            modifier = cardModifier,
            colors = cardColors,
            content = content,
            onClick = onClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeCardPreview() {
    LovefinderzzTheme {
        EpisodeCard(EpisodeEntity(
            id = 1,
            name = "Episode Name",
            airDate = LocalDate.now(),
            episode = "S01E01"
        ))
    }
}

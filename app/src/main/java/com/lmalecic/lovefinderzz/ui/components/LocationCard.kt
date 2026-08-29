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
import com.lmalecic.lovefinderzz.entity.LocationEntity
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.extendedColors

@Composable
fun LocationCard(
    location: LocationEntity,
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
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    Banner(
                        text = location.dimension.replaceFirstChar { it.uppercase() },
                        iconPainter = painterResource(R.drawable.ic_dimension),
                        containerColor = MaterialTheme.extendedColors.bannerOpaque
                    )

                    Banner(
                        text = location.type,
                        containerColor = MaterialTheme.extendedColors.bannerOpaque
                    )
                }

                Text(
                    text = location.name,
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
fun LocationCardPreview() {
    LovefinderzzTheme {
        LocationCard(LocationEntity(
            id = 1,
            name = "Location Name",
            type = "Virtual Machine",
            dimension = "QEMU"
        ))
    }
}

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterStatus
import com.lmalecic.lovefinderzz.entity.Gender
import com.lmalecic.lovefinderzz.framework.toTitleCase
import com.lmalecic.lovefinderzz.ui.icon_filled
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.extendedColors
import com.lmalecic.lovefinderzz.ui.theme.getColor

@Composable
fun CharacterCard(
    character: CharacterEntity,
    onClick: (() -> Unit)? = null,
) {
    val content: @Composable ColumnScope.() -> Unit = {
        Row {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxHeight()
                    .aspectRatio(1f)
            )

            Column(
                modifier = Modifier.padding(10.dp, 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.labelLarge
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    Banner(
                        text = character.status.toTitleCase(),
                        containerColor = MaterialTheme.extendedColors.bannerOpaque
                    ) {
                        prepend {
                            Surface(
                                modifier = Modifier
                                    .height(12.dp)
                                    .padding(3.5.dp, 3.5.dp, 1.dp, 3.5.dp)
                                    .aspectRatio(1f),
                                shape = CircleShape,
                                color = character.status.getColor()
                            ) {}
                        }
                    }

                    Banner(
                        text = character.species.replaceFirstChar { it.uppercase() },
                        containerColor = MaterialTheme.extendedColors.bannerOpaque
                    )

                    if (character.type?.isNotBlank() ?: false) {
                        Banner(
                            text = character.type.replaceFirstChar { it.uppercase() },
                            containerColor = MaterialTheme.extendedColors.bannerOpaque
                        )
                    }

                    Banner(
                        text = character.gender.toTitleCase(),
                        iconPainter = painterResource(character.gender.icon_filled),
                        containerColor = MaterialTheme.extendedColors.bannerOpaque
                    )
                }
            }
        }
    }

    val cardModifier = Modifier.fillMaxWidth()
        .height(80.dp)

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
fun CharacterCardPreview1() {
    LovefinderzzTheme {
        CharacterCard(CharacterEntity(
            id = 1,
            name = "Character Name",
            status = CharacterStatus.UNKNOWN,
            species = "Species",
            gender = Gender.GENDERLESS,
            imageUrl = ""
        ))
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterCardPreview2() {
    LovefinderzzTheme {
        CharacterCard(CharacterEntity(
            id = 1,
            name = "Character Name",
            status = CharacterStatus.ALIVE,
            species = "Species",
            gender = Gender.MALE,
            imageUrl = ""
        ))
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterCardPreview3() {
    LovefinderzzTheme {
        CharacterCard(CharacterEntity(
            id = 1,
            name = "Character Name",
            status = CharacterStatus.DEAD,
            species = "Species",
            type = "Type?",
            gender = Gender.FEMALE,
            imageUrl = ""
        ))
    }
}
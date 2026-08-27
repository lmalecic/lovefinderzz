package com.lmalecic.lovefinderzz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.CharacterDetails
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterStatus
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import com.lmalecic.lovefinderzz.entity.Gender
import com.lmalecic.lovefinderzz.framework.toTitleCase
import com.lmalecic.lovefinderzz.ui.components.Banner
import com.lmalecic.lovefinderzz.ui.components.BannerSize
import com.lmalecic.lovefinderzz.ui.components.Detail
import com.lmalecic.lovefinderzz.ui.components.EpisodeCard
import com.lmalecic.lovefinderzz.ui.icon_filled
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.extendedColors
import com.lmalecic.lovefinderzz.ui.theme.getColor
import com.lmalecic.lovefinderzz.viewmodel.CharactersViewModel
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CharacterPagerRoute(
    val characterId: Long
) : AppRoute

@Composable
fun CharacterPagerScreen(
    initialCharacterId: Long,
    navController: NavHostController,
    viewModel: CharactersViewModel = viewModel()
) {
    val characters by viewModel.characters.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    if (characters.isEmpty()) {
        CharacterDetailsEmptyContent()
        return
    }

    val initialPage = remember(characters, initialCharacterId) {
        characters.indexOfFirst { it.id == initialCharacterId }
            .coerceAtLeast(0)
    }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { characters.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val character = characters[page]

        val detailsFlow = remember(character.id) {
            viewModel.observeDetails(character.id)
        }

        val details by detailsFlow.collectAsStateWithLifecycle(
            initialValue = null
        )

        if (details == null) {
            CharacterDetailsEmptyContent()
        } else {
            CharacterDetailsContent(details = details!!, navController = navController)
        }
    }
}

@Composable
fun CharacterDetailsEmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CharacterDetailsContent(details: CharacterDetails, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "image") {
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                AsyncImage(
                    model = details.character.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(1f)
                        .fillMaxWidth(),
                )
            }
        }

        item(key = "title") {
            Text(
                text = "Character — ${details.character.name}",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item(key = "details") {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Detail(
                    labelText = stringResource(R.string.statusDetail)
                ) {
                    Banner(
                        text = details.character.status.toTitleCase(),
                        containerColor = MaterialTheme.extendedColors.bannerOpaque,
                        bannerSize = BannerSize.LARGE
                    ) { scale ->
                        prepend {
                            Surface(
                                modifier = Modifier
                                    .height(12.dp * scale)
                                    .padding(3.5.dp * scale, 3.5.dp * scale, scale.dp, 3.5.dp * scale)
                                    .aspectRatio(1f),
                                shape = CircleShape,
                                color = details.character.status.getColor()
                            ) {}
                        }
                    }
                }

                Detail(
                    labelText = stringResource(R.string.speciesDetail),
                    valueText = details.character.species.replaceFirstChar { it.uppercase() }
                )

                if (details.character.type?.isNotBlank() ?: false) {
                    Detail(
                        labelText = stringResource(R.string.typeDetail),
                        valueText = details.character.type.replaceFirstChar { it.uppercase() }
                    )
                }

                Detail(
                    labelText = stringResource(R.string.genderDetail),
                    valueText = details.character.gender.toTitleCase(),
                    valueIconPainter = painterResource(details.character.gender.icon_filled)
                )

                Detail(
                    labelText = stringResource(R.string.originDetail),
                    valueText = details.origin?.name ?: stringResource(R.string.unknown),
                    valueIconPainter = painterResource(R.drawable.ic_location)
                )

                Detail(
                    labelText = stringResource(R.string.locationDetail),
                    valueText = details.location?.name ?: stringResource(R.string.unknown),
                    valueIconPainter = painterResource(R.drawable.ic_location)
                )
            }
        }

        item(key = "episodes-title") {
            Text(
                text = "Appears in",
                style = MaterialTheme.typography.bodyLarge.merge(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (details.episodes.isNotEmpty()) {
            items(
                items = details.episodes,
                key = { episode -> episode.id }
            ) { episode ->
                EpisodeCard(
                    episode = episode,
                    onClick = {
                        navController.navigate(EpisodePagerRoute(episode.id))
                    }
                )
            }
        } else {
            item(key = "no-episodes") {
                Text(
                    text = "This character doesn't appear in any episode",
                    style = MaterialTheme.typography.bodySmall.merge(
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailsEmptyContentPreview() {
    LovefinderzzTheme {
        CharacterDetailsEmptyContent()
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailsContentPreview() {
    LovefinderzzTheme {
        CharacterDetailsContent(
            details = CharacterDetails(
                character = CharacterEntity(
                    id = 1,
                    name = "Character Name",
                    status = CharacterStatus.ALIVE,
                    species = "Android",
                    gender = Gender.GENDERLESS,
                    imageUrl = ""
                ),

                origin = null,
                location = null,

                episodes = listOf(
                    EpisodeEntity(
                        id = 1,
                        name = "Episode Name",
                        airDate = LocalDate.now(),
                        episode = "S01E01"
                    ),

                    EpisodeEntity(
                        id = 2,
                        name = "Episode Name",
                        airDate = LocalDate.now(),
                        episode = "S01E02"
                    ),

                    EpisodeEntity(
                        id = 3,
                        name = "Episode Name",
                        airDate = LocalDate.now(),
                        episode = "S01E03"
                    )
                )
            ),
            navController = rememberNavController()
        )
    }
}
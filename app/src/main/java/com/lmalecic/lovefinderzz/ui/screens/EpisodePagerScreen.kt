package com.lmalecic.lovefinderzz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterStatus
import com.lmalecic.lovefinderzz.entity.EpisodeDetails
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import com.lmalecic.lovefinderzz.entity.Gender
import com.lmalecic.lovefinderzz.formatter.toLocalizedString
import com.lmalecic.lovefinderzz.ui.components.CharacterCard
import com.lmalecic.lovefinderzz.ui.components.Detail
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.viewmodel.EpisodesViewModel
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.FormatStyle

@Serializable
data class EpisodePagerRoute(
    val episodeId: Long
) : AppRoute

@Composable
fun EpisodePagerScreen(
    initialEpisodeId: Long,
    navController: NavHostController,
    viewModel: EpisodesViewModel = viewModel()
) {
    val episodes by viewModel.episodes.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    if (episodes.isEmpty()) {
        EpisodeDetailsEmptyContent()
        return
    }

    val initialPage = remember(episodes, initialEpisodeId) {
        episodes.indexOfFirst { it.id == initialEpisodeId }
            .coerceAtLeast(0)
    }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { episodes.size }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            val currentEpisode = episodes.getOrNull(pagerState.currentPage)

            if (currentEpisode != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.setFavorite(currentEpisode.id, !currentEpisode.favorite)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            if (currentEpisode.favorite) R.drawable.ic_favorites
                            else R.drawable.ic_outline_favorites
                        ),
                        contentDescription = null
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color.Transparent,
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            pageSpacing = 32.dp
        ) { page ->
            val episode = episodes[page]

            val detailsFlow = remember(episode.id) {
                viewModel.observeDetails(episode.id)
            }

            val details by detailsFlow.collectAsStateWithLifecycle(
                initialValue = null
            )

            if (details == null) {
                EpisodeDetailsEmptyContent()
            } else {
                EpisodeDetailsContent(details = details!!, navController = navController)
            }
        }
    }
}

@Composable
fun EpisodeDetailsEmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EpisodeDetailsContent(details: EpisodeDetails, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "title") {
            Text(
                text = "Episode — ${details.episode.name}",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item(key = "details") {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Detail(
                    labelText = "Episode",
                    valueText = details.episode.episode
                )

                Detail(
                    labelText = "Air Date",
                    valueText = details.episode.airDate.toLocalizedString(FormatStyle.LONG),
                    valueIconPainter = painterResource(R.drawable.ic_calendar)
                )
            }
        }

        item(key = "characters-title") {
            Text(
                text = "Featured characters",
                style = MaterialTheme.typography.bodyLarge.merge(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(
            items = details.characters,
            key = { character -> character.id }
        ) { character ->
            CharacterCard(
                character = character,
                onClick = {
                    navController.navigate(CharacterPagerRoute(character.id))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsEmptyContentPreview() {
    LovefinderzzTheme {
        EpisodeDetailsEmptyContent()
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsContentPreview() {
    LovefinderzzTheme {
        EpisodeDetailsContent(
            details = EpisodeDetails(
                episode = EpisodeEntity(
                    id = 1,
                    name = "Episode Name",
                    airDate = LocalDate.now(),
                    episode = "E01S01"
                ),

                characters = listOf(
                    CharacterEntity(
                        id = 1,
                        name = "Character Name",
                        status = CharacterStatus.UNKNOWN,
                        species = "Android",
                        type = "",
                        gender = Gender.GENDERLESS,
                        imageUrl = ""
                    )
                )
            ),
            navController = rememberNavController()
        )
    }
}
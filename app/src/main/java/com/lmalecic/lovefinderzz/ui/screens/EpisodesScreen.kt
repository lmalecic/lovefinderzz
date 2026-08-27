package com.lmalecic.lovefinderzz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import com.lmalecic.lovefinderzz.ui.components.EpisodeCard
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.Typography
import com.lmalecic.lovefinderzz.viewmodel.EpisodesViewModel
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data object EpisodesRoute: AppRoute

@Composable
fun EpisodesScreen(
    viewModel: EpisodesViewModel = viewModel(),
    navController: NavHostController
) {
    val episodes by viewModel.episodes.collectAsStateWithLifecycle(initialValue = emptyList())

    EpisodesContent(
        episodes = episodes,
        navController = navController
    )
}

@Composable
fun EpisodesContent(
    episodes: List<EpisodeEntity>,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.episodes_title),
            style = Typography.titleLarge
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = episodes,
                key = { episode -> episode.id }
            ) { episode ->
                EpisodeCard(
                    episode = episode,
                    onClick = {
                        navController.navigate(EpisodePagerRoute(episode.id))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodesContentPreview() {
    LovefinderzzTheme {
        EpisodesContent(
            episodes = listOf(
                EpisodeEntity(
                    id = 1,
                    name = "Episode Name",
                    airDate = LocalDate.now(),
                    episode = "S01E01"
                )
            ),
            navController = rememberNavController()
        )
    }
}
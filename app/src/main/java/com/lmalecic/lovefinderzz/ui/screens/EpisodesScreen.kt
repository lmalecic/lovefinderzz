package com.lmalecic.lovefinderzz.ui.screens

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.EpisodeEntity
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.Typography
import com.lmalecic.lovefinderzz.viewmodel.EpisodesViewModel
import kotlinx.serialization.Serializable

@Serializable
data object EpisodesRoute: AppRoute

@Composable
fun EpisodesScreen(viewModel: EpisodesViewModel = viewModel()) {
    val episodes by viewModel.episodes.collectAsStateWithLifecycle(initialValue = emptyList())

    EpisodesContent(
        episodes = episodes
    )
}

@Composable
fun EpisodesContent(
    episodes: List<EpisodeEntity>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.episodes_title),
            style = Typography.titleLarge
        )

        LazyColumn {
            items(
                items = episodes,
                key = { episode -> episode.id }
            ) { episode ->
                Text(
                    text = episode.name
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
            episodes = emptyList()
        )
    }
}
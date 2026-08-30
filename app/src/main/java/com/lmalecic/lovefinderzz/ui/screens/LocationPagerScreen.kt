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
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.entity.LocationDetails
import com.lmalecic.lovefinderzz.entity.LocationEntity
import com.lmalecic.lovefinderzz.ui.components.CharacterCard
import com.lmalecic.lovefinderzz.ui.components.Detail
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.pageContentPadding
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.viewmodel.LocationsViewModel
import kotlinx.serialization.Serializable

@Serializable
data class LocationPagerRoute(
    val locationId: Long
) : AppRoute

@Composable
fun LocationPagerScreen(
    initialEpisodeId: Long,
    navController: NavHostController,
    viewModel: LocationsViewModel = viewModel()
) {
    val locations by viewModel.locations.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    if (locations.isEmpty()) {
        LocationDetailsEmptyContent()
        return
    }

    val initialPage = remember(locations, initialEpisodeId) {
        locations.indexOfFirst { it.id == initialEpisodeId }
            .coerceAtLeast(0)
    }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { locations.size }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            val currentLocation = locations.getOrNull(pagerState.currentPage)

            if (currentLocation != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.setFavorite(currentLocation.id, !currentLocation.favorite)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            if (currentLocation.favorite) R.drawable.ic_favorites
                            else R.drawable.ic_outline_favorites
                        ),
                        contentDescription = null
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Color.Transparent
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            pageSpacing = 32.dp
        ) { page ->
            val location = locations[page]

            val detailsFlow = remember(location.id) {
                viewModel.observeDetails(location.id)
            }

            val details by detailsFlow.collectAsStateWithLifecycle(
                initialValue = null
            )

            if (details == null) {
                LocationDetailsEmptyContent()
            } else {
                LocationDetailsContent(details = details!!, navController = navController)
            }
        }
    }
}

@Composable
fun LocationDetailsEmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LocationDetailsContent(details: LocationDetails, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .pageContentPadding(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "title") {
            Text(
                text = "Location — ${details.location.name}",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        item(key = "details") {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Detail(
                    labelText = stringResource(R.string.dimensionDetail),
                    valueText = details.location.dimension.replaceFirstChar { it.uppercase() },
                    valueIconPainter = painterResource(R.drawable.ic_dimension)
                )

                Detail(
                    labelText = stringResource(R.string.typeDetail),
                    valueText = details.location.type
                )
            }
        }

        item(key = "residents-title") {
            Text(
                text = stringResource(R.string.residents),
                style = MaterialTheme.typography.bodyLarge.merge(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (details.residents.isNotEmpty()) {
            items(
                items = details.residents,
                key = { resident -> resident.id }
            ) { resident ->
                CharacterCard(
                    character = resident,
                    onClick = {
                        navController.navigate(CharacterPagerRoute(resident.id))
                    }
                )
            }
        } else {
            item(key = "no-residents") {
                Text(
                    text = stringResource(R.string.no_residents),
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
fun LocationDetailsEmptyContentPreview() {
    LovefinderzzTheme {
        LocationDetailsEmptyContent()
    }
}

@Preview(showBackground = true)
@Composable
fun LocationDetailsContentPreview() {
    LovefinderzzTheme {
        LocationDetailsContent(
            details = LocationDetails(
                location = LocationEntity(
                    id = 1,
                    name = "Location Name",
                    type = "Virtual Machine",
                    dimension = "QEMU"
                ),

                residents = listOf(
//                    CharacterEntity(
//                        id = 1,
//                        name = "Resident Name",
//                        status = CharacterStatus.UNKNOWN,
//                        species = "Android",
//                        type = "",
//                        gender = Gender.GENDERLESS,
//                        imageUrl = ""
//                    )
                )
            ),
            navController = rememberNavController()
        )
    }
}
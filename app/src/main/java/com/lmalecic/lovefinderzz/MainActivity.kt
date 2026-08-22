package com.lmalecic.lovefinderzz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.view.AppRoute
import com.lmalecic.lovefinderzz.view.CharactersRoute
import com.lmalecic.lovefinderzz.view.CharactersView
import com.lmalecic.lovefinderzz.view.EpisodesRoute
import com.lmalecic.lovefinderzz.view.EpisodesView
import com.lmalecic.lovefinderzz.view.FavoritesRoute
import com.lmalecic.lovefinderzz.view.FavoritesView
import com.lmalecic.lovefinderzz.view.HomeRoute
import com.lmalecic.lovefinderzz.view.HomeView
import com.lmalecic.lovefinderzz.view.LocationsRoute
import com.lmalecic.lovefinderzz.view.LocationsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LovefinderzzTheme {
                LovefinderzzApp()
            }
        }
    }
}

//@PreviewScreenSizes
@Preview(name = "Phone", device = PHONE, showSystemUi = true)
@Composable
fun LovefinderzzApp() {
    val navigationController = rememberNavController()
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
        containerColor = MaterialTheme.colorScheme.background,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Color.Transparent,
            navigationBarContentColor = MaterialTheme.colorScheme.onSurface
        ),
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                val selected = it == currentDestination

                item(
                    icon = {
                        Icon(
                            painterResource(if (selected) it.selectedIcon ?: it.icon else it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = selected,
                    onClick = {
                        currentDestination = it
                        navigationController.navigate(it.route)
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navigationController,
            startDestination = AppDestinations.entries.first().route,
            modifier = Modifier.fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp)
        ) {
            composable<HomeRoute> {
                HomeView()
            }

            composable<FavoritesRoute> {
                FavoritesView()
            }

            composable<CharactersRoute> {
                CharactersView()
            }

            composable<LocationsRoute> {
                LocationsView()
            }

            composable<EpisodesRoute> {
                EpisodesView()
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
    val selectedIcon: Int?,
    val route: AppRoute
) {
    HOME(
        "Home",
        R.drawable.ic_outline_home,
        R.drawable.ic_home,
        HomeRoute,
    ),
    FAVORITES(
        "Favorites",
        R.drawable.ic_outline_favorites,
        R.drawable.ic_favorites,
        FavoritesRoute
    ),
    CHARACTERS(
        "Characters",
        R.drawable.ic_outline_characters,
        R.drawable.ic_characters,
        CharactersRoute
    ),
    LOCATIONS(
        "Locations",
        R.drawable.ic_outline_locations,
        R.drawable.ic_locations,
        LocationsRoute
    ),
    EPISODES(
        "Episodes",
        R.drawable.ic_outline_episodes,
        R.drawable.ic_episodes,
        EpisodesRoute
    )
}
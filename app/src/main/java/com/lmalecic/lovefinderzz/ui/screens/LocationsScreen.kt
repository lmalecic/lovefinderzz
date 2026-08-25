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
import com.lmalecic.lovefinderzz.entity.LocationEntity
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.Typography
import com.lmalecic.lovefinderzz.viewmodel.LocationsViewModel
import kotlinx.serialization.Serializable

@Serializable
data object LocationsRoute: AppRoute

@Composable
fun LocationsScreen(viewModel: LocationsViewModel = viewModel()) {
    val locations by viewModel.locations.collectAsStateWithLifecycle(initialValue = emptyList())

    LocationsContent(
        locations = locations
    )
}

@Composable
fun LocationsContent(
    locations: List<LocationEntity>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.locations_title),
            style = Typography.titleLarge
        )

        LazyColumn {
            items(
                items = locations,
                key = { location -> location.id }
            ) { location ->
                Text(
                    text = location.name
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationsContentPreview() {
    LovefinderzzTheme {
        LocationsContent(
            locations = emptyList()
        )
    }
}
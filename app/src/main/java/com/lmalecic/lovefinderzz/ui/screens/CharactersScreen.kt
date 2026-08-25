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
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.Typography
import com.lmalecic.lovefinderzz.viewmodel.CharactersViewModel
import kotlinx.serialization.Serializable

@Serializable
data object CharactersRoute: AppRoute

@Composable
fun CharactersScreen(viewModel: CharactersViewModel = viewModel()) {
    val characters by viewModel.characters.collectAsStateWithLifecycle(initialValue = emptyList())

    CharactersContent(
        characters = characters
    )
}

@Composable
fun CharactersContent(
    characters: List<CharacterEntity>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.characters_title),
            style = Typography.titleLarge
        )

        LazyColumn {
            items(
                items = characters,
                key = { character -> character.id }
            ) { character ->
                Text(
                    text = character.name
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharactersContentPreview() {
    LovefinderzzTheme {
        CharactersContent(
            characters = emptyList()
        )
    }
}
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
import com.lmalecic.lovefinderzz.entity.CharacterEntity
import com.lmalecic.lovefinderzz.entity.CharacterStatus
import com.lmalecic.lovefinderzz.entity.Gender
import com.lmalecic.lovefinderzz.ui.components.CharacterCard
import com.lmalecic.lovefinderzz.ui.navigation.AppRoute
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.Typography
import com.lmalecic.lovefinderzz.viewmodel.CharactersViewModel
import kotlinx.serialization.Serializable

@Serializable
data object CharactersRoute: AppRoute

@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = viewModel(),
    navController: NavHostController
) {
    val characters by viewModel.characters.collectAsStateWithLifecycle(initialValue = emptyList())

    CharactersContent(
        characters = characters,
        navigationController = navController
    )
}

@Composable
fun CharactersContent(
    characters: List<CharacterEntity>,
    navigationController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.characters_title),
            style = Typography.titleLarge
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = characters,
                key = { character -> character.id }
            ) { character ->
                CharacterCard(
                    character = character,
                    onClick = {
                        navigationController.navigate(CharacterPagerRoute(character.id))
                    }
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
            characters = listOf(
                CharacterEntity(
                    id = 1,
                    name = "Alive Character",
                    status = CharacterStatus.ALIVE,
                    species = "Android",
                    type = "",
                    gender = Gender.MALE,
                    imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                ),
                CharacterEntity(
                    id = 2,
                    name = "Dead Character",
                    status = CharacterStatus.DEAD,
                    species = "Android",
                    type = "",
                    gender = Gender.FEMALE,
                    imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                ),
                CharacterEntity(
                    id = 3,
                    name = "Unknown Character",
                    status = CharacterStatus.UNKNOWN,
                    species = "Android",
                    type = "",
                    gender = Gender.GENDERLESS,
                    imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                ),
                CharacterEntity(
                    id = 4,
                    name = "Unknown Gender Character",
                    status = CharacterStatus.UNKNOWN,
                    species = "Android",
                    type = "SuperDuperLongType Haha",
                    gender = Gender.UNKNOWN,
                    imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                )
            ),
            navigationController = rememberNavController()
        )
    }
}
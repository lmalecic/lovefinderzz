package com.lmalecic.lovefinderzz.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
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
import com.lmalecic.lovefinderzz.ui.pageContentPadding
import com.lmalecic.lovefinderzz.ui.provider.PressPositionProvider
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import com.lmalecic.lovefinderzz.ui.theme.extendedColors
import com.lmalecic.lovefinderzz.ui.theme.getColor
import com.lmalecic.lovefinderzz.viewmodel.CharactersViewModel
import com.lmalecic.lovefinderzz.viewmodel.ImageSaveEvent
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.math.roundToInt

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

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()

    val permissionDeniedMessage = stringResource(R.string.image_save_storage_permission_denied)
    val imageSavedMessage = stringResource(R.string.image_saved_to_gallery)
    val imageSaveFailedMessage = stringResource(R.string.save_image_gallery_failed)

    LaunchedEffect(viewModel) {
        viewModel.imageSaveEvents.collect { event ->
            when (event) {
                is ImageSaveEvent.Saved -> snackbarHostState.showSnackbar(
                    message = imageSavedMessage,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short,
                )

                is ImageSaveEvent.Failed -> snackbarHostState.showSnackbar(
                    message = imageSaveFailedMessage,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            val currentCharacter = characters.getOrNull(pagerState.currentPage)

            if (currentCharacter != null) {
                FloatingActionButton(
                    onClick = {
                        viewModel.setFavorite(currentCharacter.id, !currentCharacter.favorite)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            if (currentCharacter.favorite) R.drawable.ic_favorites
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
                CharacterDetailsContent(
                    details = details!!,
                    navController = navController,
                    onSaveToGallery = {
                        viewModel.saveImageToGallery(details!!.character.imageUrl, details!!.character.name)
                    },
                    onStoragePermissionDenied = {
                        snackbarScope.launch {
                            snackbarHostState.showSnackbar(
                                message = permissionDeniedMessage,
                                withDismissAction = true,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                )
            }
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
fun CharacterDetailsContent(
    details: CharacterDetails,
    navController: NavController,
    onSaveToGallery: () -> Unit = {},
    onStoragePermissionDenied: () -> Unit = {}
) {
    val context = LocalContext.current

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onSaveToGallery()
        } else {
            onStoragePermissionDenied()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .pageContentPadding(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "title") {
            Text(
                text = "Character — ${details.character.name}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item(key = "image") {
            CharacterImage(
                imageUrl = details.character.imageUrl,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                onSaveToGallery = {
                    val permissionAlreadyGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED

                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                            // Android 10+, no permission required.
                            onSaveToGallery()
                        }

                        permissionAlreadyGranted -> {
                            onSaveToGallery()
                        }

                        else -> {
                            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    }
                }
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
                                    .padding(
                                        3.5.dp * scale,
                                        3.5.dp * scale,
                                        scale.dp,
                                        3.5.dp * scale
                                    )
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
                text = stringResource(R.string.appears_in),
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
                    text = stringResource(R.string.character_no_episode),
                    style = MaterialTheme.typography.bodySmall.merge(
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Composable
fun CharacterImage(
    imageUrl: String,
    onSaveToGallery: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuVisible by remember { mutableStateOf(false) }
    var pressPosition by remember { mutableStateOf(IntOffset.Zero) }

    Box(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onLongPress = { position ->
                    pressPosition = IntOffset(position.x.roundToInt(), position.y.roundToInt())
                    menuVisible = true
                }
            )
        }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        if (menuVisible) {
            Popup(
                popupPositionProvider = remember(pressPosition) {
                    PressPositionProvider(pressPosition)
                },
                onDismissRequest = { menuVisible = false },
                properties = PopupProperties(focusable = true)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.width(IntrinsicSize.Max)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(R.string.save_to_gallery)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_download),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                menuVisible = false
                                onSaveToGallery()
                            }
                        )
                    }
                }
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
            navController = rememberNavController(),
        )
    }
}
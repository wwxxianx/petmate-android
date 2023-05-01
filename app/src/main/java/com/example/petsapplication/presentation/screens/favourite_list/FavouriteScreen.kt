package com.example.petsapplication.presentation.screens.favourite_list

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.AnonymousScreen
import com.example.petsapplication.common.composable.PetTopAppBar
import com.example.petsapplication.common.composable.PrimaryButton
import com.example.petsapplication.common.composable.PrimaryIconButton
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.petListExample
import com.example.petsapplication.presentation.screens.home.HomeSections
import com.example.petsapplication.presentation.screens.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun FavouriteScreen(
    navigate: (String) -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavouriteViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            PetTopAppBar(
                navigateBack = navigateBack,
                label = R.string.favourite_pets
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        val uiState = viewModel.uiState.value
        val coroutineScope = rememberCoroutineScope()
        if (uiState.user.anonymous) {
            AnonymousScreen(
                navigateAndPopUp = navigateAndPopUp,
                popUpFromRoute = HomeSections.FAVOURITE.route
            )
        } else {
            if (uiState.isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    if (uiState.favouritePetList.isEmpty()) {
                        item {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 36.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.no_favourite_pet),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.h2
                                )
                                PrimaryButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = R.string.explore_now
                                ) {
                                    viewModel.onNavigateToPetScreen(navigateAndPopUp)
                                }
                            }
                        }
                    } else {
                        if (uiState.showingRemoveDialog) {
                            item {
                                RemovePetDialog(
                                    onCloseDialog = viewModel::onCloseDialog,
                                    onRemovePetFromFavourite = viewModel::onRemovePetFromFavourite,
                                )
                            }
                        }
                        items(uiState.favouritePetList) { pet ->
                            FullSizedPetCard(
                                pet = pet,
                                getPetImage = viewModel::getPetImageUrl,
                                onOpenDialog = viewModel::onOpenDialog,
                                navigate = navigate,
                                onNavigateToChat = viewModel::onNavigateToChat,
                                coroutineScope = coroutineScope
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RemovePetDialog(
    onCloseDialog: () -> Unit,
    onRemovePetFromFavourite: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCloseDialog,
        title = {
            Text(text = "Pet Already in Your Favourite List")
        },
        text = {
            Text(text = "Remove this pet from your favourite list?")
        },
        confirmButton = {
            TextButton(
                onClick = onRemovePetFromFavourite
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCloseDialog,
            ) {
                Text("No")
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun FullSizedPetCard(
    pet: Pet,
    getPetImage: suspend (String) -> String,
    navigate: (String) -> Unit,
    onOpenDialog: (pet: Pet) -> Unit,
    onNavigateToChat: (String, (String) -> Unit) -> Unit,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    var imageUrl by remember { mutableStateOf("") }
    coroutineScope.launch {
        imageUrl = getPetImage(pet.id)
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
            .padding(8.dp)
            .heightIn(min = 110.dp, max = 130.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            if (imageUrl.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    color = Color(0xFFC6F2F0),
                    modifier = Modifier
                        .size(108.dp)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "${pet.name}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(14.dp)
                    )
                }

            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp)
            ) {
                Row() {
                    Column(
                        modifier = Modifier.weight(3f)
                    ) {
                        Text(
                            text = "${pet.name}",
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            text = "${pet.breed} (${pet.sex})",
                            style = MaterialTheme.typography.subtitle2
                        )
                    }

                    FilledIconButton(
                        shape = RoundedCornerShape(4.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFFD6767),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(4.dp))
                            .padding(0.dp)
                            .size(26.dp),
                        onClick = { onOpenDialog(pet) }
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.remove_from_favourite),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

                PrimaryButton(
                    label = R.string.adopt_now,
                    textStyle = MaterialTheme.typography.caption,
                    height = 40.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End)
                ) {
                    // Adopt Action
                    onNavigateToChat(pet.userId, navigate)
                }
            }
        }
    }
}

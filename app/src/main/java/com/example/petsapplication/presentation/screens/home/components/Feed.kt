package com.example.petsapplication.presentation.screens.home.components

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.petsapplication.R
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.common.composable.Avatar
import com.example.petsapplication.common.composable.LoveButton
import com.example.petsapplication.common.composable.PrimaryButton
import com.example.petsapplication.common.composable.TextFieldWithIcon
import com.example.petsapplication.common.getRandomColour
import com.example.petsapplication.model.*
import com.example.petsapplication.presentation.screens.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Feed(
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .clickable {
                    // Remove the keyboard when users click
                    // outside the searchfield
                    focusManager.clearFocus()
                }
        ) {
            HomeHeader(
                user = uiState.user,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            SearchField(
                value = uiState.searchText,
                onValueChanged = viewModel::onSearchTextChange,
                onSearchStateChange = viewModel::onSearchStateChange,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            PetsFeed(
                navigate = navigate,
                getPetImage = viewModel::getPetImageUrl,
                selectedCategory = uiState.selectedCategory,
                isLoading = uiState.petsIsLoading,
                isFailed = uiState.petsIsFailed,
                pets = uiState.pets,
                onCategoryChanged = viewModel::onCategoryChanged,
                onAddPetToFavourite = viewModel::onAddPetToFavourite,
                coroutineScope = coroutineScope
            )
        }

        if (uiState.isSearching) {
            SearchResult(
                results = uiState.searchResults,
                onNavigateToPetDetail = viewModel::onNavigateToPetDetail,
                navigate = navigate
            )
        }
    }
}

@Composable
fun PetsFeed(
    selectedCategory: String,
    isLoading: Boolean,
    isFailed: Boolean,
    pets: List<Pet>,
    onCategoryChanged: (String) -> Unit,
    onAddPetToFavourite: suspend (Pet) -> Unit,
    shouldShowCategoryHeaderLabel: Boolean = true,
    shouldShowPetHeaderLabel: Boolean = true,
    shouldShowHeaderButton: Boolean = true,
    navigate: (String) -> Unit,
    getPetImage: suspend (String) -> String,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column {
            SectionHeader(
                headerLabel = R.string.categories,
                shouldShowHeaderLabel = shouldShowCategoryHeaderLabel,
                shouldShowHeaderButton = shouldShowHeaderButton,
                navigateToRoute = navigate,
                modifier = modifier.padding(horizontal = 20.dp)
            )
            CategoryItemList(
                categoryItemList = categoryItemList,
                selectedCategory = selectedCategory,
                onCategoryChanged = onCategoryChanged,
            )
        }

        Column {
            SectionHeader(
                headerLabel = R.string.pets,
                shouldShowHeaderLabel = shouldShowPetHeaderLabel,
                shouldShowHeaderButton = shouldShowHeaderButton,
                navigateToRoute = navigate,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        CircularProgressIndicator()
                        PrimaryButton(label = R.string.print) {
                            println("pet list: ${pets.size}")
                        }
                    }
                }
            } else if (isFailed) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Unexpected Error...try again")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .padding(start = 4.dp, end = 20.dp)
                        .padding(top = if (shouldShowPetHeaderLabel) 0.dp else 16.dp)
                ) {
                    items(pets) { pet ->
                        PetCard(
                            navigateToRoute = navigate,
                            pet = pet,
                            getPetImage = getPetImage,
                            onAddPetToFavourite = onAddPetToFavourite,
                            coroutineScope = coroutineScope
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    @StringRes headerLabel: Int,
    shouldShowHeaderLabel: Boolean = true,
    shouldShowHeaderButton: Boolean = true,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        if (shouldShowHeaderLabel) {
            Text(
                text = stringResource(id = headerLabel),
                style = MaterialTheme.typography.subtitle1
            )
        }
        if (shouldShowHeaderButton) {
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = Color.DarkGray
                ),
                onClick = { navigateToRoute(ScreenRoutes.PETS_ROUTE) }) {
                Text(
                    text = stringResource(R.string.view_all),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PetCard(
    navigateToRoute: (String) -> Unit,
    pet: Pet,
    shouldShowFavourite: Boolean = true,
    getPetImage: suspend (String) -> String,
    coroutineScope: CoroutineScope,
    onAddPetToFavourite: suspend (Pet) -> Unit,
    modifier: Modifier = Modifier
) {
    var imageUrl by remember { mutableStateOf("") }
    coroutineScope.launch {
        imageUrl = getPetImage(pet.id)
    }
    Box(
        modifier = modifier
            .size(152.dp, 198.dp)
            .clickable { navigateToRoute("${ScreenRoutes.PET_DETAIL_ROUTE}?${ScreenRoutes.PET_ID}=${pet.id}") }
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp)
                .padding(bottom = 8.dp)
                .aspectRatio(0.898f)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                if (imageUrl.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        color = getRandomColour(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(128.dp)
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 4.dp)
                ) {
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

                    if (shouldShowFavourite) {
                        // Love Icon
                        LoveButton() {
                            // Add to favourite action
                            coroutineScope.launch {
                                onAddPetToFavourite(pet)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItemList(
    selectedCategory: String,
    onCategoryChanged: (String) -> Unit,
    categoryItemList: List<CategoryItem>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        items(categoryItemList) { item ->
            CategoryItem(
                item = item,
                selectedCategory = selectedCategory,
                onCategoryChanged = onCategoryChanged
            )
        }
    }
}

@Composable
fun CategoryItem(
    item: CategoryItem,
    selectedCategory: String,
    onCategoryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = selectedCategory == item.name
    Surface(
        color = if(isSelected) Color(0xFFFEFFE0) else Color.White,
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
            .width(58.dp)
            .height(if (isSelected) 85.dp else 58.dp)
            .clip(RoundedCornerShape(100.dp))
            .clickable { onCategoryChanged(item.name) }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(12.dp)
                .background(if (isSelected) Color(0xFFFEFFE0) else Color.White)
        )
    }
}
@Composable
private fun HomeHeader(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Avatar(
            shadowOffsetY = 2.dp,
            shadowBorderRadius = 120.dp,
            imageUri = user.imageUrl.toUri()
        )
        Text(
            text = "Hi ${user.name ?: "User"}",
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun SearchResult(
    results: List<Pet>,
    navigate: (String) -> Unit,
    onNavigateToPetDetail: (String, (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 30.dp)
            .offset(y = 135.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (results.isEmpty()) {
                // None matched result
                item {
                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.no_pets_found),
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                items(results) { pet ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clickable {
                                // Navigate to PetDetailScreen
                                onNavigateToPetDetail(pet.id, navigate)
                            }
                    ) {
                        Column() {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${pet.name}",
                                    style = MaterialTheme.typography.h3
                                )
                                Text(
                                    text = "(${pet.breed})",
                                    style = MaterialTheme.typography.body1
                                )
                            }
                            Text(
                                text = "${pet.weight}kg (${pet.age} year olds)",
                                style = MaterialTheme.typography.body2
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.arrow_forward),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChanged: (String) -> Unit,
    onSearchStateChange: (Boolean) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions { },
    modifier: Modifier = Modifier
) {
    TextFieldWithIcon(
        value = value,
        onValueChanged = { onValueChanged(it) },
        icon = Icons.Rounded.Search,
        label = R.string.search_text,
        shape = RoundedCornerShape(24.dp),
        shadowBorderRadius = 24.dp,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                onSearchStateChange(focusState.isFocused)
            }
    )
}
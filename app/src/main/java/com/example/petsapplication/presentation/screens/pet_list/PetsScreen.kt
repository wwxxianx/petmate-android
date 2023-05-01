package com.example.petsapplication.presentation.screens.pet_list

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.PetTopAppBar
import com.example.petsapplication.common.composable.PrimaryButton
import com.example.petsapplication.model.Pet
import com.example.petsapplication.presentation.screens.home.HomeViewModel
import com.example.petsapplication.presentation.screens.home.components.PetsFeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PetsScreen(
    navigateToRoute: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            PetTopAppBar(navigateBack = navigateBack, label = R.string.pets)
        },
        modifier = modifier
    ) { innerPadding ->
        val uiState by viewModel.uiState
        val coroutineScope = rememberCoroutineScope()
        PetsFeed(
            selectedCategory = uiState.selectedCategory,
            isLoading = uiState.petsIsLoading,
            isFailed = uiState.petsIsFailed,
            pets = uiState.pets,
            onCategoryChanged = viewModel::onCategoryChanged,
            onAddPetToFavourite = viewModel::onAddPetToFavourite,
            navigate = navigateToRoute,
            getPetImage = viewModel::getPetImageUrl,
            coroutineScope = coroutineScope,
            shouldShowPetHeaderLabel = false,
            shouldShowHeaderButton = false,
            modifier = Modifier.padding(innerPadding).padding(top = 16.dp)
        )
    }
}


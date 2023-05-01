package com.example.petsapplication.presentation.screens.favourite_list

import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.User

data class FavouriteUiState (
    val user: User = User(),
    val favouritePetList: List<Pet> = emptyList(),
    val showingRemoveDialog: Boolean = false,
    val selectedPet: Pet = Pet(),
    val isLoading: Boolean = true,
    val isFailed: Boolean = false
)
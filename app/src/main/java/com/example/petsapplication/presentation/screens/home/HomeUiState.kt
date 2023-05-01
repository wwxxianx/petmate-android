package com.example.petsapplication.presentation.screens.home

import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.Response
import com.example.petsapplication.model.User
import com.example.petsapplication.model.petListExample

data class HomeUiState(
    val allPets: List<Pet> = emptyList(),
    val selectedCategory: String = "CAT",
    val pets: List<Pet> = emptyList(),
    val user: User = User(),

    val petsIsLoading: Boolean = true,
    val petsIsFailed: Boolean = false,

    val isSearching: Boolean = false,
    val searchText: String = "",
    val searchResults: List<Pet> = emptyList()
    )
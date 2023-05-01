package com.example.petsapplication.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import com.example.petsapplication.PetAppViewModel
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.Response
import com.example.petsapplication.model.petListExample
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirebaseStorageService
import com.example.petsapplication.model.service.FirestoreService
import com.example.petsapplication.use_case.AddPetToFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService,
    private val firebaseStorageService: FirebaseStorageService,
    private val firestoreService: FirestoreService,
    private val addPetToFavouriteUseCase: AddPetToFavouriteUseCase
) : PetAppViewModel() {
    var uiState = mutableStateOf(HomeUiState())
        private set

    private val selectedCategory
        get() = uiState.value.selectedCategory

    private val allPets
        get() = uiState.value.allPets

    private val pets
        get() = uiState.value.pets

    private val searchText
        get() = uiState.value.searchText

    init {
        launchCatching {
            initPets()
            // Init current user
            val user = firestoreService.getCurrentUser()
            uiState.value = uiState.value.copy(user = user)
        }
    }

    private suspend fun initPets() {
        when (val allPets = firestoreService.getPets()) {
            is Response.Success -> {
                uiState.value = uiState.value.copy(
                    allPets = allPets.data ?: petListExample,
                    pets = allPets.data?.filter { it.type == selectedCategory } ?: emptyList(),
                    petsIsLoading = false
                )
            }
            is Response.Failure -> {
                uiState.value = uiState.value.copy(petsIsFailed = true)
            }
            is Response.Loading -> {
                uiState.value = uiState.value.copy(petsIsLoading = true)
            }
        }
    }

    suspend fun getPetImageUrl(path: String): String
        = firebaseStorageService.getPetImageUrl(path)

    fun onNavigateToPet(navigate: (String) -> Unit) {
        navigate(ScreenRoutes.PETS_ROUTE)
    }

    fun onNavigateToPetDetail(petId: String, navigate: (String) -> Unit) {
        navigate("${ScreenRoutes.PET_DETAIL_ROUTE}?${ScreenRoutes.PET_ID}=${petId}")
    }

    fun onCategoryChanged(category: String) {
        val filteredPets = allPets.filter { it.type == category }
        uiState.value = uiState.value.copy(pets = filteredPets ?: emptyList(), selectedCategory = category)
    }

    suspend fun onAddPetToFavourite(pet: Pet) {
        addPetToFavouriteUseCase(pet)
    }

    fun onSearchTextChange(text: String) {
        uiState.value = uiState.value.copy(
            searchText = text,
            searchResults = allPets.filter { it.doesMatchSearchQuery(searchText) }
        )
        if (searchText.isBlank()) {
            uiState.value = uiState.value.copy(searchResults = emptyList())
            return
        }
    }

    fun onSearchStateChange(isSearching: Boolean) {
        uiState.value = uiState.value.copy(isSearching = isSearching)
    }
}
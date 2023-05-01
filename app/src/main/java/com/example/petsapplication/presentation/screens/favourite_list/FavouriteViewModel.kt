package com.example.petsapplication.presentation.screens.favourite_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.petsapplication.PetAppViewModel
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.common.snackbar.SnackbarMessage
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.Response
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirebaseStorageService
import com.example.petsapplication.model.service.FirestoreService
import com.example.petsapplication.presentation.screens.home.HomeSections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val authService: AuthService,
    private val firebaseStorageService: FirebaseStorageService,
    private val firestoreService: FirestoreService,
) : PetAppViewModel() {
    var uiState = mutableStateOf(FavouriteUiState())
        private set

    init {
        launchCatching {
            val user = firestoreService.getCurrentUser()
            uiState.value = uiState.value.copy(
                user = user
            )

            when(val favouritePetList = firestoreService.getFavouritePetList()) {
                is Response.Success -> {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        favouritePetList = favouritePetList.data ?: emptyList()
                    )
                }
                is Response.Loading -> {
                    uiState.value = uiState.value.copy(isLoading = true)
                }
                is Response.Failure -> {
                    uiState.value = uiState.value.copy(isFailed = true)
                    SnackbarManager.showMessage(SnackbarMessage.StringSnackbar(favouritePetList.message!!))
                }
            }
        }
    }

    suspend fun getPetImageUrl(path: String): String
            = firebaseStorageService.getPetImageUrl(path)

    fun onNavigateToChat(chatUserId: String, navigate: (String) -> Unit) {
        navigate("${ScreenRoutes.CHAT}?${ScreenRoutes.CHAT_ID}=${chatUserId}")
    }

    fun onNavigateToPetScreen(navigateAndPopUp: (String, String) -> Unit) {
        navigateAndPopUp(ScreenRoutes.PETS_ROUTE, HomeSections.FAVOURITE.route)
    }

    fun onOpenDialog(pet: Pet) {
        uiState.value = uiState.value.copy(
            showingRemoveDialog = true,
            selectedPet = pet
        )
    }

    fun onCloseDialog() {
        uiState.value = uiState.value.copy(showingRemoveDialog = false)
    }

    fun onRemovePetFromFavourite() {
        launchCatching {
            firestoreService.removePetFromFavourite(uiState.value.selectedPet.id)
            uiState.value = uiState.value.copy(showingRemoveDialog = false)
        }
    }
}
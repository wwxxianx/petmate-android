package com.example.petsapplication.presentation.screens.pet_detail

import androidx.compose.runtime.mutableStateOf
import com.example.petsapplication.PetAppViewModel
import com.example.petsapplication.R
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.User
import com.example.petsapplication.model.petListExample
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirebaseStorageService
import com.example.petsapplication.model.service.FirestoreService
import com.example.petsapplication.use_case.AddPetToFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val firebaseStorageService: FirebaseStorageService,
) : PetAppViewModel() {
    var pet = mutableStateOf(Pet())
    var imageUrl = mutableStateOf("")
    var owner = mutableStateOf(User())

    suspend fun initialize(petId: String) {
        // Emulate the fetching and initializing process
        launchCatching {
            pet.value = firestoreService.getPet(petId)
            imageUrl.value = firebaseStorageService.getPetImageUrl(pet.value.id)
            owner.value = firestoreService.getUser(pet.value.userId)
        }
    }

    fun onAddPetToFavourite() {

        launchCatching {
            firestoreService.addPetToFavourite(pet.value)
            SnackbarManager.showMessage(R.string.pet_added_to_favourite)
        }
    }

    fun onAdoptPet(navigate: (String) -> Unit) {
        launchCatching {
            if (owner.value.id == authService.currentUser.first().id) {
                SnackbarManager.showMessage(R.string.you_are_the_owner)
                return@launchCatching
            } else {
                // Navigate to the owner chat
                navigate("${ScreenRoutes.CHAT}?${ScreenRoutes.CHAT_ID}=${owner.value.id}")
            }
        }
    }
}
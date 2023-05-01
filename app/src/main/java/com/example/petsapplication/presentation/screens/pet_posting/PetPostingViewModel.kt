package com.example.petsapplication.presentation.screens.pet_posting

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.example.petsapplication.PetAppViewModel
import com.example.petsapplication.R
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.common.extension.isValidDescription
import com.example.petsapplication.common.extension.isValidName
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.PetType
import com.example.petsapplication.model.Sex
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirestoreService
import com.example.petsapplication.presentation.screens.home.HomeSections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class PetPostingViewModel @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
) : PetAppViewModel() {
    var uiState = mutableStateOf(PetPostingUiState())
        private set

    private val name
        get() = uiState.value.name

    private val age
        get() = uiState.value.age

    private val selectedImageUri
        get() = uiState.value.selectedImageUri

    private val breed
        get() = uiState.value.breed

    private val description
        get() = uiState.value.description

    private val weight
        get() = uiState.value.weight

    private val sex
        get() = uiState.value.sex

    private val petType
        get() = uiState.value.petType

    fun onImageChange(uri: Uri?) {
        uiState.value = uiState.value.copy(selectedImageUri = uri)
    }

    fun onNameChange(name: String) {
        uiState.value = uiState.value.copy(name = name)
    }

    fun onAgeChange(age: String) {
        uiState.value = uiState.value.copy(age = age)
    }

    fun onBreedChange(breed: String) {
        uiState.value = uiState.value.copy(breed = breed)
    }

    fun onWeightChange(weight: String) {
        uiState.value = uiState.value.copy(weight = weight)
    }

    fun onDescriptionChange(description: String) {
        uiState.value = uiState.value.copy(description = description)
    }

    fun onClosePetTypeDropdown() {
        uiState.value = uiState.value.copy(showingPetTypeDropdown = false)
    }

    fun onOpenPetTypeDropdown() {
        uiState.value = uiState.value.copy(showingPetTypeDropdown = true)
    }

    fun onPetTypeChange(petType: PetType) {
        uiState.value = uiState.value.copy(petType = petType, showingPetTypeDropdown = false)
    }

    fun onCloseSexDropdown() {
        uiState.value = uiState.value.copy(showingSexDropdown = false)
    }

    fun onOpenSexDropdown() {
        uiState.value = uiState.value.copy(showingSexDropdown = true)
    }

    fun onSexChange(sex: Sex) {
        uiState.value = uiState.value.copy(sex = sex, showingSexDropdown = false)
    }

    fun savePet(navigateAndPopUp: (String, String) -> Unit) {
        if (selectedImageUri == null) {
            SnackbarManager.showMessage(R.string.invalid_pet_photo)
            return
        }

        if (!name.isValidName()) {
            SnackbarManager.showMessage(R.string.invalid_name)
            return
        }

        if (age.toIntOrNull() == null || age.toInt() <= 0) {
            SnackbarManager.showMessage(R.string.invalid_age)
            return
        }

        if (weight.toDoubleOrNull() == null || weight.toDouble() <= 0) {
            SnackbarManager.showMessage(R.string.invalid_weight)
            return
        }

        if (!breed.isValidName()) {
            SnackbarManager.showMessage(R.string.invalid_breed)
            return
        }

        if (!description.isValidDescription()) {
            SnackbarManager.showMessage(R.string.invalid_description)
            return
        }

        launchCatching {
            val pet = Pet(
                name = name,
                age = age.toInt(),
                breed = breed,
                weight = weight.toDouble(),
                imageUrl = selectedImageUri?.lastPathSegment.toString(),
                sex = sex.toString(),
                type = petType.toString(),
                description = description,
                userId = authService.currentUser.first().id
            )
            firestoreService.savePet(pet = pet, petImageUri = selectedImageUri!!)
            navigateAndPopUp(HomeSections.PROFILE.route, ScreenRoutes.PET_POSTING_ROUTE)
        }
    }
}
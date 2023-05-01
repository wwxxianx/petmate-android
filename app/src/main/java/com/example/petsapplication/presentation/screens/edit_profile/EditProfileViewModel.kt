package com.example.petsapplication.presentation.screens.edit_profile

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.petsapplication.PetAppViewModel
import com.example.petsapplication.R
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.common.extension.isValidName
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.model.service.FirebaseStorageService
import com.example.petsapplication.model.service.FirestoreService
import com.example.petsapplication.presentation.screens.home.HomeSections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val firestoreService: FirestoreService,
    private val firebaseStorageService: FirebaseStorageService
) : PetAppViewModel() {
    var uiState = mutableStateOf(EditProfileUiState())

    private val selectedImageUri
        get() = uiState.value.selectedImageUri

    private val name
        get() = uiState.value.name

    private val age
        get() = uiState.value.age

    init {
        launchCatching {
            uiState.value = uiState.value.copy(
                currentUser = firestoreService.getCurrentUser()
            )
        }
    }

    fun onNameChange(name: String) {
        uiState.value = uiState.value.copy(name = name)
    }

    fun onAgeChange(age: String) {
        uiState.value = uiState.value.copy(age = age)
    }

    fun onImageChange(uri: Uri?) {
        uiState.value = uiState.value.copy(selectedImageUri = uri)
    }

    fun onSaveUserProfile(navigateAndPopUp: (String, String) -> Unit) {
        if (!name.isValidName()) {
            SnackbarManager.showMessage(R.string.invalid_name)
            return
        }

        if (age.toIntOrNull() == null || age.toInt() <= 0) {
            SnackbarManager.showMessage(R.string.invalid_age)
            return
        }

        launchCatching {
            var user = firestoreService.getCurrentUser()
            if (selectedImageUri != null) {
                val userImageUrl = firebaseStorageService.saveAvatarImage(selectedImageUri!!)
                user = user.copy(imageUrl = userImageUrl.toString())
            }
            user = user.copy(
                name = uiState.value.name,
                age = uiState.value.age.toInt()
            )
            firestoreService.saveUser(user)
            navigateAndPopUp(HomeSections.PROFILE.route, ScreenRoutes.EDIT_PROFILE_ROUTE)
        }
    }
}
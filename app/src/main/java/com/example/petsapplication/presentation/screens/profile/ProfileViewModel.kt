package com.example.petsapplication.presentation.screens.profile

import androidx.compose.runtime.mutableStateOf
import com.example.petsapplication.PetAppViewModel
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirestoreService
import com.example.petsapplication.presentation.screens.home.HomeSections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
) : PetAppViewModel() {
    var uiState = mutableStateOf(ProfileUiState())
        private set

    init {
        launchCatching {
            val user = firestoreService.getCurrentUser()
            uiState.value = uiState.value.copy(
                user = user
            )
        }
    }

    fun printUser() {
        println("User id: ${uiState.value.user.id}")
        println("User name: ${uiState.value.user.name}")
        println("User age: ${uiState.value.user.age}")
        println("User isAnonymous: ${uiState.value.user.anonymous}")
    }

    fun onOpenDialog() {
        uiState.value = uiState.value.copy(showingSignOutDialog = true)
    }

    fun onCloseDialog() {
        uiState.value = uiState.value.copy(showingSignOutDialog = false)
    }

    fun onNavigateToSignIn(navigateAndPopUp: (String, String) -> Unit) {
        navigateAndPopUp(ScreenRoutes.LOGIN_ROUTE, HomeSections.PROFILE.route)
    }

    fun onNavigateToEditProfile(navigate: (String) -> Unit) {
        navigate(ScreenRoutes.EDIT_PROFILE_ROUTE)
    }

    fun onNavigateToPetPosting(navigate: (String) -> Unit) {
        navigate(ScreenRoutes.PET_POSTING_ROUTE)
    }

    fun onSignOut(navigateAndPopUp: (String, String) -> Unit) {
        launchCatching {
            authService.signOut()
            navigateAndPopUp(ScreenRoutes.LOGIN_ROUTE, HomeSections.PROFILE.route)
        }
    }
}
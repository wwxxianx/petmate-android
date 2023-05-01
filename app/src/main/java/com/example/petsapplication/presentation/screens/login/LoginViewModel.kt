package com.example.petsapplication.presentation.screens.login

import android.content.Context
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.petsapplication.*
import com.example.petsapplication.common.extension.isValidEmail
import com.example.petsapplication.common.extension.isValidPassword
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirestoreService
import com.example.petsapplication.presentation.screens.home.HomeSections
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService
) : PetAppViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password
    private val repeatPassword
        get() = uiState.value.repeatPassword

    fun getUserLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        println("Location: ${location.latitude}, ${location.longitude}")
                    } else {
                        println("Location is null")
                    }
                }
        } catch(e: SecurityException) {
            println("Cannot get user's location")
        }
    }

    fun onNavigateToSignUpClick(navigateAndPopUp: (String) -> Unit) {
        navigateAndPopUp(ScreenRoutes.SIGN_UP_ROUTE)
    }

    fun onCloseDialog() {
        uiState.value = uiState.value.copy(showingDialog = false)
    }

    fun onOpenDialog() {
        uiState.value = uiState.value.copy(showingDialog = true)
    }

    fun onDialogEmailChange(email: String) {
        uiState.value = uiState.value.copy(dialogEmail = email)
    }

    fun onSignInClick(navigateToHome: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.enter_valid_email)
            return
        }
        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(R.string.password_constraint)
            return
        }
        if (repeatPassword != password) {
            SnackbarManager.showMessage(R.string.repeat_password_constraint)
            return
        }
        launchCatching {
            authService.authenticate(email, password)
            navigateToHome(HomeSections.FEED.route, ScreenRoutes.LOGIN_ROUTE)
        }
    }

    fun onAnonymousSignInClick(navigateToHome: (String, String) -> Unit) {
        launchCatching {
            authService.anonymousSignIn()
            navigateToHome(HomeSections.FEED.route, ScreenRoutes.LOGIN_ROUTE)
        }
    }

    fun onEmailChanged(email: String) {
        uiState.value = uiState.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        uiState.value = uiState.value.copy(password = password)
    }

    fun onRepeatPasswordChanged(repeatPassword: String) {
        uiState.value = uiState.value.copy(repeatPassword = repeatPassword)
    }

    fun onResetPassword() {
        launchCatching {
            uiState.value = uiState.value.copy(
                dialogEmail = "",
                showingDialog = false
            )
            authService.resetPassword(uiState.value.dialogEmail)
            SnackbarManager.showMessage(R.string.reset_password_success)
        }
    }
}
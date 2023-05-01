package com.example.petsapplication.presentation.screens.channel

import androidx.compose.runtime.mutableStateOf
import com.example.petsapplication.PetAppViewModel
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.common.snackbar.SnackbarMessage
import com.example.petsapplication.model.Response
import com.example.petsapplication.model.User
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirebaseStorageService
import com.example.petsapplication.model.service.FirestoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val firebaseStorageService: FirebaseStorageService
) : PetAppViewModel() {
    var uiState = mutableStateOf(ChannelUiState())
        private set

    init {
        launchCatching {
            uiState.value = uiState.value.copy(currentUser = firestoreService.getCurrentUser())
            when (val channelUserList = firestoreService.getCurrentUserChannel()) {
                is Response.Success -> {
                    uiState.value = uiState.value.copy(
                        channelUserList = channelUserList.data ?: emptyList(),
                        isLoading = false,
                        isFailed = false
                    )
                }
                is Response.Loading -> {
                    uiState.value = uiState.value.copy(isLoading = true)
                }
                is Response.Failure -> {
                    SnackbarManager.showMessage(SnackbarMessage.StringSnackbar(channelUserList.message!!))
                    uiState.value = uiState.value.copy(isFailed = true)
                }
            }
        }
    }

    fun onNavigateToChat(user: User, navigate: (String) -> Unit) {
        navigate("${ScreenRoutes.CHAT}?${ScreenRoutes.CHAT_ID}=${user.id}")
    }
}
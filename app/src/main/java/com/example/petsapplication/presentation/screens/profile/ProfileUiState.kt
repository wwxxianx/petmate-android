package com.example.petsapplication.presentation.screens.profile

import com.example.petsapplication.model.User

data class ProfileUiState(
    val user: User = User(),
    val userIsAnonymous: Boolean = true,
    val showingSignOutDialog: Boolean = false
)
package com.example.petsapplication.presentation.screens.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val showingDialog: Boolean = false,
    val dialogEmail: String = "",
)
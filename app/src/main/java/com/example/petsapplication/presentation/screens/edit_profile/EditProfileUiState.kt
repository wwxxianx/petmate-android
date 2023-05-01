package com.example.petsapplication.presentation.screens.edit_profile

import android.net.Uri
import com.example.petsapplication.model.User

data class EditProfileUiState(
    val currentUser: User = User(),
    val selectedImageUri: Uri? = null,
    val name: String = "",
    val age: String = "18"
)

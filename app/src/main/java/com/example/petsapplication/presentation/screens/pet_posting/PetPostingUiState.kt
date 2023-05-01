package com.example.petsapplication.presentation.screens.pet_posting

import android.net.Uri
import com.example.petsapplication.model.PetType
import com.example.petsapplication.model.Sex

data class PetPostingUiState(
    val selectedImageUri: Uri? = null,
    val name: String = "",
    val age: String = "",
    val breed: String = "",
    val petType: PetType = PetType.CAT,
    val sex: Sex = Sex.FEMALE,
    val weight: String = "",
    val description: String = "",
    val showingSexDropdown: Boolean = false,
    val showingPetTypeDropdown: Boolean = false
)
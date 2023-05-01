package com.example.petsapplication.use_case

import com.example.petsapplication.R
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirestoreService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddPetToFavouriteUseCase @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService
) {
    suspend operator fun invoke(pet: Pet) {
        if (firestoreService.getCurrentUser().anonymous) {
            SnackbarManager.showMessage(R.string.please_sign_in)
            return
        }
        coroutineScope {
            firestoreService.addPetToFavourite(pet)
            SnackbarManager.showMessage(R.string.pet_added_to_favourite)
        }
    }
}
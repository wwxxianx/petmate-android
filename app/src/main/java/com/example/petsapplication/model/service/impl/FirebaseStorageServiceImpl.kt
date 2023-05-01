package com.example.petsapplication.model.service.impl

import android.net.Uri
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirebaseStorageService
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageServiceImpl @Inject constructor(
    private val authService: AuthService,
    private val firebaseStorage: FirebaseStorage
) : FirebaseStorageService {
    override var avatarImageRef: StorageReference
        = firebaseStorage.reference.child("images/avatar")

    override var petImageRef: StorageReference
            = firebaseStorage.reference.child("images/pet")

    override suspend fun getCurrentUserImageUrl(): String {
        try {
            return avatarImageRef.child(authService.currentUser.first().id).downloadUrl.await().toString()
        } catch(e: Exception) {
            return ""
        }
    }

    override suspend fun getAvatarImageUrl(path: String): String =
        avatarImageRef.child(path).downloadUrl.await().toString()

    override suspend fun getPetImageUrl(path: String): String =
        petImageRef.child(path).downloadUrl.await().toString()

    override suspend fun saveAvatarImage(uri: Uri): Uri {
        return avatarImageRef.child(authService.currentUser.first().id)
            .putFile(uri)
            .await()
            .storage
            .downloadUrl
            .await()
    }

    override suspend fun savePetImage(petId: String, uri: Uri) {
        petImageRef.child(petId)
            .putFile(uri)
            .await()
    }
}
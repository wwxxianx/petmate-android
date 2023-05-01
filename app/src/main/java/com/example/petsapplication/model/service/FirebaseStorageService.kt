package com.example.petsapplication.model.service

import android.net.Uri
import com.google.firebase.storage.StorageReference

interface FirebaseStorageService {
    var avatarImageRef: StorageReference
    var petImageRef: StorageReference

    suspend fun getCurrentUserImageUrl(): String
    suspend fun getAvatarImageUrl(path: String): String
    suspend fun getPetImageUrl(path: String): String
    suspend fun saveAvatarImage(uri: Uri): Uri
    suspend fun savePetImage(petId: String, uri: Uri)
}
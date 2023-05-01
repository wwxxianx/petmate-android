package com.example.petsapplication.model.service

import android.net.Uri
import com.example.petsapplication.model.Message
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.Response
import com.example.petsapplication.model.User
import kotlinx.coroutines.flow.Flow

interface FirestoreService {
    suspend fun createUser()

    suspend fun getPets(): Response<List<Pet>>

    suspend fun getPet(petId: String): Pet

    suspend fun getFavouritePetList(): Response<List<Pet>>

    suspend fun addPetToFavourite(pet: Pet)

    suspend fun removePetFromFavourite(petId: String)

    suspend fun getUser(uid: String): User

    suspend fun getCurrentUser(): User

    suspend fun saveUser(user: User)

    suspend fun savePet(pet: Pet, petImageUri: Uri)

    suspend fun getCurrentUserChannel(): Response<List<User>>

    suspend fun getMessages(chatUserId: String): Response<List<Message>>

    suspend fun saveMessage(chatUserId: String, messages: Any)

    suspend fun saveTestingData(data: Any)
}
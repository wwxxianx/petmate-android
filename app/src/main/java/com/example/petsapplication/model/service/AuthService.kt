package com.example.petsapplication.model.service

import com.example.petsapplication.model.User
import kotlinx.coroutines.flow.Flow

interface AuthService {
    var currentUserId: String
    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)

    fun signOut()

    suspend fun createUser(email: String, password: String)

    suspend fun anonymousSignIn()

    suspend fun resetPassword(email: String)
}
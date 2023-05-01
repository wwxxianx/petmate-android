package com.example.petsapplication.model.service.impl

import com.example.petsapplication.model.User
import com.example.petsapplication.model.service.AuthService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthService {
    override var currentUserId = auth.currentUser?.uid.orEmpty()

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override fun signOut() {
        auth.signOut()
    }

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let
                    { user ->
                        User(id = user.uid, anonymous = user.isAnonymous)
                    } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun createUser(email: String, password: String): Unit {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun anonymousSignIn() {
        auth.signInAnonymously().await()
    }

    override suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}
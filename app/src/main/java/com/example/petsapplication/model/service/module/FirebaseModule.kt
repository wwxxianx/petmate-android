package com.example.petsapplication.model.service.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    // Provides Firebase Auth instance for dependency injection
    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth

    // Provides Firebase Auth instance for dependency injection
    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    // Provides Firebase Auth instance for dependency injection
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage
}
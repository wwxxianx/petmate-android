package com.example.petsapplication.model.service.module

import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirebaseStorageService
import com.example.petsapplication.model.service.FirestoreService
import com.example.petsapplication.model.service.impl.AuthServiceImpl
import com.example.petsapplication.model.service.impl.FirebaseStorageServiceImpl
import com.example.petsapplication.model.service.impl.FirestoreServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAuthService(impl: AuthServiceImpl): AuthService

    @Binds
    abstract fun provideFirestoreService(impl: FirestoreServiceImpl): FirestoreService

    @Binds
    abstract fun provideFirebaseStorageService(impl: FirebaseStorageServiceImpl): FirebaseStorageService
}
package com.example.petsapplication.model

data class User(
    val id: String = "",
    val name: String = "",
    val age: Int = 18,
    val imageUrl: String = "",
    val anonymous: Boolean = true
)

package com.example.petsapplication.common.extension

import android.util.Patterns

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() && this.length >= 6
}

fun String.isValidName(): Boolean {
    return this.isNotBlank() && this.length >= 3
}

fun String.isValidDescription(): Boolean {
    return this.isNotBlank() && this.length >= 10
}

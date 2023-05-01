package com.example.petsapplication.presentation.screens.chat

import com.example.petsapplication.model.Message
import com.example.petsapplication.model.User

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val currentUser: User = User(),
    val chatUser: User = User(),
    val messageText: String = "",
    val isLoading: Boolean = true,
)
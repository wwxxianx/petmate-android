package com.example.petsapplication.presentation.screens.channel

import com.example.petsapplication.model.User


data class ChannelUiState(
    val currentUser: User = User(),
    val channelUserList: List<User> = emptyList(),
    val isLoading: Boolean = true,
    val isFailed: Boolean = false
)
package com.example.petsapplication.presentation.screens.chat

import androidx.compose.runtime.mutableStateOf
import com.example.petsapplication.PetAppViewModel
import com.example.petsapplication.R
import com.example.petsapplication.common.snackbar.SnackbarManager
import com.example.petsapplication.common.snackbar.SnackbarMessage
import com.example.petsapplication.model.Message
import com.example.petsapplication.model.Response
import com.example.petsapplication.model.service.AuthService
import com.example.petsapplication.model.service.FirebaseStorageService
import com.example.petsapplication.model.service.FirestoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val firebaseStorageService: FirebaseStorageService
) : PetAppViewModel() {
    var uiState = mutableStateOf(ChatUiState())
        private set

    private val messageText
        get() = uiState.value.messageText

    private val currentUser
        get() = uiState.value.currentUser

    private val chatUser
        get() = uiState.value.chatUser

    private val messages
        get() = uiState.value.messages

    suspend fun initialize(chatUserId: String) {
        uiState.value = uiState.value.copy(currentUser = firestoreService.getCurrentUser())
        uiState.value = uiState.value.copy(chatUser = firestoreService.getUser(chatUserId))
        when (val messages = firestoreService.getMessages(chatUserId)) {
            is Response.Success -> {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    messages = messages.data ?: emptyList()
                )
            }
            is Response.Failure -> {
                SnackbarManager.showMessage(SnackbarMessage.StringSnackbar(messages.message!!))
            }
            is Response.Loading -> {
                uiState.value = uiState.value.copy(isLoading = true)
            }
        }
    }

    fun sentByCurrentUser(userId: String) =
        userId == uiState.value.currentUser.id

    fun onMessageTextChange(text: String) {
        uiState.value = uiState.value.copy(messageText = text)
    }

    private fun addNewMessage() {
        val newMessage = Message(
            message = messageText,
            senderId = currentUser.id,
            timestamp = System.currentTimeMillis()
        )
        val newMessageList = messages.toMutableList()
        newMessageList.add(newMessage)
        uiState.value = uiState.value.copy(
            messages = newMessageList,
            messageText = ""
        )
    }

    suspend fun sendMessage() {
        if (messageText.isBlank()) {
            SnackbarManager.showMessage(R.string.chat_message_error)
            return
        }
        launchCatching {
            addNewMessage()
            // Create new message to save to firestore
            val messagesMap = messages.map {
                mapOf(
                    "message" to it.message,
                    "senderId" to it.senderId,
                    "timestamp" to it.timestamp
                )
            }
            val data = hashMapOf(
                "messages" to messagesMap
            )
            firestoreService.saveMessage(chatUserId = chatUser.id, messages = data)
        }
    }
}
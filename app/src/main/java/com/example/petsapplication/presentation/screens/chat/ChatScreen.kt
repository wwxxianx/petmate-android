package com.example.petsapplication.presentation.screens.chat

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.Avatar
import com.example.petsapplication.common.composable.PetTopAppBar
import com.example.petsapplication.common.composable.PrimaryButton
import com.example.petsapplication.common.composable.PrimaryIconButton
import com.example.petsapplication.model.Message
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    chatUserId: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.value
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) { viewModel.initialize(chatUserId) }

    Scaffold(
        topBar = { PetTopAppBar(navigateBack = navigateBack, label = R.string.chat) },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            if (uiState.isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp)
                        .fillMaxSize()
                ) {
                    items(uiState.messages) { message ->
                        if (viewModel.sentByCurrentUser(message.senderId)) {
                            CurrentUserChatDialog(
                                message = message,
                                imageUri = uiState.currentUser.imageUrl.toUri()
                            )
                        } else {
                            FriendChatDialog(
                                message = message,
                                imageUri = uiState.chatUser.imageUrl.toUri()
                            )
                        }
                    }
                }
            }

            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 26.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    MessageTextField(
                        value = uiState.messageText,
                        onMessageTextChange = viewModel::onMessageTextChange,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                coroutineScope.launch {
                                    viewModel.sendMessage()
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryIconButton(
                        icon = R.drawable.send_icon,
                        height = 50.dp,
                        width = 46.dp
                    ) {
                        // Send message action
                        coroutineScope.launch {
                            viewModel.sendMessage()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageTextField(
    value: String,
    onMessageTextChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions {  },
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onMessageTextChange,
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.White
        ),
        placeholder = { Text("Type your message...") },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            .height(50.dp)
    )
}

@Composable
fun CurrentUserChatDialog(
    message: Message,
    imageUri: Uri,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .padding(bottom = 30.dp)
            .fillMaxWidth()
    ) {
        Surface(
            color = Color(0xFF4469AE),
            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomStart = 10.dp,
            ),
            elevation = 8.dp,
            modifier = Modifier
                .width(170.dp)
                .padding(end = 10.dp)
        ) {
            Text(
                text = "${message.message}",
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 16.dp)
            )
        }

        Avatar(
            imageUri = imageUri,
            shadowBorderRadius = 100.dp,
            shadowPaddingX = 0.dp,
            shadowPaddingY = 0.dp,
            size = 50.dp,
            modifier = Modifier.offset(y = 25.dp)
        )
    }
}

@Composable
fun FriendChatDialog(
    message: Message,
    imageUri: Uri,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .padding(bottom = 30.dp)
            .fillMaxWidth()
    ) {
        Avatar(
            imageUri = imageUri,
            shadowBorderRadius = 100.dp,
            shadowPaddingX = 0.dp,
            shadowPaddingY = 0.dp,
            size = 50.dp,
            modifier = Modifier.offset(y = 25.dp)
        )

        Surface(
            color = Color(0xFFFD7A7A),
            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomEnd = 10.dp,
            ),
            elevation = 8.dp,
            modifier = Modifier
                .width(170.dp)
                .padding(start = 10.dp)
        ) {
            Text(
                text = "${message.message}",
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
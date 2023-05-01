package com.example.petsapplication.presentation.screens.channel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.AnonymousScreen
import com.example.petsapplication.common.composable.Avatar
import com.example.petsapplication.common.composable.PetTopAppBar
import com.example.petsapplication.common.extension.shadow
import com.example.petsapplication.model.User
import com.example.petsapplication.presentation.screens.home.HomeSections

@Composable
fun ChannelScreen(
    navigateBack: () -> Unit,
    navigate: (String) -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChannelViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { PetTopAppBar(navigateBack = navigateBack, label = R.string.messages)},
        modifier = modifier
    ) { innerPadding ->
        val uiState = viewModel.uiState.value
        if (uiState.currentUser.anonymous) {
            AnonymousScreen(
                navigateAndPopUp = navigateAndPopUp,
                popUpFromRoute = HomeSections.CHANNEL.route
            )
        } else {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.isFailed) {
                // Handling error
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(26.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(26.dp)
                ) {
                    items(uiState.channelUserList) { user ->
                        ChannelCard(
                            chatUser = user,
                            modifier = Modifier
                                .clickable { viewModel.onNavigateToChat(user, navigate) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChannelCard(
    chatUser: User,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                borderRadius = 8.dp,
                offsetX = 3.dp,
                offsetY = 3.dp
            )
            .padding(end = 4.dp)
            .padding(bottom = 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 26.dp, vertical = 20.dp)
        ) {
            Avatar(
                shadowBorderRadius = 100.dp,
                shadowPaddingX = 0.dp,
                shadowPaddingY = 0.dp,
                imageUri = chatUser.imageUrl.toUri(),
            )
            Text(
                text = "${chatUser.name}",
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
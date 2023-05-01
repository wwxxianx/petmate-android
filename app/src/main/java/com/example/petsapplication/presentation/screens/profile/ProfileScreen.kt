package com.example.petsapplication.presentation.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.*
import com.example.petsapplication.common.extension.shadow
import com.example.petsapplication.model.User
import com.example.petsapplication.presentation.screens.home.HomeSections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navigateBack: () -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
    navigate: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            PetTopAppBar(
                navigateBack = navigateBack,
                label = R.string.profile
            )
        },
        modifier = modifier.fillMaxSize()
    ) {innerPadding ->
        val uiState = viewModel.uiState.value
        if (uiState.user.anonymous) {
            // Prompt user to sign in
            AnonymousScreen(
                navigateAndPopUp = navigateAndPopUp,
                popUpFromRoute = HomeSections.PROFILE.route
            )
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    if (uiState.showingSignOutDialog) {
                        SignOutDialog(
                            navigateAndPopUp = navigateAndPopUp,
                            onCloseDialog = viewModel::onCloseDialog,
                            onSignOut = viewModel::onSignOut
                        )
                    }
                    Avatar(
                        imageUri = uiState.user.imageUrl.toUri(),
                        shape = RoundedCornerShape(23.dp),
                        size = 137.dp,
                        shadowOffsetX = 4.dp,
                        shadowOffsetY = 4.dp,
                        shadowBorderRadius = 23.dp
                    )

                    SecondaryButton(
                        label = R.string.sign_out,
                        modifier = Modifier.width(137.dp)
                    ) {
                        // Sign out action
                        viewModel.onOpenDialog()
                    }

                    UserDetails(user = uiState.user)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        PrimaryButton(
                            label = R.string.post_a_pet,
                            modifier = Modifier.width(200.dp)
                        ) {
                            // Pet posting action
                            viewModel.onNavigateToPetPosting(navigate)
                        }

                        SecondaryIconButton(icon = R.drawable.edit) {
                            // Navigate to EditProfile action
                            viewModel.onNavigateToEditProfile(navigate)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SignOutDialog(
    navigateAndPopUp: (String, String) -> Unit,
    onCloseDialog: () -> Unit,
    onSignOut: ((String, String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCloseDialog,
        title = {
            Text(text = "Sign out")
        },
        text = {
            Text(text = "Do you want to sign out your account?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSignOut(navigateAndPopUp)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCloseDialog,
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun UserDetails(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "${user.name}",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .width(123.dp)
                    .height(86.dp)
                    .shadow(
                        offsetX = 2.dp,
                        offsetY = 2.dp,
                        borderRadius = 8.dp
                    )
                    .padding(end = 3.dp)
                    .padding(bottom = 3.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${user.age}",
                        style = MaterialTheme.typography.h3
                    )
                    Text(
                        text = "Age",
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .width(123.dp)
                    .height(86.dp)
                    .shadow(
                        offsetX = 2.dp,
                        offsetY = 2.dp,
                        borderRadius = 8.dp
                    )
                    .padding(end = 3.dp)
                    .padding(bottom = 3.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.malaysia_flag),
                        contentDescription = null,
                        modifier = Modifier
                            .width(56.dp)
                            .height(30.dp)
                    )
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}
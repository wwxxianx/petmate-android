package com.example.petsapplication.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.*

//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun LoginScreen(
//    navigateToSignUp: (String) -> Unit,
//    navigateToHome: (String, String) -> Unit,
//    viewModel: LoginViewModel = hiltViewModel()
//) {
//    val context = LocalContext.current
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    val locationPermissionsState = rememberPermissionState(
//        android.Manifest.permission.ACCESS_FINE_LOCATION
//    )
//
//    if (locationPermissionsState.status.isGranted) {
//        Text("Thanks! I can access your exact location :D")
//        PrimaryButton(label = R.string.print) {
//            viewModel.getUserLocation(context)
//            println("print")
//        }
//    } else {
//        Column {
//            val textToShow = if (locationPermissionsState.status.shouldShowRationale) {
//                R.string.location_permission_message
//            } else {
//                // First time the user sees this feature or the user doesn't want to be asked again
//                R.string.first_time_location_permission
//            }
//
//            Text(text = stringResource(id = textToShow))
//            Spacer(modifier = Modifier.height(8.dp))
//            PrimaryButton(label = R.string.request_permission) {
//                locationPermissionsState.launchPermissionRequest()
//            }
//        }
//    }
//}

@Composable
fun LoginScreen(
    navigate: (String) -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.value
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(top = 50.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Reset Password Dialog
        if(uiState.showingDialog) {
            ResetPasswordDialog(
                email = uiState.dialogEmail,
                onEmailChange = viewModel::onDialogEmailChange,
                onCloseDialog = viewModel::onCloseDialog,
                onResetPassword = viewModel::onResetPassword
            )
        }

        Column {
            Text(text = "Login", style = MaterialTheme.typography.h1)
            Text(
                text = "Enter your account details, and let’s find your favourite pets!",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .wrapContentWidth()
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            EmailField(
                value = uiState.email,
                onValueChanged = viewModel::onEmailChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            PasswordField(
                value = uiState.password,
                onValueChanged = viewModel::onPasswordChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            RepeatPasswordField(
                value = uiState.repeatPassword,
                onValueChanged = viewModel::onRepeatPasswordChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.onSignInClick(navigateAndPopUp)
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        HighlightedTextButton(
            label = R.string.forgot_password,
            width = 130.dp,
            modifier = Modifier
                .align(Alignment.End)
        ) {
            // Forgot Password Action
            viewModel.onOpenDialog()
        }

        PrimaryButton(
            label = R.string.sign_in,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Sign in Action
            viewModel.onSignInClick(navigateAndPopUp)
        }

        Row() {
            Text(
                text = stringResource(R.string.dont_have_account),
                style = MaterialTheme.typography.caption,
            )
            HighlightedTextButton(
                label = R.string.sign_up,
                width = 60.dp
            ) {
                // Navigate to Sign up Action
                viewModel.onNavigateToSignUpClick(navigate)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        SecondaryButton(
            label = R.string.continue_as_guest,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Anonymous Login Action
            viewModel.onAnonymousSignInClick(navigateAndPopUp)
        }
    }
}

@Composable
fun ResetPasswordDialog(
    email: String,
    onEmailChange: (String) -> Unit,
    onCloseDialog: () -> Unit,
    onResetPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCloseDialog,
        title = {
            Text(text = "You'll receive a link in your email to reset password")
        },
        text = {
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(id = R.string.enter_your_email))}
            )
        },
        confirmButton = {
            TextButton(
                onClick = onResetPassword
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCloseDialog,
            ) {
                Text("Cancel")
            }
        }
    )
}






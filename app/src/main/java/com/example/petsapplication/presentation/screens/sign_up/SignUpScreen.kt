package com.example.petsapplication.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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

@Composable
fun SignUpScreen(
    navigateBack: () -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
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
        Column {
            Text(text = stringResource(R.string.register), style = MaterialTheme.typography.h1)
            Text(
                text = stringResource(R.string.register_caption),
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
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.signUp(navigateBack)
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        PrimaryButton(
            label = R.string.sign_up,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Sign up action
            viewModel.signUp(navigateBack)
        }

        Row() {
            Text(
                text = stringResource(R.string.already_have_account),
                style = MaterialTheme.typography.caption,
            )
            HighlightedTextButton(
                label = R.string.sign_in,
                width = 60.dp
            ) {
                // Navigate back to Sign in Action
                viewModel.navigateBackToSignIn(navigateBack)
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






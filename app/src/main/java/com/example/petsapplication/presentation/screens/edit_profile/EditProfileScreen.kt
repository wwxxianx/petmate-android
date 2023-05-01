package com.example.petsapplication.presentation.screens.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    navigateBack: () -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            PetTopAppBar(
                navigateBack = navigateBack,
                label = R.string.edit_profile
            )
        },
        modifier = modifier.fillMaxSize()
    ) {innerPadding ->
        val uiState = viewModel.uiState.value
        val photoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> viewModel.onImageChange(uri)}
        )
        val coroutineScope = rememberCoroutineScope()
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 36.dp)
        ) {
            Avatar(
                imageUri = uiState.selectedImageUri ?: uiState.currentUser.imageUrl.toUri(),
                shape = RoundedCornerShape(23.dp),
                size = 157.dp,
                shadowOffsetX = 4.dp,
                shadowOffsetY = 4.dp,
                shadowBorderRadius = 23.dp
            )
            
            SecondaryButton(
                label = R.string.upload_image,
                modifier = Modifier.width(157.dp)) {
                // Image picker action
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            NormalTextField(
                value = uiState.name,
                onValueChanged = viewModel::onNameChange,
                placeholder = uiState.currentUser.name,
                label = R.string.your_name,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            NormalTextField(
                value = uiState.age,
                onValueChanged = viewModel::onAgeChange,
                placeholder = uiState.currentUser.age.toString(),
                label = R.string.your_age,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(36.dp))

            PrimaryButton(
                label = R.string.done,
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.onSaveUserProfile(navigateAndPopUp)
            }
        }
    }
}
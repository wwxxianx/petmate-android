package com.example.petsapplication.presentation.screens.pet_posting

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.NormalTextField
import com.example.petsapplication.common.composable.PetTopAppBar
import com.example.petsapplication.common.composable.PrimaryButton
import com.example.petsapplication.common.composable.SecondaryButton
import com.example.petsapplication.common.extension.shadow
import com.example.petsapplication.model.PetType
import com.example.petsapplication.model.Sex
import kotlinx.coroutines.launch

@Composable
fun PetPostingScreen(
    navigateBack: () -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PetPostingViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { PetTopAppBar(navigateBack = navigateBack, label = R.string.posting)},
        modifier = modifier.fillMaxSize()
    ) {innerPadding ->
        val uiState = viewModel.uiState.value
        val photoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> viewModel.onImageChange(uri)}
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 36.dp)
        ) {
            if (uiState.selectedImageUri != null) {
                AsyncImage(
                    model = uiState.selectedImageUri,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Pet's photo",
                    modifier = Modifier
                        .width(195.dp)
                        .height(183.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.image_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .width(195.dp)
                        .height(183.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                )
            }
            SecondaryButton(
                label = R.string.upload_image,
                modifier = Modifier.width(195.dp)
            ) {
                // Upload image action
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                NormalTextField(
                    value = uiState.name,
                    onValueChanged = viewModel::onNameChange,
                    label = R.string.name,
                    modifier = Modifier.weight(1f)
                )
                
                NormalTextField(
                    value = uiState.age,
                    onValueChanged = viewModel::onAgeChange,
                    label = R.string.age,
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                NormalTextField(
                    value = uiState.breed,
                    onValueChanged = viewModel::onBreedChange,
                    label = R.string.breed,
                    modifier = Modifier.weight(1f)
                )
                
                PetTypeDropdownMenu(
                    selectedPetType = uiState.petType,
                    showingPetTypeDropdown = uiState.showingPetTypeDropdown,
                    onClosePetTypeDropdown = viewModel::onClosePetTypeDropdown,
                    onOpenPetTypeDropdown = viewModel::onOpenPetTypeDropdown,
                    onPetTypeChange = viewModel::onPetTypeChange,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PetSexDropdownMenu(
                    selectedSex = uiState.sex,
                    showingSexDropdown = uiState.showingSexDropdown,
                    onCloseSexDropdown = viewModel::onCloseSexDropdown,
                    onOpenSexDropdown = viewModel::onOpenSexDropdown,
                    onSexChange = viewModel::onSexChange,
                    modifier = Modifier.weight(1f)
                )

                NormalTextField(
                    value = uiState.weight,
                    onValueChanged = viewModel::onWeightChange,
                    label = R.string.weight,
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
            }

            NormalTextField(
                value = uiState.description,
                onValueChanged = viewModel::onDescriptionChange,
                label = R.string.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            PrimaryButton(
                label = R.string.done,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Save pet action
                viewModel.savePet(navigateAndPopUp)
            }
        }
    }
}

@Composable
fun PetTypeDropdownMenu(
    selectedPetType: PetType,
    showingPetTypeDropdown: Boolean,
    onClosePetTypeDropdown: () -> Unit,
    onOpenPetTypeDropdown: () -> Unit,
    onPetTypeChange: (PetType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Surface(
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .shadow(
                    offsetX = 2.dp,
                    offsetY = 2.dp,
                    borderRadius = 6.dp
                )
                .fillMaxWidth()
                .height(62.dp)
                .padding(end = 4.dp)
                .padding(bottom = 6.dp)
                .clickable { onOpenPetTypeDropdown() }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 16.dp)
            ){
                Text(
                    text = "${selectedPetType.name}",
                    style = MaterialTheme.typography.caption,
                    color = Color.DarkGray
                )
            }
        }
        DropdownMenu(
            expanded = showingPetTypeDropdown,
            onDismissRequest = onClosePetTypeDropdown
        ) {
            PetType.values().forEach { petType ->
                DropdownMenuItem(
                    text = { Text("${petType.name}") },
                    onClick = { onPetTypeChange(petType) }
                )
            }
        }
    }
}

@Composable
fun PetSexDropdownMenu(
    selectedSex: Sex,
    showingSexDropdown: Boolean,
    onCloseSexDropdown: () -> Unit,
    onOpenSexDropdown: () -> Unit,
    onSexChange: (Sex) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Surface(
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .shadow(
                    offsetX = 2.dp,
                    offsetY = 2.dp,
                    borderRadius = 6.dp
                )
                .fillMaxWidth()
                .height(62.dp)
                .padding(end = 4.dp)
                .padding(bottom = 6.dp)
                .clickable { onOpenSexDropdown() }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 16.dp)
            ){
                Text(
                    text = "${selectedSex.name}",
                    style = MaterialTheme.typography.caption,
                    color = Color.DarkGray
                )
            }
        }
        DropdownMenu(
            expanded = showingSexDropdown,
            onDismissRequest = onCloseSexDropdown
        ) {
            Sex.values().forEach { sex ->
                DropdownMenuItem(
                    text = { Text("${sex.name}") },
                    onClick = { onSexChange(sex) }
                )
            }
        }
    }
}

@Preview
@Composable
fun PetPostingPreview() {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.TopStart)) {
        Surface(
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .width(120.dp)
                .height(60.dp)
                .shadow(
                    offsetX = 4.dp,
                    offsetY = 4.dp,
                    borderRadius = 6.dp
                )
                .padding(end = 4.dp)
                .padding(bottom = 3.dp)
                .clickable { expanded = true }
        ) {
            Text("Cat")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            PetType.values().forEach { petType ->
                DropdownMenuItem(
                    text = { Text("${petType.name}") },
                    onClick = { /* Handle edit! */ }
                )
            }
        }
    }
}
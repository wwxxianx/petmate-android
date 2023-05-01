package com.example.petsapplication.presentation.screens.pet_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.petsapplication.R
import com.example.petsapplication.common.composable.*
import com.example.petsapplication.common.extension.shadow
import com.example.petsapplication.common.getRandomColour
import com.example.petsapplication.model.Pet
import com.example.petsapplication.model.User

@Composable
fun PetDetailScreen(
    petId: String,
    navigate: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PetDetailViewModel = hiltViewModel()
) {
    var pet by viewModel.pet
    var imageUrl by viewModel.imageUrl
    var owner by viewModel.owner
    LaunchedEffect(Unit) { viewModel.initialize(petId) }
    Scaffold(
        topBar = {
            PetTopAppBar(
                navigateBack = navigateBack,
                label = R.string.pet_details
            )
        },
        bottomBar = {
            PrimaryButton(
                label = R.string.adopt_now,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp)
                    .padding(bottom = 10.dp)
            ) {
                // Adopt action
                viewModel.onAdoptPet(navigate)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(bottom = 76.dp)
        ) {
            item {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .padding(end = 25.dp)
                    ) {
                        LoveButton(
                            size = 36.dp,
                        ) {
                            // Add to favourite action
                            viewModel.onAddPetToFavourite()
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .shadow(
                                    offsetX = 4.dp,
                                    offsetY = 4.dp,
                                    borderRadius = 24.dp,
                                )
                                .padding(end = 4.dp)
                                .padding(bottom = 4.dp)
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(260.dp)
                                    .background(getRandomColour(), RoundedCornerShape(24.dp))
                                    .clip(RoundedCornerShape(24.dp))
                                    .border(
                                        BorderStroke(1.dp, Color.Black),
                                        RoundedCornerShape(24.dp)
                                    )
                                    .padding(16.dp)
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 34.dp)
                    ) {
                        Text(
                            text = "${pet.name}",
                            style = MaterialTheme.typography.h2
                        )
                        Text(
                            text = "Breed: ${pet.breed}",
                            style = MaterialTheme.typography.h5
                        )
                        PetDetailCardList(pet = pet)
                    }
                    
                    Spacer(modifier = Modifier.height(26.dp))

                    OwnerDetail(
                        owner = owner,
                        modifier = Modifier
                            .padding(horizontal = 36.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${pet.description}",
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 36.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OwnerDetail(
    owner: User,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Avatar(
            imageUri = owner.imageUrl.toUri(),
            shadowBorderRadius = 100.dp
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${owner.name}",
                style = MaterialTheme.typography.caption
            )
            Text(
                text = "@PetsLover",
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun PetDetailCardList(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        PetDetailCard(
            detailString = pet.sex,
            detailLabel = "Sex",
            modifier = Modifier.weight(1f)
        )
        PetDetailCard(
            detailString = pet.age.toString(),
            detailLabel = "Age",
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        PetDetailCard(
            detailString = "${pet.weight.toString()}kg",
            detailLabel = "Weight",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PetDetailCard(
    detailString: String,
    detailLabel: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
            .height(63.dp)
            .shadow(
                borderRadius = 4.dp,
                offsetX = 4.dp,
                offsetY = 2.dp,
                spread = 0.dp
            )
            .padding(bottom = 4.dp)
            .padding(end = 4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "$detailString",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "$detailLabel",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
        }
    }
}
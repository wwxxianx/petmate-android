package com.example.petsapplication.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.petsapplication.R

@Composable
fun PetTopAppBar(
    navigateBack: () -> Unit,
    @StringRes label: Int,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = label),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 56.dp)
            )
        },
        navigationIcon = {
            PrimaryIconButton(icon = R.drawable.arrow_back) {
                // Navigate Back
                navigateBack()
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        modifier = modifier.padding(horizontal = 16.dp)
    )
}
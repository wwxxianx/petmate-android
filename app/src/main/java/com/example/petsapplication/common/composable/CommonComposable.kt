package com.example.petsapplication.common.composable

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.petsapplication.R
import com.example.petsapplication.ScreenRoutes
import com.example.petsapplication.common.extension.shadow

@Composable
fun Avatar(
    shadowOffsetX: Dp = 0.dp,
    shadowOffsetY: Dp = 0.dp,
    shadowPaddingX: Dp = 4.dp,
    shadowPaddingY: Dp = 4.dp,
    shadowBorderRadius: Dp = 0.dp,
    size: Dp = 60.dp,
    shape: Shape = CircleShape,
    imageUri: Uri? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
            .shadow(
                offsetX = shadowOffsetX,
                offsetY = shadowOffsetY,
                borderRadius = shadowBorderRadius,
            )
            .size(size)
            .padding(end = shadowPaddingX)
            .padding(bottom = shadowPaddingY)
            .clip(shape)
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .clip(shape)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.avatar_1),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier
                    .clip(shape)
            )
        }
    }
}

@Composable
fun AnonymousScreen(
    navigateAndPopUp: (String, String) -> Unit,
    popUpFromRoute: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 36.dp)
    ) {
        Text(
            text = stringResource(id = R.string.please_sign_in),
            style = MaterialTheme.typography.h3
        )
        PrimaryButton(
            label = R.string.sign_in_now,
            modifier = Modifier.fillMaxWidth()
        ) {
            navigateAndPopUp(ScreenRoutes.LOGIN_ROUTE, popUpFromRoute)
        }
    }
}
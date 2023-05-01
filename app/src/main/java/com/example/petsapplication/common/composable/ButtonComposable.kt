package com.example.petsapplication.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.R
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.petsapplication.common.extension.shadow

@Composable
fun PrimaryButton(
    height: Dp = 50.dp,
    textStyle: TextStyle = MaterialTheme.typography.button,
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFCEFF1A), 
            contentColor = Color.Black),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
            .shadow(
                offsetX = 2.dp,
                offsetY = 2.dp,
                borderRadius = 8.dp,
            )
            .padding(end = 3.dp)
            .height(height)
            .padding(bottom = 5.dp)
    ) {
        Text(
            text = stringResource(id = label),
            style = textStyle
        )
    }
}

@Composable
fun PrimaryIconButton(
    height: Dp = 46.dp,
    width: Dp = 44.dp,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFCEFF1A),
            contentColor = Color.Black),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .shadow(
                offsetX = 2.dp,
                offsetY = 2.dp,
                borderRadius = 6.dp,
            )
            .padding(end = 2.dp)
            .width(width)
            .height(height)
            .padding(bottom = 4.dp)
    ) {
        Image(
            contentScale = ContentScale.Fit,
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(66.dp)
                .offset(x = 3.dp)
        )
    }
}

@Composable
fun SecondaryIconButton(
    width: Dp = 48.dp,
    height: Dp = 49.dp,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = Color(0xFFCEFF1A)),
        border = BorderStroke(1.dp, Color(0xFFCEFF1A)),
        modifier = modifier
            .shadow(
                offsetX = 2.dp,
                offsetY = 2.dp,
                borderRadius = 6.dp,
            )
            .padding(end = 2.dp)
            .width(width)
            .height(height)
            .padding(bottom = 4.dp)
    ) {
        Image(
            contentScale = ContentScale.Fit,
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(66.dp)
                .offset(x = 3.dp)
        )
    }
}

@Composable
fun SecondaryButton(@StringRes label: Int, modifier: Modifier, action: () -> Unit) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = Color(0xFFCEFF1A)
        ),
        border = BorderStroke(1.dp, Color(0xFFCEFF1A)),
        modifier = modifier
            .shadow(
                offsetX = 2.dp,
                offsetY = 2.dp,
                borderRadius = 6.dp,
            )
            .padding(end = 4.dp)
            .height(50.dp)
            .padding(bottom = 6.dp)
    ) {
        Text(text = stringResource(id = label))
    }
}

@Composable
fun LoveButton(
    size: Dp = 24.dp,
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    FilledIconButton(
        shape = RoundedCornerShape(4.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color(0xFFFFB2E9),
            contentColor = Color(0xFFEB07AC)
        ),
        modifier = modifier
            .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(4.dp))
            .padding(0.dp)
            .size(size),
        onClick = action
    ) {
        Icon(
            Icons.Rounded.Favorite,
            contentDescription = stringResource(com.example.petsapplication.R.string.add_to_favourite),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun HighlightedTextButton(
    @StringRes label: Int,
    width: Dp = 80.dp,
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { action() }
            .background(MaterialTheme.colors.background)
            .width(width)
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFFCEFF1A))
                .align(Alignment.BottomCenter)
        )
        Text(
            text = stringResource(id = label),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        )
    }
}
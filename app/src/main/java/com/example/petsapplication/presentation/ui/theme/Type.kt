package com.example.petsapplication.presentation.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.petsapplication.R

val RocGrotesk = FontFamily(
    Font(R.font.roc_grotesk_regular, FontWeight.Normal),
    Font(R.font.roc_grotesk_medium, FontWeight.Medium),
    Font(R.font.roc_grotesk_bold, FontWeight.Bold),
    Font(R.font.roc_grotesk_thin, FontWeight.Thin),
    Font(R.font.roc_grotesk_light, FontWeight.Light)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp
    ),
    h2 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    h3 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    body1 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    body2 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    h4 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    h5 = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    caption = TextStyle(
        fontFamily = RocGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
)
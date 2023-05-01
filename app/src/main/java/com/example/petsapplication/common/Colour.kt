package com.example.petsapplication.common

import androidx.compose.ui.graphics.Color

fun getRandomColour(): Color{
    val colourList = listOf(
        Color(0xFFC6F2F0),
        Color(0xFFCEF6BB),
        Color(0xFFFACCC2),
        Color(0xFFFFFDCF),
        Color(0xFFCBD3FD),
    )
    return colourList.random()
}
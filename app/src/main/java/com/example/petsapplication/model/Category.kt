package com.example.petsapplication.model

import androidx.annotation.DrawableRes
import com.example.petsapplication.R

data class CategoryItem(
    val name: String,
    @DrawableRes val image: Int
)

val categoryItemList = listOf(
    CategoryItem(PetType.CAT.name, R.drawable.cat),
    CategoryItem(PetType.DOG.name, R.drawable.dog),
    CategoryItem(PetType.ROBIN.name, R.drawable.robin),
    CategoryItem(PetType.SQUIRREL.name, R.drawable.squirrel),
    CategoryItem(PetType.PARROT.name, R.drawable.parrot)
)
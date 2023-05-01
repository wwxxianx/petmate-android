package com.example.petsapplication.model

import com.google.firebase.firestore.DocumentId

data class Pet(
    @DocumentId val id: String = "",
    val name: String = "",
    val type: String = PetType.CAT.name,
    val breed: String = "",
    val userId: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val weight: Double = 1.0,
    val age: Int = 1,
    val sex: String = Sex.MALE.name
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$name",
            "${name.first()}",
            "$breed",
            "$type $breed",
            "$breed $name",
            "$name $breed"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

val petListExample = listOf(
    Pet(
        id = "zh4KIZl8YXKCdc7k4w6B",
        name = "Luna",
        type = PetType.CAT.name,
        breed = "Siamese",
        userId = "user1",
        description = "Loves to play with toys and cuddle.",
        imageUrl = "1000000044",
        weight = 5.0,
        age = 2,
        sex = Sex.FEMALE.name
    ),
    Pet(
        id = "zh4KIZl8YXKCdc7k4w6B",
        name = "Max",
        type = PetType.DOG.name,
        breed = "Golden Retriever",
        userId = "user2",
        description = "Friendly and loves to go for walks.",
        imageUrl = "1000000044",
        weight = 25.0,
        age = 4,
        sex = Sex.FEMALE.name
    ),
    Pet(
        id = "zh4KIZl8YXKCdc7k4w6B",
        name = "Simba",
        type = PetType.CAT.name,
        breed = "Persian",
        userId = "user1",
        description = "Loves to nap and play with string.",
        imageUrl = "1000000044",
        weight = 6.0,
        age = 3,
        sex = Sex.FEMALE.name
    ),
    Pet(
        id = "zh4KIZl8YXKCdc7k4w6B",
        name = "Rocky",
        type = PetType.DOG.name,
        breed = "German Shepherd",
        userId = "user3",
        description = "Loyal and protective.",
        imageUrl = "1000000044",
        weight = 30.0,
        age = 5,
        sex = Sex.FEMALE.name
    ),
    Pet(
        id = "zh4KIZl8YXKCdc7k4w6B",
        name = "Coco",
        type = PetType.PARROT.name,
        breed = "African Grey",
        userId = "user4",
        description = "Can mimic words and loves to sing.",
        imageUrl = "1000000044",
        weight = 1.0,
        age = 10,
        sex = Sex.FEMALE.name
    )
)

enum class Sex {
    FEMALE, MALE
}

enum class PetType{
    CAT, DOG, ROBIN, SQUIRREL, PARROT
}

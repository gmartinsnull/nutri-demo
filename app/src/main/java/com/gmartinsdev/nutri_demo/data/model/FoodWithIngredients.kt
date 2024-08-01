package com.gmartinsdev.nutri_demo.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * data class representing a food item containing its ingredients
 */
data class FoodWithIngredients(
    @Embedded val food: Food,
    @Relation(
        parentColumn = "id",
        entityColumn = "foodId"
    )
    val ingredients: List<SubRecipe>
)

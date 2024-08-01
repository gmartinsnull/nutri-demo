package com.gmartinsdev.nutri_demo.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * data class representing a food item containing its recipe ingredients
 */
data class FoodWithIngredients(
    @Embedded val food: Food,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<SubRecipe>
)

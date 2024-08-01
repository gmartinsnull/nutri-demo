package com.gmartinsdev.nutri_demo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 *  data class representing sub-recipe of food
 */
@Entity(tableName = "sub_recipes")
data class SubRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @Json(name = "recipe_id")
    val foodId: Int,
    @Json(name = "serving_weight")
    val servingWeight: Double,
    val food: String,
    @Json(name = "calories")
    val cal: Double,
    @Json(name = "serving_qty")
    val servingQty: Double,
    @Json(name = "serving_unit")
    val servingUnit: String?,
)

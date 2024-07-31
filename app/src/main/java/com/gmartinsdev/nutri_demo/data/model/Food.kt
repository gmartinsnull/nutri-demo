package com.gmartinsdev.nutri_demo.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 * data class representing food nutrients
 */
@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @Json(name = "food_name")
    val name: String,
    @Json(name = "serving_qty")
    val servingQty: Int,
    @Json(name = "serving_unit")
    val servingUnit: String,
    @Json(name = "serving_weight_grams")
    val servingWeight: Double,
    @Json(name = "nf_calories")
    val cal: Double,
    @Json(name = "nf_total_fat")
    val totalFat: Double,
    @Json(name = "nf_saturated_fat")
    val saturatedFat: Double,
    @Json(name = "nf_cholesterol")
    val cholesterol: Double,
    @Json(name = "nf_sodium")
    val sodium: Double,
    @Json(name = "nf_total_carbohydrate")
    val totalCarbs: Double,
    @Json(name = "nf_dietary_fiber")
    val fiber: Double,
    @Json(name = "nf_sugars")
    val sugars: Double,
    @Json(name = "nf_protein")
    val protein: Double,
    @Json(name = "nf_potassium")
    val potassium: Double,
    @Json(name = "nf_p")
    val phosphorus: Double,
    @Json(name = "nf_ingredient_statement")
    val info: String?,
    @Embedded
    val photo: Photo
)
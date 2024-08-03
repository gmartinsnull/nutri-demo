package com.gmartinsdev.nutri_demo.data.model

import com.squareup.moshi.Json

/**
 * data class representing food nutrients response
 */
data class FoodNutrientsResponse(
    @Json(name = "ndb_no")
    val recipeId: Int,
    @Json(name = "food_name")
    val name: String,
    @Json(name = "serving_qty")
    val servingQty: Double,
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
    val sugars: Double? = 0.0,
    @Json(name = "nf_protein")
    val protein: Double,
    @Json(name = "nf_potassium")
    val potassium: Double,
    @Json(name = "nf_p")
    val phosphorus: Double,
    val photo: Photo,
    @Json(name = "sub_recipe")
    val ingredients: List<SubRecipe>?
) {
    fun parseToFood() = Food(
        id = this.recipeId,
        name = this.name,
        servingQty = this.servingQty,
        servingUnit = this.servingUnit,
        servingWeight = this.servingWeight,
        cal = this.cal,
        totalFat = this.totalFat,
        saturatedFat = this.saturatedFat,
        cholesterol = this.cholesterol,
        sodium = this.sodium,
        totalCarbs = this.totalCarbs,
        fiber = this.fiber,
        sugars = this.sugars,
        protein = this.protein,
        potassium = this.potassium,
        phosphorus = this.phosphorus,
        photo = this.photo
    )
    fun parseToIngredientFood() = IngredientFood(
        id = this.recipeId,
        name = this.name,
        servingWeight = this.servingWeight,
        cal = this.cal,
        totalFat = this.totalFat,
        saturatedFat = this.saturatedFat,
        cholesterol = this.cholesterol,
        sodium = this.sodium,
        totalCarbs = this.totalCarbs,
        fiber = this.fiber,
        sugars = this.sugars,
        protein = this.protein,
        potassium = this.potassium,
        phosphorus = this.phosphorus
    )
}

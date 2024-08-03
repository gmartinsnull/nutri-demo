package com.gmartinsdev.nutri_demo.domain

import com.gmartinsdev.nutri_demo.data.model.FoodWithIngredients
import com.gmartinsdev.nutri_demo.data.model.IngredientFood
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * usecase class responsible for recalculating food nutrients
 */
class RecalculateNutrients {
    operator fun invoke(
        food: FoodWithIngredients,
        ingredientsAsFood: List<IngredientFood>,
        newIngredients: List<SubRecipe>
    ): Flow<FoodWithIngredients> = flow {
        // map and calculate every original nutrient per 1 gram
        val nutrientsPerGram = ingredientsAsFood.associate {
            it.name to IngredientFood(
                id = it.id,
                servingWeight = 1.0,
                name = it.name,
                cal = it.cal / it.servingWeight,
                totalFat = it.totalFat / it.servingWeight,
                saturatedFat = it.saturatedFat / it.servingWeight,
                cholesterol = it.cholesterol / it.servingWeight,
                sodium = it.sodium / it.servingWeight,
                totalCarbs = it.totalCarbs / it.servingWeight,
                fiber = it.fiber / it.servingWeight,
                sugars = it.sugars?.div(it.servingWeight) ?: 0.0,
                protein = it.protein / it.servingWeight,
                potassium = it.potassium / it.servingWeight,
                phosphorus = it.phosphorus / it.servingWeight
            )
        }
        // multiply all new ingredient amounts with all nutrients per 1g
        val newNutrientInfo = newIngredients.associate {
            it.food to IngredientFood(
                id = nutrientsPerGram[it.food]?.id ?: 0,
                servingWeight = it.servingWeight,
                name = it.food,
                cal = nutrientsPerGram[it.food]?.cal?.times(it.servingWeight) ?: 0.0,
                totalFat = nutrientsPerGram[it.food]?.totalFat?.times(it.servingWeight) ?: 0.0,
                saturatedFat = nutrientsPerGram[it.food]?.saturatedFat?.times(it.servingWeight)
                    ?: 0.0,
                cholesterol = nutrientsPerGram[it.food]?.cholesterol?.times(it.servingWeight)
                    ?: 0.0,
                sodium = nutrientsPerGram[it.food]?.sodium?.times(it.servingWeight) ?: 0.0,
                totalCarbs = nutrientsPerGram[it.food]?.totalCarbs?.times(it.servingWeight) ?: 0.0,
                fiber = nutrientsPerGram[it.food]?.fiber?.times(it.servingWeight) ?: 0.0,
                sugars = nutrientsPerGram[it.food]?.sugars?.times(it.servingWeight) ?: 0.0,
                protein = nutrientsPerGram[it.food]?.protein?.times(it.servingWeight) ?: 0.0,
                potassium = nutrientsPerGram[it.food]?.potassium?.times(it.servingWeight) ?: 0.0,
                phosphorus = nutrientsPerGram[it.food]?.phosphorus?.times(it.servingWeight) ?: 0.0,
            )
        }
        // temp object to hold final nutrient values
        var updatedNutrientsInfo = food.food.copy(
            servingWeight = 0.0,
            cal = 0.0,
            totalFat = 0.0,
            saturatedFat = 0.0,
            cholesterol = 0.0,
            sodium = 0.0,
            totalCarbs = 0.0,
            fiber = 0.0,
            sugars = 0.0,
            protein = 0.0,
            potassium = 0.0,
            phosphorus = 0.0,
        )
        // sum all newly multiplied nutrients, multiply by serving quantity and add them to the temp object
        // iterating through all ingredients with new nutrient values
        newNutrientInfo.forEach {
            updatedNutrientsInfo = updatedNutrientsInfo.copy(
                servingWeight = updatedNutrientsInfo.servingWeight + it.value.servingWeight * updatedNutrientsInfo.servingQty,
                cal = updatedNutrientsInfo.cal + it.value.cal * updatedNutrientsInfo.servingQty,
                totalFat = updatedNutrientsInfo.totalFat + it.value.totalFat * updatedNutrientsInfo.servingQty,
                saturatedFat = updatedNutrientsInfo.saturatedFat + it.value.saturatedFat * updatedNutrientsInfo.servingQty,
                cholesterol = updatedNutrientsInfo.cholesterol + it.value.cholesterol * updatedNutrientsInfo.servingQty,
                sodium = updatedNutrientsInfo.sodium + it.value.sodium * updatedNutrientsInfo.servingQty,
                totalCarbs = updatedNutrientsInfo.totalCarbs + it.value.totalCarbs * updatedNutrientsInfo.servingQty,
                fiber = updatedNutrientsInfo.fiber + it.value.fiber * updatedNutrientsInfo.servingQty,
                sugars = (updatedNutrientsInfo.sugars?.plus(it.value.sugars ?: 0.0)
                    ?: 0.0) * updatedNutrientsInfo.servingQty,
                protein = updatedNutrientsInfo.protein + it.value.protein * updatedNutrientsInfo.servingQty,
                potassium = updatedNutrientsInfo.potassium + it.value.potassium * updatedNutrientsInfo.servingQty,
                phosphorus = updatedNutrientsInfo.phosphorus + it.value.phosphorus * updatedNutrientsInfo.servingQty,
            )
        }
        emit(food.copy(food = updatedNutrientsInfo, ingredients = newIngredients))
    }
}
package com.gmartinsdev.nutri_demo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gmartinsdev.nutri_demo.data.model.CommonFood
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.FoodWithIngredients
import com.gmartinsdev.nutri_demo.data.model.IngredientFood
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import kotlinx.coroutines.flow.Flow

/**
 *  DAO representing all database operations related to foods table
 */
@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(vararg food: Food)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCommonFood(vararg commonFood: CommonFood)

    @Query("SELECT * FROM common_food WHERE name LIKE '%' || :query || '%'")
    fun getCommonFoodByName(query: String): Flow<List<CommonFood>>

    @Query("SELECT * FROM foods ORDER BY name ASC")
    fun getAll(): Flow<List<Food>>

    @Query("SELECT * FROM foods WHERE name = :query")
    fun getFoodByName(query: String): Flow<Food>

    // #### sub-recipe operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredients(ingredients: List<SubRecipe>)

    @Transaction
    @Query("SELECT * FROM foods WHERE id = :foodId")
    fun getFoodWithIngredients(foodId: Int): Flow<FoodWithIngredients>
    // ####

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredientAsFood(vararg ingredientFood: IngredientFood)

    @Query("SELECT * FROM ingredient_foods WHERE name = :ingredientName")
    fun getIngredientAsFoodByName(ingredientName: String): Flow<IngredientFood>
}
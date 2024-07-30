package com.gmartinsdev.nutri_demo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gmartinsdev.nutri_demo.data.model.Food
import kotlinx.coroutines.flow.Flow

/**
 *  DAO representing all database operations related to food items table
 */
@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg foods: Food)

    @Query("SELECT * FROM food ORDER BY name ASC")
    fun getAll(): Flow<List<Food>>

    @Query("SELECT * FROM food WHERE name LIKE '%' || :query || '%'")
    fun getFoodsByName(query: String): Flow<List<Food>>
}
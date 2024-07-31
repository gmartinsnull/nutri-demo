package com.gmartinsdev.nutri_demo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmartinsdev.nutri_demo.data.model.CommonFood
import com.gmartinsdev.nutri_demo.data.model.Food

/**
 *  class representing application database
 */
@Database([Food::class, CommonFood::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFoodDao(): FoodDao
}
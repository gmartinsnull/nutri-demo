package com.gmartinsdev.nutri_demo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 * data class representing common food
 */
@Entity(tableName = "common_food")
data class CommonFood(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @Json(name = "food_name")
    val name: String,
    @Json(name = "tag_id")
    val tagId: String,
)

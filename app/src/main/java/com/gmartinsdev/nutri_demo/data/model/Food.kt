package com.gmartinsdev.nutri_demo.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 *  data class representing local food data
 */
@Entity
data class Food(
    @PrimaryKey
    @Json(name = "nix_item_id")
    val id: String,
    @Json(name = "food_name")
    val name: String,
    @Json(name = "nf_calories")
    val cal: Int,
    @Embedded
    val photo: Photo
)
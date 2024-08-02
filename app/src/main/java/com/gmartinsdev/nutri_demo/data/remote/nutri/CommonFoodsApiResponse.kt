package com.gmartinsdev.nutri_demo.data.remote.nutri

import com.gmartinsdev.nutri_demo.data.model.CommonFood

/**
 * represents the response object from the common foods API
 */
data class CommonFoodsApiResponse(
    val common: List<CommonFood>
)

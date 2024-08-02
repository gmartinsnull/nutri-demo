package com.gmartinsdev.nutri_demo.data.remote.google_maps

/**
 * represents the response object nearby search from google maps API
 */
data class NearbySearchResult(
    val geometry: NearbySearchGeometry,
    val name: String
)

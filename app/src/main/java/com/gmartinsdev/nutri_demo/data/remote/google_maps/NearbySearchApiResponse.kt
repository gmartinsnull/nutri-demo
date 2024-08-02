package com.gmartinsdev.nutri_demo.data.remote.google_maps

/**
 * represents the response object from nearby search google maps API
 */
data class NearbySearchApiResponse(
    val results: List<NearbySearchResult>
)

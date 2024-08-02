package com.gmartinsdev.nutri_demo.ui.info

import com.gmartinsdev.nutri_demo.data.remote.google_maps.NearbySearchApiResponse

/**
 *  state class representing the current/latest UI state in google maps view within food info
 */
sealed class UiMapState {
    data object Loading : UiMapState()
    data class Loaded(val data: NearbySearchApiResponse) : UiMapState()
    data class Error(val errorMessage: String) : UiMapState()
}
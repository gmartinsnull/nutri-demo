package com.gmartinsdev.nutri_demo.ui

/**
 * class representing screen routes
 */
sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home")
    data object InfoScreen : Screen("info")
}
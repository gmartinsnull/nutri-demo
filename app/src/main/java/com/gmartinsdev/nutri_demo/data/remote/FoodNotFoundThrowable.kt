package com.gmartinsdev.nutri_demo.data.remote

/**
 * custom throwable class for successful response from empty body API call
 */
class FoodNotFoundThrowable(message: String) : Throwable(message)
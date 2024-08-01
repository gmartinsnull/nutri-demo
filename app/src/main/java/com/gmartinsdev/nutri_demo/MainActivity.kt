package com.gmartinsdev.nutri_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gmartinsdev.nutri_demo.ui.Screen
import com.gmartinsdev.nutri_demo.ui.info.FoodInfoScreen
import com.gmartinsdev.nutri_demo.ui.home.FoodHomeScreen
import com.gmartinsdev.nutri_demo.ui.home.FoodViewModel
import com.gmartinsdev.nutri_demo.ui.info.FoodInfoViewModel
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * main activity serving as the base foundation of home screen and its composable components
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
                    composable(Screen.HomeScreen.route) {
                        val vm = hiltViewModel<FoodViewModel>()
                        FoodHomeScreen(vm) {
                            navController.navigate(Screen.InfoScreen.route + "/$it")
                        }
                    }
                    composable(
                        route = Screen.InfoScreen.route + "/{foodId}",
                        arguments = listOf(
                            navArgument("foodId") {
                                type = NavType.IntType
                                defaultValue = 0
                                nullable = false
                            }
                        )
                    ) { entry ->
                        val vm = hiltViewModel<FoodInfoViewModel>()
                        FoodInfoScreen(entry.arguments?.getInt("foodId") ?: 0, vm) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainTheme {
        Greeting("Android")
    }
}
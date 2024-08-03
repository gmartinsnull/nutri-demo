package com.gmartinsdev.nutri_demo.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme
import kotlin.math.round

/**
 * composable responsible for displaying nutritional facts of selected food
 */
@Composable
fun FoodInfoBodyScreen(modifier: Modifier = Modifier, food: Food) {
    FoodInfoBody(food = food, modifier = modifier)
}

@Composable
fun FoodInfoBody(
    modifier: Modifier = Modifier,
    food: Food
) {
    Column {
        Row {
            Text(
                modifier = modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                text = "Nutrition Facts",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                text = "Calories: ${round(food.cal)} (per serving)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                text = "Total Fat: ${round(food.totalFat)}g",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 18.dp),
                textAlign = TextAlign.Center,
                text = "Saturated Fat: ${round(food.saturatedFat)}g",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                text = "Cholesterol: ${round(food.cholesterol)}mg",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                text = "Sodium: ${round(food.sodium)}mg",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                text = "Total Carbohydrates: ${round(food.totalCarbs)}g",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 18.dp),
                textAlign = TextAlign.Center,
                text = "Dietary Fiber: ${round(food.fiber)}g",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 18.dp),
                textAlign = TextAlign.Center,
                text = "Sugars: ${round(food.sugars ?: 0.0)}g",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                text = "Protein: ${round(food.protein)}g",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = modifier.padding(start = 6.dp),
                textAlign = TextAlign.Center,
                text = "Potassium: ${round(food.potassium)}g",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview
@Composable
fun FoodInfoBodyPreview() {
    val food = remember {
        Food(
            111,
            "aaa",
            1.0,
            "small",
            100.00,
            100.00,
            20.00,
            20.00,
            40.00,
            100.00,
            600.00,
            300.00,
            300.00,
            10.00,
            150.00,
            120.00,
            Photo("www.aaa.com/photo1")
        )
    }
    MainTheme {
        FoodInfoBody(
            food = food
        )
    }
}
package com.gmartinsdev.nutri_demo.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.gmartinsdev.nutri_demo.R
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme

/**
 * composable responsible for displaying nutritional info and recipe ingredients of selected food
 */
@Composable
fun FoodInfoScreen(
    foodInfo: Food,
    ingredients: List<SubRecipe>
) {
    val context = LocalContext.current

    val nutrients = remember {
        mutableStateMapOf(
            "cal" to foodInfo.cal,
            "fat" to foodInfo.totalFat,
            "sat_fat" to foodInfo.saturatedFat,
            "cholesterol" to foodInfo.cholesterol,
            "sodium" to foodInfo.sodium,
            "carbs" to foodInfo.totalCarbs,
            "fiber" to foodInfo.fiber,
            "sugar" to (foodInfo.sugars ?: 0.0),
            "protein" to foodInfo.protein,
            "potassium" to foodInfo.potassium
        )
    }
    val ingredientsValues =
        remember { ingredients.associate { it.food to it.servingQty }.toMutableMap() }
    FoodInfo(foodInfo, nutrients, ingredients, ingredientsValues) { name, amount ->
        Toast.makeText(context, "ingredient changed: $name to $amount", Toast.LENGTH_LONG)
    }
}

@Composable
fun FoodInfo(
    item: Food,
    nutrients: MutableMap<String, Double>,
    ingredients: List<SubRecipe>,
    ingredientValues: MutableMap<String, Double>,
    onIngredientChange: (ingredientName: String, newAmount: Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        Row {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .weight(1f),
                painter = rememberAsyncImagePainter(
                    model = item.photo.thumb,
                    error = painterResource(id = R.drawable.notlikethis),
                    placeholder = painterResource(id = R.drawable.image_not_available)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier
                    .size(50.dp)
                    .weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Qty: \n${item.servingQty}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                modifier = Modifier
                    .size(50.dp)
                    .weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Unit: \n${item.servingUnit}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                modifier = Modifier
                    .size(50.dp)
                    .weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Food: \n${item.name}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                modifier = Modifier
                    .size(50.dp)
                    .weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Calories: \n${nutrients["cal"]}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                modifier = Modifier
                    .size(50.dp)
                    .weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Weight: \n${item.servingWeight}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = "Nutrition Facts",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = "Calories: ${nutrients["cal"]} (per serving)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = "Total Fat: ${nutrients["fat"]}g",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 18.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Saturated Fat: ${nutrients["sat_fat"]}g",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = "Cholesterol: ${nutrients["cholesterol"]}mg",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = "Sodium: ${nutrients["sodium"]}mg",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = "Total Carbohydrates: ${nutrients["carbs"]}g",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 18.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Dietary Fiber: ${nutrients["fiber"]}g",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 18.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Sugars: ${nutrients["sugar"]}g",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = "Protein: ${nutrients["protein"]}g",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(start = 6.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Potassium: ${nutrients["potassium"]}g",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            LazyColumn {
                items(ingredients) { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            text = item.food,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        TextField(
                            modifier = Modifier
                                .padding(start = 6.dp, end = 6.dp)
                                .weight(1f),
                            value = ingredientValues[item.food].toString(),
                            onValueChange = { ingredientValues[item.food] = it.toDouble() },
                            label = { Text("Amount") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 6.dp),
                            onClick = {
                                onIngredientChange.invoke(
                                    item.food,
                                    ingredientValues[item.food] ?: 0.0
                                )
                            }) {
                            Text("Ok")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FoodInfoPreview() {
    MainTheme {
        FoodInfo(
            item = Food(
                1,
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
            ),
            nutrients = mutableMapOf(
                "cal" to 100.0,
                "fat" to 20.0,
                "sat_fat" to 10.0,
                "cholesterol" to 50.0,
                "sodium" to 150.0,
                "carbs" to 150.0,
                "fiber" to 50.0,
                "sugar" to 100.0,
                "protein" to 30.0,
                "potassium" to 200.0,
            ),
            ingredients = listOf(
                SubRecipe(
                    1,
                    1,
                    1.0,
                    "egg",
                    100.0,
                    1.0,
                    "g"
                ),
                SubRecipe(
                    2,
                    1,
                    1.0,
                    "cheese",
                    200.0,
                    2.0,
                    "g"
                ),
                SubRecipe(
                    3,
                    1,
                    1.0,
                    "flour",
                    300.0,
                    3.0,
                    "g"
                ),
            ),
            ingredientValues = mutableMapOf(
                "egg" to 1.0,
                "cheese" to 200.0,
                "flour" to 1.0
            )
        ) { name, amount ->

        }
    }
}
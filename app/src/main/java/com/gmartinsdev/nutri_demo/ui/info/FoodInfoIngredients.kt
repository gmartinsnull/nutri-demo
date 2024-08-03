package com.gmartinsdev.nutri_demo.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme

/**
 * composable responsible for displaying the ingredients of selected food
 */
@Composable
fun FoodInfoIngredientsScreen(
    modifier: Modifier = Modifier,
    ingredients: SnapshotStateList<SubRecipe>,
    onUpdatedIngredient: (List<SubRecipe>) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    FoodInfoIngredients(
        ingredients = ingredients,
        modifier = modifier
    ) {
        keyboardController?.hide()
        focus.clearFocus()
        onUpdatedIngredient.invoke(ingredients)
    }
}

@Composable
fun FoodInfoIngredients(
    modifier: Modifier = Modifier,
    ingredients: SnapshotStateList<SubRecipe>,
    onUpdatedIngredient: (List<SubRecipe>) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = modifier.weight(1f)
            ) {
                itemsIndexed(ingredients.toList()) { idx, ingredient ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            text = ingredient.food,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        TextField(
                            modifier = modifier.weight(1f),
                            value = ingredient.servingWeight.toString(),
                            onValueChange = {
                                val updatedIngredient =
                                    ingredient.copy(servingWeight = it.toDouble())
                                ingredients[idx] = updatedIngredient
                            },
                            label = { Text("Amount") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onUpdatedIngredient.invoke(ingredients)
                                }
                            )
                        )
                    }
                }
            }
            Button(
                modifier = modifier
                    .padding(top = 6.dp, bottom = 50.dp)
                    .size(width = 300.dp, height = 80.dp),
                onClick = {
                    onUpdatedIngredient.invoke(ingredients)
                }) {
                Text(
                    text = "Recalculate",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview
@Composable
fun FoodInfoIngredientsPreview() {
    val ingredients = remember {
        mutableStateListOf<SubRecipe>().apply {
            listOf(
                SubRecipe(
                    1,
                    111,
                    1.0,
                    "egg",
                    100.0,
                    1.0,
                    "g"
                ),
                SubRecipe(
                    2,
                    222,
                    1.0,
                    "cheese",
                    200.0,
                    2.0,
                    "g"
                ),
                SubRecipe(
                    3,
                    333,
                    1.0,
                    "flour",
                    300.0,
                    3.0,
                    "g"
                )
            ).forEach { ingredient ->
                add(ingredient)
            }
        }
    }
    MainTheme {
        FoodInfoIngredients(
            ingredients = ingredients
        ) {

        }
    }
}
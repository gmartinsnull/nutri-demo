package com.gmartinsdev.nutri_demo.ui.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.gmartinsdev.nutri_demo.R
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.ui.theme.MainTheme

/**
 * composable responsible for displaying food nutrition information of selected food
 */
@Composable
fun FoodInfoHeaderScreen(modifier: Modifier = Modifier, food: MutableState<Food>) {
    FoodInfoHeader(food = food, modifier = modifier)
}

@Composable
fun FoodInfoHeader(
    modifier: Modifier = Modifier,
    food: MutableState<Food>
) {
    Row(
        modifier = modifier
            .height(60.dp)
    ) {
        Image(
            modifier = modifier
                .weight(1f)
                .padding(6.dp),
            painter = rememberAsyncImagePainter(
                model = food.value.photo.thumb,
                error = painterResource(id = R.drawable.notlikethis),
                placeholder = painterResource(id = R.drawable.image_not_available)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = "Qty: \n${food.value.servingQty}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            modifier = modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = "Unit: \n${food.value.servingUnit}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            modifier = modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = "Food: \n${food.value.name}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            modifier = modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = "Calories: \n${food.value.cal}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            modifier = modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = "Weight: \n${food.value.servingWeight}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
fun FoodInfoHeaderPreview() {
    val food = remember {
        mutableStateOf(
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
        )
    }
    MainTheme {
        FoodInfoHeader(
            food = food
        )
    }
}
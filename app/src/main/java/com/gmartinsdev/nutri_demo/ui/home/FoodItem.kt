package com.gmartinsdev.nutri_demo.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
 *  composable class responsible for displaying a food item
 */
@Composable
fun FoodItemScreen(
    foodItem: Food,
    onClick: (Food) -> Unit
) {
    FoodItem(foodItem) {
        onClick.invoke(foodItem)
    }
}

@Composable
fun FoodItem(
    item: Food,
    onClick: (Food) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(shape = RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .clickable { onClick.invoke(item) }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.Yellow)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(
                    model = item.photo.thumb,
                    error = painterResource(id = R.drawable.notlikethis),
                    placeholder = painterResource(id = R.drawable.image_not_available)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .padding(top = 10.dp, start = 10.dp)
        ) {
            Text(
                modifier = Modifier.width(200.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = item.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "${item.cal} calories",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodItemPreview() {
    MainTheme {
        FoodItem(
            item = Food(
                111,
                "aaa",
                1.0,
                "small",
                100.00,
                100.00,
                100.00,
                100.00,
                100.00,
                100.00,
                100.00,
                100.00,
                100.00,
                100.00,
                100.00,
                100.00,
                Photo("www.aaa.com/photo1")
            )
        ) {

        }
    }
}
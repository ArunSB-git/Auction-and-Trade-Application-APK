package com.example.tradeapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@Composable
fun CategoryDetailScreen(category: Player, navController: NavController) {
    // Define the border color based on the player's status
    val borderColor = if (category.status == "available") Color.Green else Color.Red

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate(Screen.playerScreen.route) }, Modifier.padding(top = 16.dp)) {
            Text(text = "Go back")
        }

        // Create a separate Column for the image and status text
        Column(
            modifier = Modifier
                .wrapContentSize()
                .border(2.dp, borderColor) // Add border here around the image
                .padding(16.dp) // Inner padding for the image and status
        ) {
            Image(
                painter = rememberImagePainter(category.playerImage),
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Center the player's name
            Text(text = category.playername.uppercase(), textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.CenterHorizontally), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        // Status text with white background and green status value
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Status: ",
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Text(
                text = category.status.uppercase(),
                textAlign = TextAlign.Center,
                color = if (category.status == "available") Color.Green else Color.Red
            )
        }
        Text(text = "Nationality: ${category.nationality.uppercase()}")
        Text(text = "Market Value: ${formatAuctionPrice(category.auctionPrice)}")
    }
}

fun formatAuctionPrice(price: Int): String {
    return when {
        price >= 1_00_00_000 -> "${price / 1_00_00_000} CR" // Convert to Crores
        price >= 1_00_000 -> "${price / 1_00_000} L" // Convert to Lakhs
        else -> "$price" // Fallback to original value if less than 1L
    }
}


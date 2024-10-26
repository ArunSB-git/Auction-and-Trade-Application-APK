package com.example.tradeapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(category: Player, userId: Int, navController: NavController) {
    val borderColor = when (category.status) {
        "available" -> Color.Green
        "sold" -> Color.Red
        else -> Color.Yellow
    }

    val expanded = remember { mutableStateOf(false) }
    val bidAmount = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .border(2.dp, borderColor)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberImagePainter(category.playerImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = category.playername.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Accordion Header
            Text(
                text = "Details",
                modifier = Modifier
                    .clickable { expanded.value = !expanded.value }
                    .padding(8.dp),
                fontWeight = FontWeight.Bold
            )

            // Accordion Content
            if (expanded.value) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Nationality: ${category.nationality.uppercase()}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Market Value: ${formatAuctionPrice(category.auctionPrice)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(text = "Status: ")
                        Text(text = "${category.status.uppercase()}", color = borderColor)
                    }


                    // Bid TextField
                    TextField(
                        value = bidAmount.value,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } && it.length <= 3) { // Allow up to 4 digits
                                bidAmount.value = it
                            }
                        },
                        label = { Text("Enter Bid Amount           X 1,000,000") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Display formatted bid amount
                    val bidValueInMillions = bidAmount.value.toIntOrNull()?.times(1_000_000) ?: 0
                    Text(text = "Bid Amount: ${formatBidAmount(bidValueInMillions)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    // Bid Button
                    Button(onClick = {
                        val bidValue = bidAmount.value.toIntOrNull()
                        if (bidValue != null) {
                            placeBid(category.playerid, bidValue, userId, snackbarHostState) // Pass userId to placeBid
                        }
                    }) {
                        Text("Place Bid")
                    }
                }
            }
        }

        // Snackbar positioned at the top
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}



private fun placeBid(playerId: Int, bidValue: Int, userId: Int, snackbarHostState: SnackbarHostState) {
    CoroutineScope(Dispatchers.IO).launch {
        val bidAmount = bidValue * 1_000_000 // Multiply by 1 million
        val response = bidService.placeBid(BidRequest(userId, playerId, bidAmount))
        // Handle the response
        when (response.code()) {
            201 -> {
                showSnackbar(snackbarHostState, "Bidded successfully", Color.Green)
            }
            400 -> {
                val errorResponse = response.errorBody()?.string()
                showSnackbar(snackbarHostState, "Player was bidded already", Color.Red)
            }
            else -> showSnackbar(snackbarHostState, "Unexpected error", Color.Red)
        }
    }
}

private suspend fun showSnackbar(snackbarHostState: SnackbarHostState, message: String, color: Color) {
    snackbarHostState.showSnackbar(message)
}

fun formatAuctionPrice(price: Int): String {
    return when {
        price >= 1_00_00_000 -> "${price / 1_00_00_000} CR" // Convert to Crores
        price >= 1_00_000 -> "${price / 1_00_000} L" // Convert to Lakhs
        else -> "$price" // Fallback to original value if less than 1L
    }
}

fun formatBidAmount(amount: Int): String {
    return when {
        amount >= 1_00_00_000 -> String.format("%.1f CR", amount / 1_00_00_000.0) // Format to Crores
        amount >= 1_00_000 -> "${amount / 1_00_000} L" // Convert to Lakhs
        else -> "$amount" // Fallback to original value if less than 1L
    }
}

data class BidRequest(val userid: Int, val playerid: Int, val bidAmount: Int)

data class BidResponse(
    val transactionid: Int,
    val userid: Int,
    val playerid: Int,
    val bidAmount: Int,
    val status: String
)

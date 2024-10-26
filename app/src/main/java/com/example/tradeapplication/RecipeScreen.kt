package com.example.tradeapplication

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberImagePainter


@Composable
fun RecipeScreen(modifier: Modifier = Modifier, navigationToDetails: (Player) -> Unit, viewstate: MainViewModel.ReceipeState) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewstate.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            viewstate.error != null -> {
                Text("ERROR: ${viewstate.error}", modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                PlayerScreen(categories = viewstate.list, navigationToDetails)
            }
        }
    }
}

@Composable
fun PlayerScreen(categories: List<Player>, navigationToDetails: (Player) -> Unit) {
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        items(categories) { category ->
            PlayerItem(category = category, navigationToDetails)
        }
    }
}

@Composable
fun PlayerItem(category: Player, navigationToDetails: (Player) -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable { navigationToDetails(category) }
            .border(3.dp, Color.Blue)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(category.playerImage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
            )

            Text(
                text = category.playername.uppercase(),
                color = Color.Yellow,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

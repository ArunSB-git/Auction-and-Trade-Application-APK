package com.example.tradeapplication

sealed class Screen(val route:String) {

    object  playerScreen:Screen("receipescreen")
    object  detailedScreen:Screen("detailedscreen")
}
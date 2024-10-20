package com.example.tradeapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Player(val playerid: Int, val playername: String, val playerImage: String, val status: String, val nationality: String, val auctionPrice: Int) : Parcelable


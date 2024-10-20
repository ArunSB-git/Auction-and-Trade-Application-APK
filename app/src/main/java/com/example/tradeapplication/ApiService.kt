package com.example.tradeapplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit=Retrofit.Builder().baseUrl("https://auction-and-trade-application-server.onrender.com/api/").addConverterFactory(GsonConverterFactory.create()).build()

val playerService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("listOfPlayers")
    suspend fun getPlayers(): List<Player> // Change this to return a list of Player directly
}

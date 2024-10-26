package com.example.tradeapplication

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private val retrofit =
    Retrofit.Builder().baseUrl("https://auction-and-trade-application-server.onrender.com/api/")
        .addConverterFactory(GsonConverterFactory.create()).build()

val playerService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("listOfPlayers")
    suspend fun getPlayers(): List<Player> // Change this to return a list of Player directly

}

interface BidService {
    @POST("createBid") // Define your endpoint
    suspend fun placeBid(@Body request: BidRequest): Response<BidResponse>
}

interface LoginService {
    @POST("auth") // Define your login endpoint
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val userid: Int, val role: String)

val bidService = retrofit.create(BidService::class.java)

val loginService = retrofit.create(LoginService::class.java)
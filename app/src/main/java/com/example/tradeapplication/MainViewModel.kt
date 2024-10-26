package com.example.tradeapplication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _playerState = mutableStateOf(ReceipeState())
    val _playersState: State<ReceipeState> = _playerState

    data class LoginResponse(val userid: Int, val role: String)

    init {
        fetchPlayers()
    }

    private fun fetchPlayers() {
        viewModelScope.launch {
            _playerState.value = ReceipeState(loading = true)
            try {
                val players = playerService.getPlayers()
                _playerState.value = _playerState.value.copy(
                    list = players,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _playerState.value = _playerState.value.copy(
                    loading = false,
                    error = "Error fetching data: ${e.message}"
                )
            }
        }
    }

    data class ReceipeState(
        val loading: Boolean = true,
        val list: List<Player> = emptyList(),
        val error: String? = null
    )

    fun login(username: String, password: String, onSuccess: (Int, String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = loginService.login(LoginRequest(username, password)) // Implement this in ApiService
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it.userid, it.role)
                    }
                } else {
                    onError("Invalid credentials")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }
}



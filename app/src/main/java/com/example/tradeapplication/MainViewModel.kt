package com.example.tradeapplication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _playerState = mutableStateOf(ReceipeState())
    val _playersState: State<ReceipeState> = _playerState

    init {
        fetchPlayers()
    }

    private fun fetchPlayers() {
        viewModelScope.launch {
            _playerState.value = ReceipeState(loading = true) // Set loading to true
            try {
                val players = playerService.getPlayers() // Fetch list of players directly
                _playerState.value = _playerState.value.copy(
                    list = players, // Set the fetched list
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
}
package com.example.pokemon.ui.pokemonDetails.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.common.Resource
import com.example.pokemon.domain.api.repository.PokemonRepository
import com.example.pokemon.domain.toPokemonDetails
import com.example.pokemon.ui.pokemonDetails.effect.PokemonDetailsEffect
import com.example.pokemon.ui.pokemonDetails.event.PokemonDetailsEvent
import com.example.pokemon.ui.pokemonDetails.state.PokemonDetailsScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    private val _effect = Channel<PokemonDetailsEffect>(Channel.UNLIMITED)
    val effect: Flow<PokemonDetailsEffect> = _effect.receiveAsFlow()

    private val _uiState = MutableStateFlow(PokemonDetailsScreenUIState.initial())
    val uiState: StateFlow<PokemonDetailsScreenUIState> = this._uiState.asStateFlow()

    fun onEvent(event: PokemonDetailsEvent) {
        when (event) {
            is PokemonDetailsEvent.FetchPokemonDetails -> {
                fetchPokemonDetails(event.name)
            }
            is PokemonDetailsEvent.OnBack -> navigateBack()
        }
    }

    @VisibleForTesting
    fun navigateBack() = viewModelScope.launch {
        _effect.send(PokemonDetailsEffect.Navigation.OnBack)
    }

    @VisibleForTesting
    fun fetchPokemonDetails(name: String) = viewModelScope.launch {
        pokemonRepository.getPokemonDetails(name).collect { result ->
            Log.d("pokemonRepository.getPokemonDetails(pokemon.url).collect { result ->", "Called")
            when (result) {
                is Resource.Success -> {
                    result.data?.let { details ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                loading = false,
                                pokemonDetail = details.toPokemonDetails()
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(error = result.message ?: "An unexpected error occurred")
                    }
                }

                is Resource.Loading -> {
                    _uiState.update { it.copy(loading = true) }
                }
            }
        }
    }
}
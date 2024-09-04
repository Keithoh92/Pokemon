package com.example.pokemon.ui.pokemon.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemon.data.api.dao.PokemonDTO
import com.example.pokemon.domain.api.repository.PokemonRepository
import com.example.pokemon.ui.pokemon.effect.PokemonScreenEffect
import com.example.pokemon.ui.pokemon.event.PokemonScreenEvent
import com.example.pokemon.ui.pokemon.state.PokemonScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonScreenViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) : ViewModel() {

    private val _effect = Channel<PokemonScreenEffect>(Channel.UNLIMITED)
    val effect: Flow<PokemonScreenEffect> = _effect.receiveAsFlow()

    private val _uiState = MutableStateFlow(PokemonScreenUIState.initial())
    val uiState: StateFlow<PokemonScreenUIState> = this._uiState.asStateFlow()

    private val _pokemonDTOState: MutableStateFlow<PagingData<PokemonDTO>> =
        MutableStateFlow(value = PagingData.empty())
    val pokemonState: MutableStateFlow<PagingData<PokemonDTO>> get() = _pokemonDTOState

    init {
        fetchPokemon()
    }

    fun onEvent(event: PokemonScreenEvent) {
        when (event) {
            is PokemonScreenEvent.OnPokemonClicked -> navigateToPokemonDetails(event.name)
        }
    }

    @VisibleForTesting
    fun navigateToPokemonDetails(name: String) = viewModelScope.launch {
        _effect.send(PokemonScreenEffect.Navigation.OnNavigateToPokemonDetails(name))
    }

    @VisibleForTesting
    fun fetchPokemon() = viewModelScope.launch {
        pokemonRepository.getPokemonStream()
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _pokemonDTOState.value = it
            }
    }
}
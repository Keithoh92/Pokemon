package com.example.pokemon.ui.pokemon.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemon.data.api.dao.PokemonDTO
import com.example.pokemon.domain.api.repository.PokemonRepository
import com.example.pokemon.domain.toPokemon
import com.example.pokemon.ui.pokemon.effect.PokemonScreenEffect
import com.example.pokemon.ui.pokemon.event.PokemonScreenEvent
import com.example.pokemon.ui.pokemon.helper.AutoCompleteSearchSystem
import com.example.pokemon.ui.pokemon.state.PokemonScreenUIState
import com.example.pokemon.ui.pokemon.view.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonScreenViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val autoCompleteSearchSystem: AutoCompleteSearchSystem
) : ViewModel() {

    private val _effect = Channel<PokemonScreenEffect>(Channel.UNLIMITED)
    val effect: Flow<PokemonScreenEffect> = _effect.receiveAsFlow()

    private val _uiState = MutableStateFlow(PokemonScreenUIState())
    val uiState: StateFlow<PokemonScreenUIState> = this._uiState.asStateFlow()

    private val _pokemonDTOState: MutableStateFlow<PagingData<PokemonDTO>> =
        MutableStateFlow(value = PagingData.empty())
    val pokemonState: MutableStateFlow<PagingData<PokemonDTO>> get() = _pokemonDTOState

    private val listOfPokemonLoaded = mutableListOf<Pokemon>()

    init {
        fetchPokemon()
    }

    fun onEvent(event: PokemonScreenEvent) {
        when (event) {
            is PokemonScreenEvent.OnPokemonClicked -> navigateToPokemonDetails(event.name)
            is PokemonScreenEvent.OnSearchTextChanged -> onSearchTextChanged(event.prefix)
            is PokemonScreenEvent.OnSelectSearchedTvShow -> onSelectSearchedTvShow(event.id)
            is PokemonScreenEvent.SavePokemon -> savePokemon(event.pokemonList)
            is PokemonScreenEvent.SetScrollToIdToFalse -> resetScrollToId()
        }
    }

    private fun resetScrollToId() {
        _uiState.update { it.copy(scrollToPokemonById = Pair(false, -1)) }
    }

    private fun savePokemon(pokemonList: List<PokemonDTO?>) {
        pokemonList.forEach {
            val pokemon = it?.toPokemon()
            if (pokemon != null) {
                listOfPokemonLoaded.add(pokemon)
            }
            pokemon?.name?.lowercase()?.let { name -> autoCompleteSearchSystem.insert(name) }
        }
    }

    private fun onSelectSearchedTvShow(id: Int) {
        _uiState.update { it.copy(
            scrollToPokemonById = Pair(true, id),
            searchedTvShows = emptyList()
        )}
    }

    private fun onSearchTextChanged(prefix: String) {
        val pokemon = autoCompleteSearchSystem.search(prefix.lowercase())

        val listOfSearchedPokemon = pokemon.map { suggestedPokemon ->
            val pokemonId = listOfPokemonLoaded.find {
                it.name.lowercase() == suggestedPokemon.lowercase()
            }?.id ?: -1

            Pair(pokemonId, suggestedPokemon)
        }

        _uiState.update {
            it.copy(
                searchedTvShows = if (prefix.isBlank()) emptyList() else listOfSearchedPokemon
            )
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
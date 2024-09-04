package com.example.pokemon.ui.pokemon.event

import com.example.pokemon.data.api.dao.PokemonDTO

sealed class PokemonScreenEvent {
    data class OnPokemonClicked(val name: String) : PokemonScreenEvent()
    data class OnSearchTextChanged(val prefix: String) : PokemonScreenEvent()
    data class OnSelectSearchedTvShow(val id: Int): PokemonScreenEvent()
    data class SavePokemon(val pokemonList: List<PokemonDTO?>) : PokemonScreenEvent()
    data object SetScrollToIdToFalse : PokemonScreenEvent()
}
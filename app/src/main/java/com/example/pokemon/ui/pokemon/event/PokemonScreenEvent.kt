package com.example.pokemon.ui.pokemon.event

sealed class PokemonScreenEvent {
    data class OnPokemonClicked(val name: String) : PokemonScreenEvent()
}
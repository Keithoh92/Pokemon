package com.example.pokemon.ui.pokemon.view.model

typealias PokemonView = Pokemon

data class Pokemon(
    val id: Int,
    val name: String,
    val url: String
)
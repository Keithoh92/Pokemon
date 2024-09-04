package com.example.pokemon.ui.pokemon.view.model

data class PokemonType(
    val slot: Int,
    val type: PokemonTypeDetail
)

data class PokemonTypeDetail(
    val name: String
)
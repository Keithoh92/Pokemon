package com.example.pokemon.data.api.dto

data class PokemonAPIResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonDTO>
)

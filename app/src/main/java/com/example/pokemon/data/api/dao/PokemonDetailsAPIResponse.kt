package com.example.pokemon.data.api.dao

data class PokemonDetailsAPIResponse(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val types: List<TypeDTO>,
    val stats: List<StatsDTO>,
    val sprites: SpritesDTO
)

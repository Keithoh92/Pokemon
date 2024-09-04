package com.example.pokemon.data.api.dao

data class TypeDTO(
    val slot: Int,
    val type: TypeDetailDTO
)

data class TypeDetailDTO(
    val name: String
)

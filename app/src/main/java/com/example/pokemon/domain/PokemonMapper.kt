package com.example.pokemon.domain

import com.example.pokemon.data.api.dto.PokemonDTO
import com.example.pokemon.data.api.dto.PokemonDetailsAPIResponse
import com.example.pokemon.data.api.dto.SpritesDTO
import com.example.pokemon.data.api.dto.StatsDTO
import com.example.pokemon.data.api.dto.TypeDTO
import com.example.pokemon.data.api.dto.TypeDetailDTO
import com.example.pokemon.ui.pokemon.view.model.PokemonDetails
import com.example.pokemon.ui.pokemon.view.model.PokemonStat
import com.example.pokemon.ui.pokemon.view.model.PokemonType
import com.example.pokemon.ui.pokemon.view.model.PokemonTypeDetail
import com.example.pokemon.ui.pokemon.view.model.PokemonView
import com.example.pokemon.ui.pokemon.view.model.Stat

fun PokemonDetailsAPIResponse.toPokemonDetails(): PokemonDetails {
    return PokemonDetails(
        id = this.id,
        name = this.name,
        weight = this.weight,
        height = this.height,
        stats = this.stats.toPokemonStats(),
        type = this.types.toPokemonType(),
        sprite = this.sprites.toSpritesList()
    )
}

fun SpritesDTO.toSpritesList(): List<String> {
    return listOfNotNull(
        frontDefault,
        frontShiny,
        frontFemale,
        frontShinyFemale,
        backDefault,
        backShiny,
        backFemale,
        backShinyFemale
    )
}

fun List<TypeDTO>.toPokemonType(): List<PokemonType> {
    return this.map {
        PokemonType(
            slot = it.slot,
            type = it.type.toPokemonTypeDetail()
        )
    }
}

fun TypeDetailDTO.toPokemonTypeDetail(): PokemonTypeDetail {
    return PokemonTypeDetail(
        name = this.name
    )
}

fun List<StatsDTO>.toPokemonStats(): List<PokemonStat> {
    return this.map {
        PokemonStat(
            baseStat = it.baseStat,
            effort = it.effort,
            stat = Stat(it.stat.name, it.stat.url)
        )
    }
}

fun PokemonDTO.toPokemon(): com.example.pokemon.ui.pokemon.view.model.Pokemon {
    return PokemonView(
        id = this.url.substringAfterLast("pokemon/").trimEnd('/').toInt(),
        name = this.name,
        url = this.url
    )
}
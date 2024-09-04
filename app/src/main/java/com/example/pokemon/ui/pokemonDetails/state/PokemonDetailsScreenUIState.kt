package com.example.pokemon.ui.pokemonDetails.state

import com.example.pokemon.data.api.dao.SpritesDTO
import com.example.pokemon.domain.toSpritesList
import com.example.pokemon.ui.pokemon.view.model.PokemonDetails
import com.example.pokemon.ui.pokemon.view.model.PokemonStat
import com.example.pokemon.ui.pokemon.view.model.PokemonType
import com.example.pokemon.ui.pokemon.view.model.PokemonTypeDetail
import com.example.pokemon.ui.pokemon.view.model.Stat

data class PokemonDetailsScreenUIState(
    val loading: Boolean,
    val error: String,
    val pokemonDetail: PokemonDetails,
) {
    companion object {
        fun initial() = PokemonDetailsScreenUIState(
            loading = true,
            error = "",
            pokemonDetail = PokemonDetails(
                id = 1,
                name = "Charizad",
                weight = 1,
                height = 4,
                type = listOf(PokemonType(slot = 1, type = PokemonTypeDetail(name = ""))),
                stats = listOf(
                    PokemonStat(
                        baseStat = 1,
                        effort = 1,
                        stat = Stat(name = "", url = "")
                    )
                ),
                sprite = SpritesDTO(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
                ).toSpritesList()
            ),
        )
    }
}

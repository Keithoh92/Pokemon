package com.example.pokemon.ui

import com.example.pokemon.BaseTest
import com.example.pokemon.common.Resource
import com.example.pokemon.data.api.dto.PokemonDetailsAPIResponse
import com.example.pokemon.data.api.dto.SpritesDTO
import com.example.pokemon.data.api.dto.StatDTO
import com.example.pokemon.data.api.dto.StatsDTO
import com.example.pokemon.data.api.dto.TypeDTO
import com.example.pokemon.data.api.dto.TypeDetailDTO
import com.example.pokemon.domain.api.repository.PokemonRepository
import com.example.pokemon.ui.pokemon.view.model.PokemonDetails
import com.example.pokemon.ui.pokemon.view.model.PokemonStat
import com.example.pokemon.ui.pokemon.view.model.PokemonType
import com.example.pokemon.ui.pokemon.view.model.PokemonTypeDetail
import com.example.pokemon.ui.pokemon.view.model.Stat
import com.example.pokemon.ui.pokemonDetails.effect.PokemonDetailsEffect
import com.example.pokemon.ui.pokemonDetails.viewmodel.PokemonDetailsViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PokemonDetailsViewModelTest() : BaseTest() {

    private lateinit var target: PokemonDetailsViewModel

    @RelaxedMockK
    private lateinit var pokemonRepository: PokemonRepository

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(testDispatcher)
        target = PokemonDetailsViewModel(pokemonRepository)
    }

    private val pokemonDetailsApiResponse = PokemonDetailsAPIResponse(
        1,
        name = "Charizard",
        weight = 1,
        height = 1,
        types = listOf(TypeDTO(slot = 1, type = TypeDetailDTO(name = ""))),
        stats = listOf(StatsDTO(baseStat = 1, effort = 1, stat = StatDTO("", ""))),
        sprites = SpritesDTO(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        )
    )

    private val pokemonDetails = PokemonDetails(
        1,
        name = "Charizard",
        weight = 1,
        height = 1,
        type = listOf(PokemonType(slot = 1, type = PokemonTypeDetail(name = ""))),
        stats = listOf(PokemonStat(baseStat = 1, effort = 1, stat = Stat("", ""))),
        sprite = listOf("", "", "", "", "", "", "", "")
    )

    @Test
    fun `navigateBAck() - WHEN called THEN navigate to PokemonDetailsScreen`() = runTest {
        target.navigateBack()
        assertEquals(PokemonDetailsEffect.Navigation.OnBack, target.effect.first())
    }

    @Test
    fun `fetchPokemonDetails() - WHEN called AND is Successful then update uiState`() = runTest {
        coEvery { pokemonRepository.getPokemonDetails("Charizard") } returns
                flowOf(Resource.Success(pokemonDetailsApiResponse))

        target.fetchPokemonDetails("Charizard")
        advanceTimeBy(1000)
        assertEquals(pokemonDetails, target.uiState.value.pokemonDetail)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchPokemonDetails() - WHEN called AND returns HttpException THEN update uiState`() = runTest {
        coEvery { pokemonRepository.getPokemonDetails("Charizard") } returns
                flowOf(Resource.Error("Http Exception, unexpected response"))

        target.fetchPokemonDetails("Charizard")
        advanceTimeBy(1000)
        assertEquals("Http Exception, unexpected response", target.uiState.value.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchPokemonDetails() - WHEN called AND returns IOException THEN update uiState`() = runTest {
        coEvery { pokemonRepository.getPokemonDetails("Charizard") } returns
                flowOf(Resource.Error("Couldn't reach server, check internet connection"))

        target.fetchPokemonDetails("Charizard")
        advanceTimeBy(1000)
        assertEquals("Couldn't reach server, check internet connection", target.uiState.value.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun tearDown() {
        Dispatchers.resetMain()
    }
}
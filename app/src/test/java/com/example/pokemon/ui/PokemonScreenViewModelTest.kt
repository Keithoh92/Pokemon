package com.example.pokemon.ui

import com.example.pokemon.BaseTest
import com.example.pokemon.data.api.dto.PokemonDTO
import com.example.pokemon.domain.api.repository.PokemonRepository
import com.example.pokemon.domain.toPokemon
import com.example.pokemon.ui.pokemon.event.PokemonScreenEvent
import com.example.pokemon.ui.pokemon.helper.AutoCompleteSearchSystem
import com.example.pokemon.ui.pokemon.viewmodel.PokemonScreenViewModel
import org.junit.jupiter.api.Assertions.assertEquals
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test

class PokemonScreenViewModelTest: BaseTest() {

    private lateinit var target: PokemonScreenViewModel

    @RelaxedMockK
    private lateinit var pokemonRepository: PokemonRepository

    @RelaxedMockK
    private lateinit var autoCompleteSearchSystem: AutoCompleteSearchSystem

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(testDispatcher)
        target = spyk(PokemonScreenViewModel(pokemonRepository, autoCompleteSearchSystem))
    }

    @Test
    fun `onEvent() - WHEN OnPokemonClicked THEN call navigateToPokemonDetails`() {
        target.onEvent(PokemonScreenEvent.OnPokemonClicked("name"))
        verify { target.navigateToPokemonDetails(any()) }
    }

    @Test
    fun `onEvent() - WHEN OnSearchTextChanged THEN call onSearchTextChanged`() {
        target.onEvent(PokemonScreenEvent.OnSearchTextChanged("name"))
        justRun { autoCompleteSearchSystem.search(any()) }
        verify { target.onSearchTextChanged("name") }
    }

    @Test
    fun `onEvent() - WHEN OnSelectSearchedTvShow THEN call onSelectSearchedTvShow`() {
        target.onEvent(PokemonScreenEvent.OnSelectSearchedTvShow(1))
        verify { target.onSelectSearchedTvShow(any()) }
    }

    @Test
    fun `onEvent() - WHEN SavePokemon THEN call savePokemon`() {
        target.onEvent(PokemonScreenEvent.SavePokemon(mockk<List<PokemonDTO>>(relaxed = true)))
        verify { target.savePokemon(any()) }
    }

    @Test
    fun `onEvent() - WHEN SetScrollToIdToFalse THEN call resetScrollToId`() {
        target.onEvent(PokemonScreenEvent.SetScrollToIdToFalse)
        verify { target.resetScrollToId() }
    }

    @Test
    fun `savePokemon() - WHEN invoked AND list is supplied THEN update listOfPokemonLoaded and insert to AutoCompleteSystem`() {
        justRun { autoCompleteSearchSystem.insert(any()) }
        val pokemonDTO = PokemonDTO(
            name = "Charizard",
            url = "https://pokeapi.co/api/v2/pokemon/18/"
        )
        target.savePokemon(listOf(pokemonDTO))

        assertEquals(target.listOfPokemonLoaded, listOf(pokemonDTO.toPokemon()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun tearDown() {
        Dispatchers.resetMain()
    }
}
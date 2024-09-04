package com.example.pokemon.data

import com.example.pokemon.BaseTest
import com.example.pokemon.common.Resource
import com.example.pokemon.data.api.dto.PokemonDetailsAPIResponse
import com.example.pokemon.data.api.dto.SpritesDTO
import com.example.pokemon.data.api.dto.StatDTO
import com.example.pokemon.data.api.dto.StatsDTO
import com.example.pokemon.data.api.dto.TypeDTO
import com.example.pokemon.data.api.dto.TypeDetailDTO
import com.example.pokemon.data.api.remote.PokemonApi
import com.example.pokemon.domain.api.repository.PokemonRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PokemonAPIRepoTest : BaseTest() {

    private lateinit var target: PokemonRepository

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var pokemonApi: PokemonApi

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(testDispatcher)
        target = PokemonRepository(pokemonApi)
    }

    @Test
    fun `getPokemonDetails emits Error WHEN IOException is thrown`() = runTest {
        coEvery { pokemonApi.downloadPokemonDetails("url") } throws
                IOException("Network error")
        val flow = target.getPokemonDetails("url").toList()

        assertEquals(2, flow.size)
        assertTrue(flow[0] is Resource.Loading)
        assertTrue(flow[1] is Resource.Error)
        assertEquals(
            "Couldn't reach server, check internet connection: Network error",
            (flow[1] as Resource.Error).message
        )
    }

    @Test
    fun `getPokemonDetails emits Error WHEN HttpException is thrown`() = runTest {
        val mockApiResponse = mockk<Response<PokemonDetailsAPIResponse>>()
        val mockHttpException = mockk<HttpException>(relaxed = true).apply {
            every { this@apply.localizedMessage } returns "Http Exception"
            every { this@apply.response() } returns Response.error<Any>(
                404,
                mockk<ResponseBody>(relaxed = true)
            )
        }
        every { mockApiResponse.body() } throws mockHttpException

        coEvery { pokemonApi.downloadPokemonDetails("url") } returns mockApiResponse
        every { mockApiResponse.isSuccessful } returns false
        val flow = target.getPokemonDetails("url").toList()

        assertEquals(2, flow.size)
        assertTrue(flow[0] is Resource.Loading)
        assertTrue(flow[1] is Resource.Error)
        assertTrue((flow[1] as Resource.Error).message?.contains("Http Exception") == true)
    }

    @Test
    fun `getPokemonDetails() - WHEN successful THEN emit SUCCESS`() = runTest {
        val mockApiResponse = mockk<Response<PokemonDetailsAPIResponse>>()

        val pokemonDetails = PokemonDetailsAPIResponse(
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
        coEvery { mockApiResponse.body() } returns pokemonDetails
        coEvery { mockApiResponse.isSuccessful } returns true
        coEvery { pokemonApi.downloadPokemonDetails("dummy") } returns mockApiResponse

        val result = target.getPokemonDetails("dummy").toList()

        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        val resourceSuccess = result[1] as Resource.Success<PokemonDetailsAPIResponse>
        assertEquals(pokemonDetails, resourceSuccess.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun tearDown() {
        Dispatchers.resetMain()
    }
}
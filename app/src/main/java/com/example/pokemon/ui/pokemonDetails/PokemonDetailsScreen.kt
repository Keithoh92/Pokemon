package com.example.pokemon.ui.pokemonDetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.pokemon.ui.common.PokemonError
import com.example.pokemon.ui.common.ThemePreview
import com.example.pokemon.ui.common.TopAppBar
import com.example.pokemon.ui.pokemonDetails.state.PokemonDetailsScreenUIState
import com.example.pokemon.ui.pokemonDetails.view.ImageCarouselIndexIndicator
import com.example.pokemon.ui.pokemonDetails.view.SwipePokemonImage
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.ui.theme.spacing12
import com.example.pokemon.ui.theme.spacing8
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PokemonDetailsScreen(
    pokemonDetailUIState: StateFlow<PokemonDetailsScreenUIState>,
    onBack: () -> Unit,
) {

    val state by pokemonDetailUIState.collectAsState()

    var selectedImageIndex by remember { mutableIntStateOf(0) }

    Surface {
        Scaffold(
            topBar = { TopAppBar(title = state.pokemonDetail.name, onBack = { onBack() }) },
            content = { padding ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .systemBarsPadding(),
                ) {
                    if (state.loading) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LinearProgressIndicator()
                            Text(text = "Loading, please wait")
                        }
                    } else if (state.error.isNotEmpty()) {
                        PokemonError(error = state.error, modifier = Modifier.fillMaxSize())
                    } else {
                        Column {
                            Card(
                                colors = CardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.White,
                                    disabledContentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .zIndex(2.0f)
                                            .background(
                                                brush = Brush.linearGradient(
                                                    listOf(
                                                        Color.Transparent,
                                                        Color.Black.copy(alpha = 0.7f),
                                                    ),
                                                    start = Offset(9f, 400f),
                                                    end = Offset(0f, 500f)
                                                ),
                                            )
                                    )
                                    SwipePokemonImage(
                                        images = state.pokemonDetail.sprite,
                                        selectedIndex = { selectedImageIndex = it }
                                    )
                                    Text(
                                        text = state.pokemonDetail.name,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif,
                                        ),
                                        modifier = Modifier
                                            .zIndex(4.0f)
                                            .align(Alignment.BottomStart)
                                            .padding(16.dp)
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(spacing12)) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(bottom = 16.dp)
                                    ) {
                                        for (i in state.pokemonDetail.sprite.indices) {
                                            ImageCarouselIndexIndicator(isSelected = i == selectedImageIndex)
                                        }
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(spacing8)
                                ) {
                                    Text(
                                        text = "Height:",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${state.pokemonDetail.height}",
                                        style = MaterialTheme.typography.labelMedium
                                    )

                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(spacing8)
                                ) {
                                    Text(
                                        text = "Weight:",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${state.pokemonDetail.weight}",
                                        style = MaterialTheme.typography.labelMedium
                                    )

                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(spacing12)) {
                                    Text(
                                        text = "Stats:",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Column {
                                        state.pokemonDetail.stats.forEach {
                                            Text(
                                                text = "Name: ${it.stat.name}",
                                            )
                                            Text(
                                                text = "BaseStat: ${it.baseStat}",
                                            )
                                            Text(
                                                text = "Effort: ${it.effort}",
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    BackHandler(onBack = { onBack() })
}

@ThemePreview
@Composable
fun PokemonDetailsScreenPreview() {
    PokemonTheme {
        PokemonDetailsScreen(
            pokemonDetailUIState = MutableStateFlow(PokemonDetailsScreenUIState.initial()),
            onBack = {}
        )
    }
}
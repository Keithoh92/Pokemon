package com.example.pokemon.ui.pokemonDetails.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipePokemonImage(
    images: List<String>,
    selectedIndex: (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            key = { images[it] }
        ) { index ->
            LaunchedEffect(key1 = pagerState, block = {
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    selectedIndex.invoke(page)
                }
            })
            AsyncImage(
                model = images[index],
                contentDescription = null,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
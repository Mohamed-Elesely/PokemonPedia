package com.elesely.pokemonpedia.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.elesely.pokemonpedia.R
import com.elesely.pokemonpedia.data.models.PokemonListEntery
import com.elesely.pokemonpedia.ui.theme.RobotoCondensed

@Composable
fun PokemonLIstScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background, // Set the background color of the Surface
        modifier = Modifier.fillMaxSize() // Occupy the entire available space
    ) {
        Column { // Arrange children vertically
            Spacer(modifier = Modifier.height(20.dp)) // Add vertical spacing of 20.dp
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo), // Set the image resource
                contentDescription = "Logo", // Description for accessibility
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally) // Center the image horizontally
            )
            SearchBar(
                hint = "Search ... ", // Set the hint text for the search bar
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // Set the width, padding, and fill the available width
            )
            {
                viewModel.searchPokemonList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
        }
    }
}



@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") } // State variable to hold the entered text
    var isHintDisplayed by remember { mutableStateOf(hint != "") } // State variable to check if the hint should be displayed

    Box(modifier = modifier) { // Create a Box composable to contain the search bar and hint
        BasicTextField(
            value = text, // Set the value of the search bar
            onValueChange = {
                text = it // Update the value when text changes
                onSearch(it) // Call the onSearch function with the updated text
            },
            maxLines = 1, // Set the maximum number of lines to 1
            singleLine = true, // Set the search bar to be a single line
            textStyle = TextStyle(color = LocalContentColor.current), // Set the text color using LocalContentColor.current
            modifier = Modifier
                .fillMaxWidth() // Fill the available width
                .shadow(5.dp, CircleShape) // Apply a shadow with specified elevation and shape
                .background(color = Color.White, CircleShape) // Set the background color and shape
                .padding(horizontal = 20.dp, vertical = 10.dp) // Set the padding
                .onFocusChanged {
                    isHintDisplayed =
                        !it.isFocused // Update the isHintDisplayed based on focus state
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint, // Display the hint text
                color = Color.Gray, // Set the color of the hint text
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp) // Set the padding of the hint text
            )
        }
    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()

){
    val pokemonList by remember {viewModel.pokeList}
    val endReached by remember {viewModel.endReached}
    val loadErorr by remember {viewModel.loadErorr}
    val isLoading by remember {viewModel.isLoading}
    val isSearching by remember {viewModel.isSearching}

    LazyColumn(contentPadding = PaddingValues(16.dp)){
        val itemCount = if(pokemonList.size %2 ==0) {
            pokemonList.size/2
        }
        else{
            pokemonList.size / 2 + 1
        }
        items(itemCount){
            //This is instead of using paging library in xml
            if(it>=itemCount-1 && !endReached && !isLoading && !isSearching){
                LaunchedEffect(key1 = true ){
                    viewModel.loadPokemonPage()
                }
            }
            PokemonRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ){
        if(isLoading){
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(loadErorr.isNotEmpty()){
            RetrySection(error = loadErorr) {
                viewModel.loadPokemonPage()
            }
        }
    }
}

@Composable
fun pokeEntry(
    entry: PokemonListEntery,
    navController: NavController,
    modifier: Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface // Get the default dominant color from the MaterialTheme
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(
                5.dp,
                RoundedCornerShape(10.dp)
            ) // Apply a shadow with specified elevation and shape
            .clip(RoundedCornerShape(10.dp)) // Clip the box with rounded corners
            .aspectRatio(1f) // Maintain an aspect ratio of 1:1
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor, // Set the dominant color as the top gradient color
                        defaultDominantColor // Set the default dominant color as the bottom gradient color
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "pokemon_details_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
                ) // Navigate to the Pokemon details screen with dominant color and name as arguments
            }
    ) {
        Column { // Arrange children vertically
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.pokemonImagUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = entry.pokemonName,
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary, modifier = Modifier.scale(0.5F)
                    )
                },
                success = { success ->
                    viewModel.calcDominantColor(success.result.drawable){
                        dominantColor = it
                    }
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )


            Text(
                text = entry.pokemonName, // Display the name of the Pokemon
                fontFamily = RobotoCondensed, // Set the font family for the text
                fontSize = 25.sp, // Set the font size
                textAlign = TextAlign.Center, // Center align the text
                modifier = Modifier.fillMaxWidth() // Fill the available width
            )
        }
    }
}

@Composable
fun PokemonRow(
    rowIndex: Int,
    entries: List<PokemonListEntery>,
    navController: NavController,
) {
    Column { // Arrange children vertically
        Row { // Arrange children horizontally
            pokeEntry(
                entry = entries[rowIndex * 2], // Get the entry at the specified index
                navController = navController, // Pass the NavController to the pokeEntry composable
                modifier = Modifier.weight(1f) // Occupy the available space horizontally
            )
            Spacer(modifier = Modifier.width(19.dp)) // Add horizontal spacing of 19.dp
            if (entries.size >= rowIndex * 2 + 2) {
                pokeEntry(
                    entry = entries[rowIndex * 2 + 1], // Get the entry at the specified index
                    navController = navController, // Pass the NavController to the pokeEntry composable
                    modifier = Modifier.weight(1f) // Occupy the available space horizontally
                )
                Spacer(modifier = Modifier.width(16.dp)) // Add horizontal spacing of 19.dp
            } else {
                Spacer(modifier = Modifier.weight(1f)) // Occupy the available space horizontally
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Add vertical spacing of 19.dp
    }
}

@Composable
fun RetrySection(
    error:String,
    onRetry:() -> Unit
){

    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {onRetry()},
            modifier = Modifier.align(CenterHorizontally))
        {
            Text(text = "Retry")

        }
    }
}


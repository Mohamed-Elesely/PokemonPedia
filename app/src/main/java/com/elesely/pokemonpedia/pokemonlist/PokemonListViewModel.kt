package com.elesely.pokemonpedia.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.elesely.pokemonpedia.data.models.PokemonListEntery
import com.elesely.pokemonpedia.repository.PokemonRepo
import com.elesely.pokemonpedia.utils.Constants.PAGE_SIZE
import com.elesely.pokemonpedia.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(

    private val repo : PokemonRepo
) : ViewModel() {

    private var currentPage= 0

    var pokeList = mutableStateOf<List<PokemonListEntery>>(listOf())
    var loadErorr = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokemonListEntery>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonPage()
    }

    fun searchPokemonList(query:String){
        val listToSearch = if(isSearchStarting){
            pokeList.value
        }
        else{
            cachedPokemonList
        }
        viewModelScope.launch (Dispatchers.Default){
            if(query.isEmpty()){
                pokeList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch

            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true)||
                        it.pokemonNumber.toString() == query.trim()
            }
            if(isSearchStarting){
                cachedPokemonList = pokeList.value
                isSearchStarting = false

            }
            pokeList.value = results
            isSearching.value = true
        }
    }

    fun loadPokemonPage(){
        isLoading.value = true
        viewModelScope.launch {
            when(val result = repo.getPokemonList(PAGE_SIZE,currentPage * PAGE_SIZE)){
                is Resource.Success ->{
                    endReached.value = currentPage* PAGE_SIZE>=result.data!!.count
                    val pokeEntreies = result.data.results.mapIndexed{index,entry->
                        val number = if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }
                        else{
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokemonListEntery(entry.name.capitalize(Locale.ROOT),url,number.toInt())
                    }
                    currentPage++

                    loadErorr.value = ""
                    isLoading.value = false
                    pokeList.value = pokeEntreies
                }
                is Resource.Error ->{
                    loadErorr.value = result.message!!
                    isLoading.value =false
                }
                else -> {

                }
            }
        }
    }

    fun calcDominantColor (drawble : Drawable,onFinish:(Color) ->Unit) {

        val bmp = (drawble as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888,true)

        Palette.from(bmp).generate { pallete ->
            pallete?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}

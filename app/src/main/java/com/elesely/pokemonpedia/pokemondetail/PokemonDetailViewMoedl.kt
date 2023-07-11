package com.elesely.pokemonpedia.pokemondetail

import androidx.lifecycle.ViewModel
import com.elesely.pokemonpedia.data.remote.responses.Pokemon
import com.elesely.pokemonpedia.repository.PokemonRepo
import com.elesely.pokemonpedia.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewMoedl @Inject constructor(
    private val repo : PokemonRepo
): ViewModel() {

    suspend fun getPokemonInfo(pokemonName:String): Resource<Pokemon>{
        return repo.getPokemonInfo(pokemonName)
    }
}
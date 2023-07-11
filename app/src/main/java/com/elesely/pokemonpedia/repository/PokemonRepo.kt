package com.elesely.pokemonpedia.repository

import com.elesely.pokemonpedia.data.remote.network.PokeApi
import com.elesely.pokemonpedia.data.remote.responses.Pokemon
import com.elesely.pokemonpedia.data.remote.responses.PokemonList
import com.elesely.pokemonpedia.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class PokemonRepo @Inject constructor(
    private val api:PokeApi
) {
    suspend fun getPokemonList(limit:Int,offset:Int): Resource<PokemonList>{
        val response = try {
            api.getPokemonList(limit,offset)

        }catch (e:Exception){
            return Resource.Error("An error occurred ")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(PokemonName: String): Resource<Pokemon>{
        val response = try {
            api.getPokemonInfo(PokemonName)

        }catch (e:Exception){
            return Resource.Error("An error occurred ")
        }
        return Resource.Success(response)
    }
}
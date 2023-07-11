package com.elesely.pokemonpedia.data.remote.network

import com.elesely.pokemonpedia.data.remote.responses.Pokemon
import com.elesely.pokemonpedia.data.remote.responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    //Function for returning all Pokemons

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit:Int, // limit is for limit of pokeomons loaded at one time
        @Query("offset") offset:Int // offset is for
    ):PokemonList

    //Function for returning a specific pokemon

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name:String
    ):Pokemon

}
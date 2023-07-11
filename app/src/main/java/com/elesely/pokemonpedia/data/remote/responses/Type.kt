package com.elesely.pokemonpedia.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Type(
    val slot: Int,
    val type: TypeX
)
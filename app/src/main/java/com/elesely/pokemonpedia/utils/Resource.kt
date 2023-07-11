package com.elesely.pokemonpedia.utils

// Sealed class that represents different states or outcomes of an operation
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    // Subclass representing a successful resource
    class Success<T>(data: T) : Resource<T>(data)

    // Subclass representing an error resource
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // Subclass representing a loading resource
    class Loading<T>(data: T?= null):Resource<T>(data)
}

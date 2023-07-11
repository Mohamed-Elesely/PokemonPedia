package com.elesely.pokemonpedia.di

import com.elesely.pokemonpedia.data.remote.network.PokeApi
import com.elesely.pokemonpedia.repository.PokemonRepo
import com.elesely.pokemonpedia.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Provides a singleton instance of PokemonRepo
    @Singleton
    @Provides
    fun providePokemonRepo(api: PokeApi) = PokemonRepo(api)

    // Provides a singleton instance of PokeApi
    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }
}

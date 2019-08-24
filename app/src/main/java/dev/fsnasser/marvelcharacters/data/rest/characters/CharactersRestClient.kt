package dev.fsnasser.marvelcharacters.data.rest.characters

import dev.fsnasser.marvelcharacters.data.entities.CharacterApi
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersRestClient {

    @GET("characters")
    fun getAll(@Query("offset") offset: Int,
               @Query("limit") limit: Int,
               @Query("nameStartsWith") nameStartsWith: String?,
               @Query("orderBy") orderBy: String)
            : Observable<Response<CharacterApi>>

}
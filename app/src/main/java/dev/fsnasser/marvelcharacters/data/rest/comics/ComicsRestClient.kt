package dev.fsnasser.marvelcharacters.data.rest.comics

import dev.fsnasser.marvelcharacters.data.entities.ComicSerieApi
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ComicsRestClient {

    @GET("characters/{id}/comics")
    fun getAllFromCharacter(@Path("id") id: Long,
               @Query("limit") limit: Int,
               @Query("noVariants") noVariants: Boolean?)
            : Observable<Response<ComicSerieApi>>

}
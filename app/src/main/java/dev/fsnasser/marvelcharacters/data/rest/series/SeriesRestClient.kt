package dev.fsnasser.marvelcharacters.data.rest.series

import dev.fsnasser.marvelcharacters.data.entities.ComicSerieApi
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesRestClient {

    @GET("characters/{id}/series")
    fun getAllFromCharacter(@Path("id") id: Long,
                            @Query("limit") limit: Int)
            : Observable<Response<ComicSerieApi>>

}
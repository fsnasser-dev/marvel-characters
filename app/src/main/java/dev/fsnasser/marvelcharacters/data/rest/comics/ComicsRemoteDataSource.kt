package dev.fsnasser.marvelcharacters.data.rest.comics

import dev.fsnasser.marvelcharacters.data.entities.ComicSerieApi
import dev.fsnasser.marvelcharacters.utils.data.RetrofitServiceGenerator
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

class ComicsRemoteDataSource @Inject constructor(private val service: RetrofitServiceGenerator) {

    fun getAllFromCharacter(id: Long, limit: Int, noVariants: Boolean)
            : Observable<Response<ComicSerieApi>> {
        val mComicsService = service.createService(ComicsRestClient::class.java)
        return mComicsService.getAllFromCharacter(id, limit, noVariants)
    }

}
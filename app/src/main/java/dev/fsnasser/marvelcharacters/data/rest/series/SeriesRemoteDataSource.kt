package dev.fsnasser.marvelcharacters.data.rest.series

import dev.fsnasser.marvelcharacters.data.entities.ComicSerieApi
import dev.fsnasser.marvelcharacters.utils.data.RetrofitServiceGenerator
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

class SeriesRemoteDataSource @Inject constructor(private val service: RetrofitServiceGenerator) {

    fun getAllFromCharacter(id: Long, limit: Int)
            : Observable<Response<ComicSerieApi>> {
        val mSeriesService = service.createService(SeriesRestClient::class.java)
        return mSeriesService.getAllFromCharacter(id, limit)
    }

}
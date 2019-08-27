package dev.fsnasser.marvelcharacters.data.repositories

import dev.fsnasser.marvelcharacters.data.entities.ComicSerieApi
import dev.fsnasser.marvelcharacters.data.rest.series.SeriesRemoteDataSource
import dev.fsnasser.marvelcharacters.utils.data.Resource
import dev.fsnasser.marvelcharacters.utils.helpers.NetworkHelper
import io.reactivex.Observable
import javax.inject.Inject

class SeriesRepository @Inject constructor(
    private val mNetworkHelper: NetworkHelper,
    private val mRemoteDataSource: SeriesRemoteDataSource
) {

    fun getAllFromCharacter(id: Long, limit: Int) : Observable<Resource<ComicSerieApi>> {
        if(mNetworkHelper.isConnectedToInternet) {
            return mRemoteDataSource.getAllFromCharacter(id, limit).flatMap {
                val body = it.body()

                val response = when(it.code()) {
                    200 -> {
                        if(body?.data?.results != null && body.data.results.isNotEmpty()) {
                            Resource.success(body)
                        } else Resource.noData()
                    }
                    else -> Resource.error()
                }

                return@flatMap Observable.just(response)
            }
        } else return Observable.just(Resource.notConnected())
    }

}
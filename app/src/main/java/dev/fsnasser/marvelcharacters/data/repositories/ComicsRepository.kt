package dev.fsnasser.marvelcharacters.data.repositories

import dev.fsnasser.marvelcharacters.data.entities.ComicSerieApi
import dev.fsnasser.marvelcharacters.data.rest.comics.ComicsRemoteDataSource
import dev.fsnasser.marvelcharacters.utils.data.Resource
import dev.fsnasser.marvelcharacters.utils.helpers.NetworkHelper
import io.reactivex.Observable
import javax.inject.Inject

class ComicsRepository @Inject constructor(
    private val mNetworkHelper: NetworkHelper,
    private val mRemoteDataSource: ComicsRemoteDataSource
) {

    fun getAllFromCharacter(id: Long, limit: Int, noVariants: Boolean) : Observable<Resource<ComicSerieApi>> {
        if(mNetworkHelper.isConnectedToInternet) {
            return mRemoteDataSource.getAllFromCharacter(id, limit, noVariants).flatMap {
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
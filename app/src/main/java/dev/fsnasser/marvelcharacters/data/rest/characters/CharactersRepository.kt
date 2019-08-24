package dev.fsnasser.marvelcharacters.data.rest.characters

import dev.fsnasser.marvelcharacters.data.entities.CharacterApi
import dev.fsnasser.marvelcharacters.utils.data.Resource
import dev.fsnasser.marvelcharacters.utils.helpers.NetworkHelper
import io.reactivex.Observable
import javax.inject.Inject

class CharactersRepository @Inject constructor(
    private val mNetworkHelper: NetworkHelper,
    private val mRemoteDataSource: CharactersRemoteDataSource
) {

    fun getAll(offset: Int, limit: Int, nameStartsWith: String?, orderBy: String)
            : Observable<Resource<CharacterApi>> {
        if(mNetworkHelper.isConnectedToInternet) {
            return mRemoteDataSource.getAll(offset, limit, nameStartsWith, orderBy).flatMap {
                val body = it.body()

                val response = when(it.code()) {
                    200 -> {
                        if(body?.data?.results != null && body.data.results.isNotEmpty()) {
                            Resource.success(body)
                        } else if(nameStartsWith != null) {
                            Resource.noData()
                        } else Resource.error()
                    }
                    else -> Resource.error()
                }

                return@flatMap Observable.just(response)
            }
        } else return Observable.just(Resource.notConnected())
    }

}
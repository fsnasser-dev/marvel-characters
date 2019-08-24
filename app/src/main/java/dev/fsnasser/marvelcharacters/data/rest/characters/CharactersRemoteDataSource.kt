package dev.fsnasser.marvelcharacters.data.rest.characters

import dev.fsnasser.marvelcharacters.data.entities.CharacterApi
import dev.fsnasser.marvelcharacters.utils.data.RetrofitServiceGenerator
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

class CharactersRemoteDataSource @Inject constructor(private val service: RetrofitServiceGenerator) {

    fun getAll(offset: Int, limit: Int, nameStartsWith: String?, orderBy: String)
            : Observable<Response<CharacterApi>> {
        val mCharactersService = service.createService(CharactersRestClient::class.java)
        return mCharactersService.getAll(offset, limit, nameStartsWith, orderBy)
    }

}
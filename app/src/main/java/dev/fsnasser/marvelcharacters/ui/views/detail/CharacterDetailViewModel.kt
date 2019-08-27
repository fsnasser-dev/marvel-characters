package dev.fsnasser.marvelcharacters.ui.views.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.fsnasser.marvelcharacters.data.entities.ComicSerieApi
import dev.fsnasser.marvelcharacters.data.repositories.CharactersRepository
import dev.fsnasser.marvelcharacters.data.repositories.ComicsRepository
import dev.fsnasser.marvelcharacters.data.repositories.SeriesRepository
import dev.fsnasser.marvelcharacters.ui.entities.Character
import dev.fsnasser.marvelcharacters.utils.data.Resource
import dev.fsnasser.marvelcharacters.utils.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CharacterDetailViewModel @Inject constructor(
    private val mComicsRepository: ComicsRepository,
    private val mSeriesRepository: SeriesRepository,
    private val mCharactersRepository: CharactersRepository
) : ViewModel() {

    val comicsStatus = ObservableField<Resource.Status>()

    val seriesStatus = ObservableField<Resource.Status>()

    val comics = MutableLiveData<List<Character.ComicSerie>>()

    val series = MutableLiveData<List<Character.ComicSerie>>()

    private val mCompositeDisposable = CompositeDisposable()

    fun getAllComicsFromCharacter(id: Long, limit: Int, noVariants: Boolean = true) {
        comicsStatus.set(Resource.Status.LOADING)

        mCompositeDisposable += mComicsRepository
            .getAllFromCharacter(id, limit, noVariants)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Resource<ComicSerieApi>>() {

                override fun onNext(data: Resource<ComicSerieApi>) {
                    val dataStatus = data.status
                    comicsStatus.set(dataStatus)

                    if(dataStatus == Resource.Status.SUCCESS) {
                        comics.value = convertToComicSerie(data.response?.data?.results)
                    }
                }

                override fun onError(e: Throwable) {
                    comicsStatus.set(Resource.Status.ERROR)
                }

                override fun onComplete() {}
            })
    }

    fun getAllSeriesFromCharacter(id: Long, limit: Int) {
        seriesStatus.set(Resource.Status.LOADING)

        mCompositeDisposable += mSeriesRepository
            .getAllFromCharacter(id, limit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Resource<ComicSerieApi>>() {

                override fun onNext(data: Resource<ComicSerieApi>) {
                    val dataStatus = data.status
                    seriesStatus.set(dataStatus)

                    if(dataStatus == Resource.Status.SUCCESS) {
                        series.value = convertToComicSerie(data.response?.data?.results)
                    }
                }

                override fun onError(e: Throwable) {
                    seriesStatus.set(Resource.Status.ERROR)
                }

                override fun onComplete() {}
            })
    }

    fun addToFavourites(character: Character) = mCharactersRepository.insertCharacterIntoFavourites(character)

    fun removeFromFavourites(character: Character) = mCharactersRepository.removeCharacterFromFavourites(character)

    private fun convertToComicSerie(results: List<ComicSerieApi.Data.Result>?) : List<Character.ComicSerie> {
        val comics = ArrayList<Character.ComicSerie>()
        results?.let {
            for(result in it) {
                comics.add(Character.ComicSerie(result.title, result.thumbnail.path,
                    result.thumbnail.extension))
            }
        }
        return comics
    }

    override fun onCleared() {
        super.onCleared()

        if(!mCompositeDisposable.isDisposed) mCompositeDisposable.dispose()
    }

}
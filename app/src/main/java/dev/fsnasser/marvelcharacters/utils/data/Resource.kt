package dev.fsnasser.marvelcharacters.utils.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class Resource<out T> private constructor (
    @NonNull val status: Status,
    @Nullable val response: T? = null) {

    companion object {
        fun <T> success(response: T? = null): Resource<T> = Resource(Status.SUCCESS, response)

        fun <T> noData(): Resource<T> = Resource(Status.NO_DATA)

        fun <T> error(response: T? = null): Resource<T> = Resource(Status.ERROR, response)

        fun <T> notConnected(): Resource<T> = Resource(Status.NOT_CONNECTED)
    }

    enum class Status {
        SUCCESS, NO_DATA, ERROR, LOADING, REFRESHING, NOT_CONNECTED
    }

}


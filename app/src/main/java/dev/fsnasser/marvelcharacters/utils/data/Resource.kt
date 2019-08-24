package dev.fsnasser.marvelcharacters.utils.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable

class Resource<out T> private constructor (
    @NonNull val status: Status,
    @Nullable val response: T? = null) {

    companion object {
        fun <T> success(response: T? = null): Resource<T> = Resource(Status.SUCCESS, response)

        fun <T> noData(): Resource<T> = Resource(Status.NO_DATA)

        fun <T> unauthorized(response: T? = null): Resource<T> = Resource(Status.UNAUTHORIZED, response)

        fun <T> conflict(): Resource<T> = Resource(Status.CONFLICT)

        fun <T> error(response: T? = null): Resource<T> = Resource(Status.ERROR, response)

        fun <T> notConnected(): Resource<T> = Resource(Status.NOT_CONNECTED)

        fun <T> badRequest(response: T? = null): Resource<T> = Resource(Status.BAD_REQUEST, response)
    }

    enum class Status {
        SUCCESS, NO_DATA, UNAUTHORIZED, CONFLICT, ERROR, BAD_REQUEST, LOADING, REFRESHING, LOADING_MORE, NOT_CONNECTED, CLOSED
    }

}


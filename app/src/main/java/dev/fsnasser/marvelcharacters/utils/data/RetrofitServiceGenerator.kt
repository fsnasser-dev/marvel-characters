package dev.fsnasser.marvelcharacters.utils.data

import dev.fsnasser.marvelcharacters.BuildConfig
import dev.fsnasser.marvelcharacters.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RetrofitServiceGenerator @Inject constructor() {

    private val mRetrofitBuilder = Retrofit.Builder()
        .baseUrl(AppConstants.MARVEL_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    private val mHttpClientBuilder = getBuilder()

    fun <S> createService(serviceClass: Class<S>, url: String? = null): S {
        url?.let {
            mRetrofitBuilder.baseUrl(it)
        }

        mHttpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()
            val timestamp = Timestamp(System.currentTimeMillis()).time.toString()
            val originalUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("ts", timestamp)
                .addQueryParameter("apikey", BuildConfig.MARVEL_PUBLIC_API_KEY)
                .addQueryParameter("hash", generateHash(timestamp))
                .build()

            val requestBuilder = original.newBuilder().url(originalUrl)
            requestBuilder.method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val client = mHttpClientBuilder
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = mRetrofitBuilder.client(client).build()

        return retrofit.create(serviceClass)
    }

    private fun getBuilder() : OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor {
            val original: Request = it.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.method(original.method(), original.body())

            val request = requestBuilder.build()
            it.proceed(request)
        }

        if(BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }

        return builder
    }

    private fun generateHash(timestamp: String)
            : String {
        val hash = timestamp + BuildConfig.MARVEL_PUBLIC_API_KEY +
                BuildConfig.MARVEL_PRIVATE_API_KEY

        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(hash.toByteArray(), 0, hash.length)
        return BigInteger(1, messageDigest.digest()).toString(16)
    }



}
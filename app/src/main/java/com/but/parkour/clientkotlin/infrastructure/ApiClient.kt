package com.but.parkour.clientkotlin.infrastructure

import com.but.parkour.clientkotlin.auth.HttpBearerAuth

import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.CallAdapter
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.squareup.moshi.Moshi
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call as CallRetrofit


class ApiClient(
    private var baseUrl: String = defaultBasePath,
    private val okHttpClientBuilder: OkHttpClient.Builder? = null,
    private val serializerBuilder: Moshi.Builder = Moshi.Builder()
        .add(BooleanAdapter())
        .add(CompetitionCreateGenderAdapter())
    ,
    private val callFactory: Call.Factory? = null,
    private val callAdapterFactories: List<CallAdapter.Factory> = listOf(),
    private val converterFactories: List<Converter.Factory> = listOf(
        ScalarsConverterFactory.create(),
        MoshiConverterFactory.create(serializerBuilder.build())
    )
) {
    private val apiAuthorizations = mutableMapOf<String, Interceptor>()
    var logger: ((String) -> Unit)? = null

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .apply {
                callAdapterFactories.forEach {
                    addCallAdapterFactory(it)
                }
            }
            .apply {
                converterFactories.forEach {
                    addConverterFactory(it)
                }
            }
    }

    private val clientBuilder: OkHttpClient.Builder by lazy {
        okHttpClientBuilder ?: defaultClientBuilder
    }

    private val defaultClientBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor { message -> logger?.invoke(message) }
                .apply { level = HttpLoggingInterceptor.Level.BODY }
            )
    }

    init {
        normalizeBaseUrl()
    }

    constructor(
        baseUrl: String = defaultBasePath,
        okHttpClientBuilder: OkHttpClient.Builder? = null,
        serializerBuilder: Moshi.Builder = Serializer.moshiBuilder,
        authNames: Array<String>
    ) : this(baseUrl, okHttpClientBuilder, serializerBuilder) {
        authNames.forEach { authName ->
            val auth: Interceptor? = when (authName) {
                "bearerAuth" -> HttpBearerAuth("bearer")

                else -> throw RuntimeException("auth name $authName not found in available auth names")
            }
            if (auth != null) {
                addAuthorization(authName, auth)
            }
        }
    }

    constructor(
        baseUrl: String = defaultBasePath,
        okHttpClientBuilder: OkHttpClient.Builder? = null,
        serializerBuilder: Moshi.Builder = Serializer.moshiBuilder,
        authName: String = "bearerAuth",
        bearerToken: String
    ) : this(baseUrl, okHttpClientBuilder, serializerBuilder, arrayOf(authName)) {
        setBearerToken(bearerToken)
    }

    fun setBearerToken(bearerToken: String): ApiClient {
        apiAuthorizations.values.runOnFirst<Interceptor, HttpBearerAuth> {
            this.bearerToken = bearerToken
        }
        return this
    }

    /**
     * Adds an authorization to be used by the client
     * @param authName Authentication name
     * @param authorization Authorization interceptor
     * @return ApiClient
     */
    fun addAuthorization(authName: String, authorization: Interceptor): ApiClient {
        if (apiAuthorizations.containsKey(authName)) {
            throw RuntimeException("auth name $authName already in api authorizations")
        }
        apiAuthorizations[authName] = authorization
        clientBuilder.addInterceptor(authorization)
        return this
    }

    fun setLogger(logger: (String) -> Unit): ApiClient {
        this.logger = logger
        return this
    }

    fun <S> createService(serviceClass: Class<S>): S {
        val usedCallFactory = this.callFactory ?: clientBuilder.build()
        return retrofitBuilder.callFactory(usedCallFactory).build().create(serviceClass)
    }

    private fun normalizeBaseUrl() {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/"
        }
    }

    private inline fun <T, reified U> Iterable<T>.runOnFirst(callback: U.() -> Unit) {
        for (element in this) {
            if (element is U) {
                callback.invoke(element)
                break
            }
        }
    }

    companion object {
        @JvmStatic
        protected val baseUrlKey: String = "org.openapitools.client.baseUrl"

        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty(baseUrlKey, "http://92.222.217.100")
        }
    }

    fun <T> fetchData(
        call: CallRetrofit<T>,
        onSuccess: (T?) -> Unit,
        onError: (String) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: CallRetrofit<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onError("Erreur: ${response.message()}")
                }
            }

            override fun onFailure(call: CallRetrofit<T>, t: Throwable) {
                onError("Erreur : ${t.message}")
            }
        })
    }

    fun <T> fetchData(
        call: CallRetrofit<T>,
        onSuccess: (T?, Int) -> Unit,
        onError: (String, Int?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: CallRetrofit<T>, response: Response<T>) {
                val statusCode = response.code()
                if (response.isSuccessful) {
                    onSuccess(response.body(), statusCode)
                } else {
                    onError("Erreur: ${response.message()}", statusCode)
                }
            }

            override fun onFailure(call: CallRetrofit<T>, t: Throwable) {
                onError("Erreur : ${t.message}", null)
            }
        })
    }
}




package ru.komcity.mobile.network

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.komcity.mobile.BuildConfig
import ru.komcity.mobile.common.Constants
import java.io.IOException
import java.util.*

class ApiNetwork {

    val api: ApiMethods = getRetrofit().create(ApiMethods::class.java)

    private fun getRetrofit(): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        retrofitBuilder.client(getOkHttpClient(ArrayList<Interceptor>().apply {
            if (BuildConfig.DEBUG) {
                add(interceptorLogging)
            }
            add(interceptorAuthorizationParam)
        }))
        return retrofitBuilder.build()
    }

//    fun getErrorConverter() : Converter<ResponseBody, BaseNetError> {
//        val retrofit = getRetrofit()
//        return retrofit.responseBodyConverter<BaseNetError>(BaseNetError::class.java, arrayOf(object: Annotation {}))
//    }

    private val interceptorAuthorizationParam: Interceptor
        get() = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .method(original.method, original.body)
                        .build()
                return chain.proceed(request)
            }
        }

    private val interceptorLogging: HttpLoggingInterceptor
        get() {
            val loggingBody = HttpLoggingInterceptor()
            loggingBody.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

            return loggingBody
        }

    private fun getOkHttpClient(interceptorItems: List<Interceptor>): OkHttpClient = OkHttpClient.Builder().apply {
        interceptorItems.forEach { item ->
            addInterceptor(item)
        }
    }.build()
}

package com.example.footballleague.data.api

import com.example.footballleague.data.model.competitions.CompetitionsListResponse
import com.example.footballleague.utils.BASE_URL
import com.example.footballleague.utils.ServerTimeOut
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface RetrofitService {
    companion object {

        var retrofitService: RetrofitService? = null

        fun getInstance(): RetrofitService {
            val interceptor = HttpLoggingInterceptor();
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .writeTimeout(ServerTimeOut.toLong(), TimeUnit.SECONDS)
                .readTimeout(ServerTimeOut.toLong(), TimeUnit.SECONDS)
                .connectTimeout(ServerTimeOut.toLong(), TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }

    @GET("competitions/")
    suspend fun getCompetitionsList(): Response<CompetitionsListResponse>

}
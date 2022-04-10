package com.app.myvotingforregistration.retrofit

import com.annisa.evoteing.retrofit.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://voting.kpupangkep.my.id/api/"
//    private val client = OkHttpClient.Builder().build()

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
            .build()

        retrofit.create(Api::class.java)
    }
}
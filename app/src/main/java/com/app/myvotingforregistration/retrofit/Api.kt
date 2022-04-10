package com.annisa.evoteing.retrofit

import com.app.myvotingforregistration.retrofit.pemilih.ModelResultPemilihData
import com.app.myvotingforregistration.retrofit.voter.VoterRequest
import com.app.myvotingforregistration.retrofit.voter.VoterResponse
import retrofit2.Call
import retrofit2.http.*


interface Api {
    @GET("pemilih")
    fun getPemilih(): Call<ModelResultPemilihData>

    @POST("voter")
    fun postVoter(
        @Body req: VoterRequest?,
    ): Call<VoterResponse>
}
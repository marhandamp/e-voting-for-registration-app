package com.app.myvotingforregistration.retrofit.pemilih

import com.google.gson.annotations.SerializedName

class ModelResultPemilihData {
    @SerializedName("data")
    lateinit var modelResultPemilih: List<ModelResultPemilih>
}
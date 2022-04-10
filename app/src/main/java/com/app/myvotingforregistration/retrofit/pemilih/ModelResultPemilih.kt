package com.app.myvotingforregistration.retrofit.pemilih

import com.google.gson.annotations.SerializedName


class ModelResultPemilih {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("nama_pemilih")
    lateinit var namaPemilih: String

    @SerializedName("address")
    lateinit var address: String

    @SerializedName("gender")
    lateinit var gender: String

    @SerializedName("no_tps")
    lateinit var noTps: String

    @SerializedName("deleted_at")
    lateinit var deleted_at: String

    @SerializedName("created_at")
    lateinit var created_at: String

    @SerializedName("updated_at")
    lateinit var updated_at: String

    @SerializedName("id_nfc")
    lateinit var idNfc: String
}
package com.app.myvotingforregistration.retrofit.voter

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class VoterResponse {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("nama_pemilih")
    @Expose
    var namaPemilih: String? = null

    @SerializedName("address")
    @Expose
    var alamat: String? = null

    @SerializedName("gender")
    @Expose
    var jenisKelamin: String? = null

    @SerializedName("no_tps")
    @Expose
    var noTps: String? = null

    @SerializedName("id_nfc")
    @Expose
    var idNfc: String? = null

    @SerializedName("deleted_at")
    @Expose
    var deletedAt: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}


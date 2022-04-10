package com.app.myvotingforregistration.retrofit.voter

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VoterRequest{
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
}

package com.app.myvotingforregistration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.app.myvotingforregistration.retrofit.RetrofitClient
import com.app.myvotingforregistration.databinding.ActivityFormBinding
import com.app.myvotingforregistration.retrofit.voter.VoterRequest
import com.app.myvotingforregistration.retrofit.voter.VoterResponse
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, gender)
        binding.actvJenisKelamin.setAdapter(arrayAdapter)

        val id = intent.getStringExtra("ID")

        binding.btnSelesai.setOnClickListener {
            val nama = binding.tiEdtNama.text.toString().trim()
            val alamat = binding.tiEdtAlamat.text.toString().trim()
            val jenisKelamin = binding.actvJenisKelamin.text.toString().trim()
            val noTps = binding.tiEdtNoTps.text.toString().trim()

            postVoter(nama, alamat, jenisKelamin, noTps, id)
        }
    }

    private fun postVoter(
        nama: String,
        alamat: String,
        jenisKelamin: String,
        noTps: String,
        id: String?
    ) {
        val voterRequest = VoterRequest()
        voterRequest.namaPemilih = nama
        voterRequest.alamat = alamat
        voterRequest.jenisKelamin = jenisKelamin
        voterRequest.noTps = noTps
        voterRequest.idNfc = id

        RetrofitClient.instance.postVoter(voterRequest).enqueue(object : Callback<VoterResponse> {
            override fun onResponse(call: Call<VoterResponse>, response: Response<VoterResponse>) {
                if (response.isSuccessful){

                    val intent = Intent(this@FormActivity, ScanNfcActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                    Toast.makeText(this@FormActivity, "Berhasil Mendaftarkan KTP", Toast.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Anda Yang Tidak Beres", Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VoterResponse>, t: Throwable) {
                Snackbar.make(binding.root, t.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
package com.app.myvotingforregistration

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.app.myvotingforregistration.retrofit.RetrofitClient
import com.app.myvotingforregistration.databinding.ActivityScanNfcBinding
import com.app.myvotingforregistration.parser.NdefMassageParser
import com.app.myvotingforregistration.retrofit.pemilih.ModelResultPemilihData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.experimental.and

class ScanNfcActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanNfcBinding
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanNfcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        PendingIntent.getActivity(
            this, 0,
            Intent(this, this.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        ).also { pendingIntent = it
        }
    }

    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null) {
            if (!nfcAdapter!!.isEnabled) showWirelessSettings()
            nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)
        }
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) {
            nfcAdapter!!.disableForegroundDispatch(this)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveIntent(intent)
    }

    private fun resolveIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val msgs: Array<NdefMessage?>
            if (rawMsgs != null) {
                msgs = arrayOfNulls(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                }
            } else {
                val empty = ByteArray(0)
                val id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
                val tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
//                val payload = toMex(tag!!.id.toString().toByteArray()).toByteArray()
                val payload = dumpTagData(tag).toByteArray()
                val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload)
                val msg = NdefMessage(arrayOf(record))
                msgs = arrayOf(msg)
                Log.d("abcd", "resolveIntent: ${id.toString()}")
                Log.d("abcd", "msgs: $msgs")

            }
            displayMsgs(msgs)
        }
    }

    private fun displayMsgs(msgs: Array<NdefMessage?>?) {
        if (msgs == null || msgs.isEmpty()) return
        val builder = StringBuilder()
        val records = NdefMassageParser.parse(msgs[0])
        Log.d("ica", "displayMsgs nilai = ${records[0].str()}")


        val size = records.size
        for (i in 0 until size) {
            val record = records[i]
            val str = record.str()
            builder.append(str).append('\n')
        }

//        saveData(builder.toString())
        val a = builder.toString()
        val data = a.split(" ")
        val data3 = data[0]+data[1]+data[2]+data[3]+data[4]+data[5]
//        moveIntent(data3)
        getPemilih(data3)
    }

    private fun getPemilih(data3: String) {
        RetrofitClient.instance.getPemilih().enqueue(object : Callback<ModelResultPemilihData> {
            override fun onResponse(
                call: Call<ModelResultPemilihData>,
                response: Response<ModelResultPemilihData>
            ) {
                Log.d("Ica-idNfc", data3)
                var idNfc = ""

                val size = response.body()?.modelResultPemilih!!.size
                for (i in 0 until size){
                    val data = response.body()!!.modelResultPemilih[i].idNfc
                    if (data == data3){
                        idNfc = response.body()!!.modelResultPemilih[i].idNfc
                    }
                }

                if (data3 == idNfc){
                    Toast.makeText(this@ScanNfcActivity, "KTP Sudah Terdaftar", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this@ScanNfcActivity, FormActivity::class.java)
                    intent.putExtra("ID", data3)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ModelResultPemilihData>, t: Throwable) {
                Toast.makeText(this@ScanNfcActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showWirelessSettings() {
        Toast.makeText(this, "You Need to enable NFC", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(intent)
    }

    private fun dumpTagData(tag: Tag?): String {
        val sb = StringBuilder()
//        sb.append("ID (hex):").append(toMex(id)).append('\n')
        val id = tag!!.id
        sb.append(toMex(id))
        sb.append(toReversedDec(id))
        sb.append(toDec(id))
        sb.append(toReversedDec(id))
//        sb.append("ID (hex):").append(toMex(id)).append('\n')
//        sb.append("ID (reversed hex):").append(toReversedDec(id)).append('\n')
//        sb.append("ID (doc):").append(toDec(id)).append('\n')
//        sb.append("ID (reversed dec):").append(toReversedDec(id)).append('\n')
//        val prefix = "android.nfc.tech"
//        sb.append("Technologies:")
//        for (tech in tag.techList) {
//            sb.append(tech.substring(prefix.length))
//            sb.append(", ")
//        }
        sb.delete(sb.length - 2, sb.length)
        for (tech in tag.techList) {
            if (tech == MifareClassic::class.java.name) {
                sb.append('\n')
                var type = "Unknown"
                try {
                    val mifareTag = MifareClassic.get(tag)
                    when (mifareTag.type) {
                        MifareClassic.TYPE_CLASSIC -> type = "Classic"
                        MifareClassic.TYPE_PLUS -> type = "Plus"
                        MifareClassic.TYPE_PRO -> type = "Pro"
                    }
                    sb.append("Mifare Classic type: ")
                    sb.append(type)
                    sb.append('\n')
                    sb.append("Mifare size: ")
                    sb.append(mifareTag.size.toString() + "bytes")
                    sb.append('\n')
                    sb.append("Mifare sectors: ")
                    sb.append(mifareTag.sectorCount)
                    sb.append('\n')
                    sb.append("Mifare block: ")
                    sb.append(mifareTag.blockCount)
                    sb.append('\n')
                } catch (e: Exception) {
                    sb.append("Mifare classic error: " + e.message)
                }
            }
            if (tech == MifareUltralight::class.java.name) {
                sb.append('\n')
                val miFareUlTag = MifareUltralight.get(tag)
                var type = "Unknown"
                when (miFareUlTag.type) {
                    MifareUltralight.TYPE_ULTRALIGHT -> type = "UltraLight"
                    MifareUltralight.TYPE_ULTRALIGHT_C -> type = "UltraLight C"
                }
                sb.append("Mifare UltraLight type: ")
                sb.append(type)
            }
        }
        return sb.toString()
    }

    private fun toMex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            if (i > 0) {
                sb.append(" ")
            }
            val b: Int = (bytes[i] and 0xff.toByte()).toInt()
            if (b > 0x10) sb.append('0')
            sb.append(Integer.toHexString(b))
        }
        return sb.toString()
    }

    private fun toDec(bytes: ByteArray): Long {
        var result: Long = 0
        var factor: Long = 1
        for (i in bytes.indices) {
            val value: Long = (bytes[i] and 0xffL.toByte()).toLong()
            result += value * factor
            factor *= 256L
        }
        return result
    }

    private fun toReversedDec(bytes: ByteArray): Long {
        var result: Long = 0
        var factor: Long = 1
        for (i in bytes.indices.reversed()) {
            val value: Long = (bytes[i] and 0xffL.toByte()).toLong()
            result += value * factor
            factor *= 256L
        }
        return result
    }
}
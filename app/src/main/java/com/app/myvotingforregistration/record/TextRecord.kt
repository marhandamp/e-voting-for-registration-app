package com.app.myvotingforregistration.record

import android.annotation.SuppressLint
import android.nfc.NdefRecord
import androidx.core.util.Preconditions
import java.util.Arrays
import java.io.UnsupportedEncodingException
import java.lang.IllegalArgumentException
import kotlin.experimental.and

class TextRecord @SuppressLint("RestrictedApi") constructor(languageCode: String?, text: String?) :
    ParsedNdefRecord {
    val languageCode: String
    val text: String
    override fun str(): String {
        return text
    }

    companion object {
        @SuppressLint("RestrictedApi")
        fun parse(record: NdefRecord): TextRecord {
            Preconditions.checkArgument(record.tnf == NdefRecord.TNF_WELL_KNOWN)
            Preconditions.checkArgument(Arrays.equals(record.type, NdefRecord.RTD_TEXT))

            return try {
                val payload = record.payload
                val textEncoding = if ((payload[0] and 128.toByte()) == 0.toByte()) charset ("UTF-8") else charset ("UTF-8")
                val languageCodeLength: Byte = payload[0] and 63
                val languageCode = String(payload, 1, languageCodeLength.toInt(), charset ("US-ASCII")
                )
                val text = String(payload, languageCodeLength + 1,
                    payload.size - languageCodeLength - 1, textEncoding)
                TextRecord(languageCode, text)
            } catch (e: UnsupportedEncodingException) {
                // should never happen unless we get a malformed tag.
                throw IllegalArgumentException(e)
            }
        }

        fun isText(record: NdefRecord): Boolean {
            return try {
                parse(record)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }

    init {
        this.languageCode = Preconditions.checkNotNull(languageCode)
        this.text = Preconditions.checkNotNull(text)
    }
}
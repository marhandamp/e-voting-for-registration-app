package com.app.myvotingforregistration.parser

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import com.app.myvotingforregistration.record.TextRecord
import com.app.myvotingforregistration.record.ParsedNdefRecord
import java.util.ArrayList


class NdefMassageParser {
    private fun NdefMessageParser() {}

    companion object {
        fun parse(message: NdefMessage?): List<ParsedNdefRecord> {
            return getRecords(message!!.records)
        }

        fun getRecords(records: Array<NdefRecord>): List<ParsedNdefRecord> {
            val elements: MutableList<ParsedNdefRecord> = ArrayList()
            for (record in records) {
                if (TextRecord.isText(record)) {
                    elements.add(TextRecord.parse(record))
                } else {
                    elements.add(object : ParsedNdefRecord {
                        override fun str(): String? {
                            return String(record.payload)
                        }
                    })
                }
            }
            return elements
        }
    }
}
package com.example.model

import android.location.Address
import java.text.SimpleDateFormat
import java.util.*

class LocationHistory {
    var address: Address? = null
    var date: String = ""

    constructor(address: Address) {
        this.address = address
        this.date = convertLongToTime(currentTimeToLong())
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return format.format(date)
    }

    private fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return df.parse(date).time
    }

    fun formatAddress(): String {

        return StringBuilder()
            .append(address?.getAddressLine(0))
            .toString()
    }
}
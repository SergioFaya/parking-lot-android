package com.example.model

import android.location.Address
import java.text.SimpleDateFormat
import java.util.*

class LocationHistory {
    var address: String = ""
    var date: String = ""

    constructor() {
        
    }

    constructor(address: Address) {
        this.address = address?.getAddressLine(0).toString()
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

}
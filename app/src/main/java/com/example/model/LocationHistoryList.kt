package com.example.model

import com.google.gson.Gson

class LocationHistoryList {

    var locationHistoryList: ArrayList<LocationHistory>? = null

    var nif: String = ""

    fun serialize(): String {
        val gson = Gson()
        val json = gson.toJson(this)
        return json
    }

    fun deserialize(json: String) {
        val gson = Gson()
        val list: LocationHistoryList =
            gson.fromJson<LocationHistoryList>(json, LocationHistoryList::class.java)
        locationHistoryList = list.locationHistoryList
    }


}
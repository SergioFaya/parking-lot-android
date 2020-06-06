package com.example.model

import com.google.gson.Gson

class ParkingLotForm(
    var email: String,
    var nif: String,
    var phone: String,
    var surname: String,
    var username: String,
    var password: String
) {
    constructor() : this("", "", "", "", "", "")

    fun serialize(): String {
        val gson = Gson()
        val json = gson.toJson(this)
        return json
    }

    fun deserialize(json: String?) {
        if (json != null) {
            val gson = Gson()
            val form: ParkingLotForm =
                gson.fromJson<ParkingLotForm>(json, ParkingLotForm::class.java)
            this.apply {
                username = form.username
                surname = form.surname
                email = form.email
                phone = form.phone
                nif = form.nif.toUpperCase()
                password = form.password
            }
        }
    }
}
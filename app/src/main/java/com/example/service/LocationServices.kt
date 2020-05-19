package com.example.service

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

internal object LocationServices {

    fun reverseGeocoding(context: Context, latlng: LatLng): Address {
        val geocoder = Geocoder(context)

        val adresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)

        return adresses.get(0)
    }

    fun locationToLatLng(currentLocation: Location): LatLng {
        return LatLng(currentLocation.latitude, currentLocation.longitude)
    }

    fun serializeLatLng(latLng: LatLng): String {
        return Gson().toJson(latLng)
    }

    fun deserializeLatLng(latLng: String): LatLng {
        return Gson().fromJson<LatLng>(latLng, LatLng::class.java)
    }

}
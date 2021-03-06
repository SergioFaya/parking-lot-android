package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.firebase.DbManager
import com.example.model.LocationHistory
import com.example.model.LocationHistoryList
import com.example.model.ParkingLotForm
import com.example.model.enum.Keys
import com.example.service.LocationServices.deserializeLatLng
import com.example.service.LocationServices.locationToLatLng
import com.example.service.LocationServices.reverseGeocoding
import com.example.service.LocationServices.serializeLatLng
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_user_car_location_placeholder.*

class UserCarLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val PERMISSION_ID = 101;

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null;
    private var vehicleMarker: Marker? = null

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_car_location_placeholder)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnStoreVehicleLocation.setOnClickListener({ manageClickStoreLocation() })
        btnLastPosition.setOnClickListener({ displayLastPosition() })
        btnLocationHistory.setOnClickListener({ displayLocationHistory() })

    }

    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    }
                    currentLocation = location
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude
                            ), 18.0f
                        )
                    )
                }
            } else {
                Toast.makeText(this, "Enciende la ubicación del movil", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Enciende la ubicación del movil", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getCurrentLocation();
        mMap.isMyLocationEnabled = true
    }


    private fun manageClickStoreLocation() {
        placeMarkerInLocation(locationToLatLng(currentLocation!!))
        var locationHistoryList =
            storeLocationInSharedPreferences(locationToLatLng(currentLocation!!))
        storeLocationInFirebase(locationHistoryList)
    }

    private fun placeMarkerInLocation(latLng: LatLng?) {
        vehicleMarker?.remove()
        val latLngLocation = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        vehicleMarker = mMap.addMarker(
            MarkerOptions().position(latLngLocation).title(
                getString(R.string.your_vehicle)
            )
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngLocation, 18.0f))
    }


    private fun storeLocationInFirebase(list: LocationHistoryList) {
        val sharedPreferences = getSharedPreferences(Keys.USER_FORM.value, Context.MODE_PRIVATE)
        val json: String? = sharedPreferences.getString(Keys.USER_SHARED_PREFERENCES.value, null)

        val form = ParkingLotForm()
        if (json != null) {
            form.deserialize(json)
        }
        DbManager.updateUserLocations(form.nif, list)
    }

    private fun storeLocationInSharedPreferences(latLng: LatLng): LocationHistoryList {
        getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
            .edit()
            .putString(Keys.CURRENT_LOCATION.value, serializeLatLng(latLng))
            .apply()

        var address = reverseGeocoding(this, latLng)
        var currentHistory = LocationHistory(address)


        var listHistory = getDataList()
        listHistory.add(currentHistory)

        var locationHistoryList = LocationHistoryList()
        locationHistoryList.locationHistoryList = listHistory
        var json = locationHistoryList.serialize()

        getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE).edit()
            .putString(Keys.LOCATION_HISTORY.value, json).commit();
        return locationHistoryList
    }

    fun getDataList(): ArrayList<LocationHistory> {
        val jsonHistoryList =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
                ?.getString(Keys.LOCATION_HISTORY.value, null)

        if (jsonHistoryList != null) {
            val list = LocationHistoryList()
            list.deserialize(jsonHistoryList)

            if (list != null) {
                return list.locationHistoryList!!
            }
        }

        return ArrayList<LocationHistory>()
    }

    private fun displayLastPosition() {
        val latLng =
            getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
                .getString(Keys.CURRENT_LOCATION.value, null)
        if (latLng != null) {
            placeMarkerInLocation(deserializeLatLng(latLng))
        } else {
            Toast.makeText(
                baseContext,
                getString(R.string.vehicle_not_saved),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun displayLocationHistory() {
        val intent = Intent(this, LocationHistoryActivity::class.java)
        startActivity(intent)
    }


}
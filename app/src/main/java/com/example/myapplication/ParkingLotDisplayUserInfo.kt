package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.model.ParkingLotForm
import com.example.model.enum.Keys
import kotlinx.android.synthetic.main.activity_parking_lot_display_user_info.*
import kotlinx.android.synthetic.main.content_parking_lot_display_user_info.*


class ParkingLotDisplayUserInfo : AppCompatActivity() {

    private val PERMISSION_ID = 101;

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_lot_display_user_info)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        loadUserInfo()
        configureEvents()
    }

    override fun onRestart() {
        super.onRestart()
        loadUserInfo()
    }

    override fun onResume() {
        super.onResume()
        loadUserInfo()
    }


    private fun loadUserInfo() {
        val sharedPreferences = getSharedPreferences(Keys.USER_FORM.value, Context.MODE_PRIVATE)
        val json: String? = sharedPreferences.getString(Keys.USER_SHARED_PREFERENCES.value, null)

        val form = ParkingLotForm()
        if (json != null) {
            form.deserialize(json)
            displayInfo(form, true)
        } else {
            displayInfo(form, false)
        }
    }

    private fun displayInfo(form: ParkingLotForm, isUserEdited: Boolean) {
        if (isUserEdited) {
            val stringBuilder = StringBuilder()
            form.apply {
                userInfoName.text = "Usuario: " + surname + ", " + username
                userInfoEmail.text = "Email: " + email
                userInfoNif.text = "Nif: " + nif
                userInfoPhone.text = "Teléfono: " + phone
            }
            fillUserInfoAdvice.visibility = View.GONE
        } else {
            fillUserInfoAdvice.visibility = View.VISIBLE
        }
    }

    private fun configureEvents() {
        val intent = Intent(this, ParkingLotEditUserInfo::class.java)
        btnCircleEdit.setOnClickListener { view ->
            startActivity(intent)
        }

        btnDisplayMap.setOnClickListener({ requestPermissions() })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_ID -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(this, UserCarLocationActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Enciende la ubicación del movil", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }
}

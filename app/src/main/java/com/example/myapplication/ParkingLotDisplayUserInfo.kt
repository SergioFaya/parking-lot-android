package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.model.ParkingLotForm
import com.example.model.enum.Keys

import kotlinx.android.synthetic.main.activity_parking_lot_display_user_info.*
import kotlinx.android.synthetic.main.content_parking_lot_display_user_info.*

class ParkingLotDisplayUserInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_lot_display_user_info)

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
        val json : String? = sharedPreferences.getString(Keys.USER_SHARED_PREFERENCES.value, null)

        val form =  ParkingLotForm()
        if (json != null) {
            form.deserialize(json)
            displayInfo(form, true)
        } else {
            displayInfo(form, false)
        }
    }

    private fun displayInfo(form: ParkingLotForm, isUserEdited: Boolean){
        if(isUserEdited) {
            val stringBuilder = StringBuilder()
            form.apply {
                userInfoName.text = "Usuario: " +surname+", "+username
                userInfoEmail.text = "Email: " + email
                userInfoNif.text = "Nif: " + nif
                userInfoPhone.text = "Teléfono: " + phone
            }
            fillUserInfoAdvice.visibility = View.GONE
        } else {
            fillUserInfoAdvice.visibility = View.VISIBLE
        }
    }

    private fun configureEvents(){
        val intent = Intent(this, ParkingLotEditUserInfo::class.java).apply {
            // TODO: revisar putExtra
        }
        btnCircleEdit.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            startActivity(intent)
        }
    }

}

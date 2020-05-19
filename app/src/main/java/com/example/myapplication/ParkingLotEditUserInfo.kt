package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.model.ParkingLotForm
import com.example.model.ValidationException
import com.example.model.enum.Keys
import com.example.service.ParkingLotFormValidator
import kotlinx.android.synthetic.main.activity_main.*

class ParkingLotEditUserInfo : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Events
        val button: Button = findViewById(R.id.btnSubmit)
        button.setOnClickListener(this)
    }

    override fun onRestart() {
        super.onRestart()
        loadCurrentUserData()
    }

    override fun onResume() {
        super.onResume()
        loadCurrentUserData()
    }

    override fun onClick(v: View) {
        val form = getFormFromView(v)
        try {
            ParkingLotFormValidator().validate(form)
            addFormDataToSharedPreferences(form)
            goBackToDisplayActivity()
        } catch ( e: ValidationException) {
            // TODO: poner en rojo los campos
            Toast.makeText(v.context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun loadCurrentUserData() {
        val sharedPreferences = getSharedPreferences(Keys.USER_FORM.value, Context.MODE_PRIVATE)
        val json : String? = sharedPreferences.getString(Keys.USER_SHARED_PREFERENCES.value, null)

        val form =  ParkingLotForm()
        if(json != null) {
            form.apply {
                deserialize(json)
                nameEditText.setText(username)
                surnameEditText.setText(surname)
                emailEditText.setText(email)
                phoneEditText.setText(phone)
                nifEditText.setText(nif)
            }
        }

    }

    private fun getFormFromView(v: View): ParkingLotForm{
        return ParkingLotForm(
            email = emailEditText.text.toString(),
            nif = nifEditText.text.toString(),
            phone = phoneEditText.text.toString(),
            surname = surnameEditText.text.toString(),
            username = nameEditText.text.toString()
        )
    }

    private fun addFormDataToSharedPreferences(form: ParkingLotForm){
        val sharedPreferences = getSharedPreferences(Keys.USER_FORM.value, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Keys.USER_SHARED_PREFERENCES.value, form.serialize()).commit()
    }

    private fun goBackToDisplayActivity(){
        finish()
    }
}

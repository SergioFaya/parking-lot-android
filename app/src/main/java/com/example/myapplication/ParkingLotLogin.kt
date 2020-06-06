package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.DbManager
import com.example.model.LocationHistoryList
import com.example.model.ParkingLotForm
import com.example.model.enum.Keys
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_parking_lot_login.*

class ParkingLotLogin : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        autoLogin();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun autoLogin() {
        val sharedPreferences = getSharedPreferences(Keys.USER_FORM.value, Context.MODE_PRIVATE)
        val json: String? = sharedPreferences.getString(Keys.USER_SHARED_PREFERENCES.value, null)
        if (json != null) {
            val form = ParkingLotForm()
            form.deserialize(json)
            loginUser(form.email, form.password)
        } else {
            setContentView(R.layout.activity_parking_lot_login)
            supportActionBar!!.hide();

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            btnLogin.setOnClickListener {
                loginUser(emailEditText.text.toString(), passwordEditText.text.toString());
            };

            btnRegister.setOnClickListener {
                goToRegister()
            };
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    DbManager.getUserInfoByEmail(email)
                        .addOnSuccessListener { documents ->
                            var users = documents.toObjects(ParkingLotForm::class.java)
                            addDataToSharedPreferences(users.get(0))
                            var nif = users.get(0).nif
                            DbManager.getUserLocationsByEmail(nif)
                                .addOnSuccessListener { documents ->
                                    var locations =
                                        documents.toObject(LocationHistoryList::class.java)
                                    if (locations != null) {
                                        addDataToSharedPreferences(locations)
                                    }
                                    authorize();
                                }
                                .addOnFailureListener { exception ->
                                    var a = exception
                                }
                        }
                        .addOnFailureListener { exception ->
                            var a = exception
                        }
                } else {
                    Toast.makeText(
                        this,
                        "La cuenta para ese usuario y contraseña no existe.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    private fun addDataToSharedPreferences(locations: LocationHistoryList) {
        var json = locations.serialize()

        getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE).edit()
            .putString(Keys.LOCATION_HISTORY.value, json).commit();
    }

    private fun addDataToSharedPreferences(form: ParkingLotForm) {
        val sharedPreferences = getSharedPreferences(Keys.USER_FORM.value, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Keys.USER_SHARED_PREFERENCES.value, form.serialize())
            .apply()

    }

    private fun authorize() {
        finish()
        val intent = Intent(this, ParkingLotDisplayUserInfo::class.java)
        startActivity(intent)
    }

    private fun goToRegister() {
        finish()
        val intent = Intent(this, ParkingLotRegisterUser::class.java)
        startActivity(intent)
    }
}

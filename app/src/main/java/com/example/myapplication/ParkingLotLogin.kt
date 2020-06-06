package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
                    authorize();
                }
            }
    }

    private fun authorize() {
        val intent = Intent(this, ParkingLotDisplayUserInfo::class.java)
        startActivity(intent)

        finish()
    }

    private fun goToRegister() {
        val intent = Intent(this, ParkingLotRegisterUser::class.java)
        startActivity(intent)

        finish()
    }
}

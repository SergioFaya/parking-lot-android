package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.model.ParkingLotForm
import com.example.model.ValidationException
import com.example.model.enum.Keys
import com.example.service.ParkingLotFormValidator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class ParkingLotRegisterUser : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth;

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        // Events
        val button: Button = findViewById(R.id.btnRegister)
        button.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        var user: FirebaseUser? = auth.currentUser;


    }

    override fun onClick(v: View) {
        val form = getFormFromView(v)
        try {
            ParkingLotFormValidator().validate(form)
            addFormDataToSharedPreferences(form)
            registerUser(form.email, form.password);
        } catch (e: ValidationException) {
            // TODO: poner en rojo los campos
            Toast.makeText(v.context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun authorize() {
        val intent = Intent(this, ParkingLotDisplayUserInfo::class.java)
        startActivity(intent)
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    authorize();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Debe registrarse para continuar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    authorize();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun addFormDataToSharedPreferences(form: ParkingLotForm) {
        val sharedPreferences = getSharedPreferences(Keys.USER_FORM.value, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Keys.USER_SHARED_PREFERENCES.value, form.serialize())
            .commit()
    }

    private fun getFormFromView(v: View): ParkingLotForm {
        return ParkingLotForm(
            email = emailEditText.text.toString(),
            nif = nifEditText.text.toString(),
            phone = phoneEditText.text.toString(),
            surname = surnameEditText.text.toString(),
            username = nameEditText.text.toString(),
            password = passwordEditText.text.toString()
        )
    }

    private fun goToDisplayActivity() {
        finish()
        val intent = Intent(this, ParkingLotDisplayUserInfo::class.java)
        startActivity(intent)
    }
}
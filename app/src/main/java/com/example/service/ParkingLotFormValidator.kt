package com.example.service

import android.text.TextUtils
import com.example.model.ParkingLotForm
import com.example.model.ValidationException

class ParkingLotFormValidator {

     fun validate(form: ParkingLotForm) {
         validateName(form.username)
         validateSurname(form.surname)
         validatePhone(form.phone)
         validateNif(form.nif)
         validateEmail(form.email)
    }

    private fun validateName(name: String) {
        if (TextUtils.isEmpty(name)) {
            throw ValidationException("Name field cannot be empty")
        }
    }

    private fun validateSurname(surname: String) {
        if (TextUtils.isEmpty(surname)) {
            throw ValidationException("Surname field cannot be empty")
        }
    }

    private fun validateNif(nif: String){
        if (TextUtils.isEmpty(nif)) {
            throw ValidationException("Nif field cannot be empty")
        }
        if(!ValidadorDNI.validar(nif)){
            throw ValidationException("Nif format not valid with spanish DNI")
        }
    }

    private fun validateEmail(email: String){
        if (TextUtils.isEmpty(email)) {
            throw ValidationException("Email field cannot be empty")
        }
        if(!Regex(".*@.*").matches(email)){
            throw ValidationException("Email field is not valid")
        }
    }

    private fun validatePhone(phone: String){
        if (TextUtils.isEmpty(phone)) {
            throw ValidationException("Phone field cannot be empty")
        }
    }
}
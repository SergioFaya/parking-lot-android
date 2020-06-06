package com.example.firebase

import com.example.model.LocationHistoryList
import com.example.model.ParkingLotForm
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

internal object DbManager {

    private val db = FirebaseFirestore.getInstance()

    fun updateUserInfo(form: ParkingLotForm): Task<Void> {
        return db.collection("users")
            .document(form.nif)
            .set(form)
    }

    fun getUserInfoByEmail(email: String): Task<QuerySnapshot> {
        return db.collection("users")
            .whereEqualTo("email", email)
            .get();
    }

    fun updateUserLocations(nif: String, locationHistoryList: LocationHistoryList) {
        locationHistoryList.nif = nif;

        db.collection("locations")
            .document(nif)
            .delete()
            .addOnSuccessListener {
                db.collection("locations")
                    .document(nif)
                    .set(locationHistoryList)
                    .addOnSuccessListener { documentReference ->
                        var ldoc = documentReference
                    }
                    .addOnFailureListener { e ->
                        var doc = e
                    }
            }
            .addOnFailureListener {
                var a = it
            }

    }

    fun getUserLocationsByEmail(nif: String): Task<DocumentSnapshot> {
        return db.collection("locations")
            .document(nif)
            .get();
    }

}
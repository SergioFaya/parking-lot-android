package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.model.LocationHistory

class LocationHistoryActivity : AppCompatActivity(),
    LocationHistoryFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_history)
    }

    override fun onListFragmentInteraction(item: LocationHistory?) {
        // TODO("Not yet implemented")
    }
}

package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.model.LocationHistory
import com.example.model.LocationHistoryList
import com.example.model.enum.Keys


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [LocationHistoryFragment.OnListFragmentInteractionListener] interface.
 */
class LocationHistoryFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location_history_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                var list = getDataList()
                adapter = MyLocationHistoryRecyclerViewAdapter(list, listener)
            }
        }
        return view
    }

    fun getDataList(): List<LocationHistory> {
        val jsonHistoryList = activity
            ?.getSharedPreferences(Keys.USER_SHARED_PREFERENCES.value, Context.MODE_PRIVATE)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: LocationHistory?)
    }

}

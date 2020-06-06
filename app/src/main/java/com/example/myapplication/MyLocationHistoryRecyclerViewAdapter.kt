package com.example.myapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.model.LocationHistory
import com.example.myapplication.LocationHistoryFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_location_history.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyLocationHistoryRecyclerViewAdapter(
    private val mValues: List<LocationHistory>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyLocationHistoryRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as LocationHistory
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_location_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.dateView.text = item.date.toString()
        holder.addressView.text = item.address.toString()

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val dateView: TextView = mView.dateField
        val addressView: TextView = mView.addressField

        override fun toString(): String {
            return super.toString() + " '" + addressView.toString() + "'"
        }
    }
}

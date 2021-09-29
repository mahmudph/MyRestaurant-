package com.example.restaurantreview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val listReview: List<String>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.findViewById(R.id.tvItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItem.text = listReview[position]
    }

    override fun getItemCount() = listReview.size

}
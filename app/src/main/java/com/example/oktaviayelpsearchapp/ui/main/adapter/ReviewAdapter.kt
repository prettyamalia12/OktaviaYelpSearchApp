package com.example.oktaviayelpsearchapp.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oktaviayelpsearchapp.R
import com.example.oktaviayelpsearchapp.data.model.Reviews
import com.example.oktaviayelpsearchapp.ui.main.adapter.ReviewAdapter.DataViewHolder
import kotlinx.android.synthetic.main.reviews_item.view.*

class ReviewAdapter(
    private val reviews: ArrayList<Reviews>
) : RecyclerView.Adapter<DataViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.reviews_item, parent, false
            )
        )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
                holder.bind(reviews[position])
            }

    fun addData(reviewsList: List<Reviews>) {
        reviews.addAll(reviewsList)
    }

    fun refreshData(){
        reviews.removeAll(reviews)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = reviews.size

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(reviews: Reviews) {
            itemView.txtUsername.text = reviews.user.name
            itemView.txtReview.text = reviews.text
            itemView.rbReview.rating = reviews.rating.toFloat()

            itemView.txtDate.text = reviews.time_created

            Glide.with(itemView.imgAvatar.context)
                .load(reviews.user.image_url)
                .into(itemView.imgAvatar)
        }
    }
}


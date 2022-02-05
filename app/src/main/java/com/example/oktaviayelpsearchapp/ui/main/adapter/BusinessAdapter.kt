package com.example.oktaviayelpsearchapp.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oktaviayelpsearchapp.R
import com.example.oktaviayelpsearchapp.data.model.Businesses
import com.example.oktaviayelpsearchapp.ui.main.adapter.BusinessAdapter.DataViewHolder
import kotlinx.android.synthetic.main.business_item.view.*

class BusinessAdapter (
    private val businesses: ArrayList<Businesses>
) : RecyclerView.Adapter<DataViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(businesses: Businesses) {
            itemView.txtName.text = businesses.name
            itemView.txtLocation.text = businesses.location.title
            itemView.txtBusinessType.text = businesses.categories[0].title
            itemView.txtReviewCount.text = businesses.review_count.toString()
            itemView.txtLocation.text = businesses.location.display_address[0]

            itemView.ratingBar.rating = businesses.rating.toFloat()
            Glide.with(itemView.imgLogo.context)
                .load(businesses.image_url)
                .into(itemView.imgLogo)

            //onclick show business details
            itemView.setOnClickListener {
                onItemClick?.invoke(businesses.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.business_item, parent, false
            )
        )

    override fun getItemCount(): Int = businesses.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(businesses[position])

    fun addData(businessList: List<Businesses>) {
        businesses.addAll(businessList)
    }

    fun refreshData(){
        businesses.removeAll(businesses)
        notifyDataSetChanged()
    }
}
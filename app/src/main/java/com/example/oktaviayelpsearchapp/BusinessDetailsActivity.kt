package com.example.oktaviayelpsearchapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.oktaviayelpsearchapp.data.model.Businesses
import com.example.oktaviayelpsearchapp.data.model.Hours
import com.example.oktaviayelpsearchapp.ui.main.adapter.ReviewAdapter
import com.example.oktaviayelpsearchapp.ui.main.viewmodel.ReviewsViewModel
import kotlinx.android.synthetic.main.activity_business_details.*
import kotlinx.android.synthetic.main.activity_business_details.view.*
import java.util.*


class BusinessDetailsActivity : AppCompatActivity() {

    private lateinit var reviewsViewModel: ReviewsViewModel
    private lateinit var adapter: ReviewAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details)

        setupUI()

        val businesses: Businesses? = intent.getSerializableExtra("business") as Businesses?

        if (businesses != null) {
            showBusinessDetails(businesses)
        }

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupUI() {
        rvReview.layoutManager = LinearLayoutManager(this)
        adapter = ReviewAdapter(arrayListOf())
        rvReview.addItemDecoration(
            DividerItemDecoration(
                rvReview.context, (rvReview.layoutManager as LinearLayoutManager).orientation)
        )
        rvReview.adapter = adapter
    }

    fun showBusinessDetails(businesses: Businesses){
        txtBusinessName.text = businesses.name

        val price = businesses.price
        if (price == "") {
            txtPrice.visibility = View.VISIBLE
            txtPrice.text = price
        }else{
            txtPrice.visibility = View.GONE
        }

        txtCategory.text = businesses.categories[0].title
        txtBusinessAddress.text = businesses.location.display_address.joinToString()
        ratingBusiness.rating = businesses.rating.toFloat()
        txtReviews.text = getString(R.string.reviews, businesses.review_count.toString())

        //contact information
        if (businesses.phone != ""){
            imgCall.visibility = View.VISIBLE
            imgCall.setOnClickListener{ callBusiness(businesses.phone) }
            imgMessage.visibility = View.VISIBLE
            imgMessage.setOnClickListener{ messageBusiness(businesses.phone) }
        }else{
            imgCall.visibility = View.GONE
            imgMessage.visibility = View.GONE
        }

        if (businesses.hours != null){
            txtOpenHours.visibility = View.VISIBLE
            txtOpenHours.text = setOpenHours(businesses.hours[0])
        }else{
            txtOpenHours.visibility = View.GONE
        }

        Glide.with(imgBusiness.context)
            .load(businesses.image_url)
            .into(imgBusiness.imgBusiness)

        //fetch review data
        getReviews(businesses.id)
    }

    private fun getReviews(id : String){
        reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        reviewsViewModel.getReviews(id)!!.observe(this){
            adapter.refreshData()
            adapter.addData(it.reviews)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setOpenHours(hours: Hours): String{
        var openHours = ""

        //check if its open today
        if (hours.is_open_now){
            //check todays day
            for (i in hours.open){
                openHours = if (i.day == checkTodaysDay()){
                    //check if its open 24 hours
                    val start = i.start.toString()
                    val end = i.end.toString()

                    if (isOpen24Hours(start, end, i.is_overnight)){
                        getString(R.string.open24Hours)
                    }else{
                        getString(R.string.openHours, setHourFormat(start), setHourFormat(end))
                    }

                }else{
                    getString(R.string.close)
                }
            }
        }else{
            openHours = getString(R.string.close)
        }

        return openHours
    }

    private fun isOpen24Hours(start: String, end: String, isOvernight: Boolean): Boolean{
        return start == "0" && end == "0" && isOvernight
    }

    private fun setHourFormat(hours: String): String {
        return hours.substring(0, 2) + ":" + hours.substring(2, 4)
    }

    private fun checkTodaysDay(): Int {
        val calendar: Calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK) - 1
    }

    private fun messageBusiness(phone: String) {
        val url = "https://api.whatsapp.com/send?phone=$phone"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun callBusiness(phone: String) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CALL_PHONE),
                123)
        } else {
            startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:$phone")))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }

        item.setTitle(R.string.business_details)

        return super.onOptionsItemSelected(item)
    }

}
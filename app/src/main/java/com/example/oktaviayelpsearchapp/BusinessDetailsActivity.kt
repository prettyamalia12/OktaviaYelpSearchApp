package com.example.oktaviayelpsearchapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.oktaviayelpsearchapp.data.model.Businesses
import kotlinx.android.synthetic.main.activity_business_details.*

class BusinessDetailsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details)

        val businesses: Businesses? = intent.getSerializableExtra("business") as Businesses?

        if (businesses != null) {
            txtBusinessName.text = businesses.name
            txtBusinessAddress.text = businesses.location.display_address[0]+businesses.location.display_address[1]
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
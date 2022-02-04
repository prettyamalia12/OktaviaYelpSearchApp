package com.example.oktaviayelpsearchapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oktaviayelpsearchapp.ui.main.adapter.BusinessAdapter
import com.example.oktaviayelpsearchapp.ui.main.viewmodel.BusinessViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_business.*

class BusinessActivity : AppCompatActivity() {

    private lateinit var businessViewModel: BusinessViewModel
    private lateinit var adapter: BusinessAdapter
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var longitude = 0.0
    private var latitude = 0.0
    private val LOCATION_PERMISSION_REQ_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupUI()
        setUpViewModel()
        getUserLocation()
    }

    private fun getUserLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)

            return
        }

        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude
                getBusinessList()
            }
            ?.addOnFailureListener {
                Toast.makeText(this, "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBusinessList(){
        if (longitude == 0.0 || latitude == 0.0) return

        businessViewModel.getBusiness(latitude, longitude)!!.observe(this) {
            adapter.addData(it.businesses)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupUI() {
        rvBusiness.layoutManager = LinearLayoutManager(this)
        adapter = BusinessAdapter(arrayListOf())
        rvBusiness.addItemDecoration(
            DividerItemDecoration(
                rvBusiness.context, (rvBusiness.layoutManager as LinearLayoutManager).orientation)
        )
        rvBusiness.adapter = adapter
    }

    private fun setUpViewModel(){
        businessViewModel = ViewModelProvider(this).get(BusinessViewModel::class.java)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchViewItem.actionView as SearchView

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(this, R.layout.layout_search, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))

        searchView.suggestionsAdapter = cursorAdapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                 cursor.let {
                     if (newText == null) return false
                        val suggested = searchBusiness(newText)
                        suggested.forEachIndexed { index, suggestion ->
                            if (suggestion.contains(newText, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }

                     cursorAdapter.changeCursor(cursor)
                }

                return true
            }
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener{
            override fun onSuggestionSelect(p0: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursors = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursors.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                // TODO Do something with selection
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    fun searchBusiness(text:String): List<String>{
        val suggestions : ArrayList<String> = ArrayList()
        businessViewModel.searchBusiness(text, latitude, longitude)!!.observe(this){
            for (business in it.businesses){
                suggestions.add(business?.alias ?: return@observe)
            }

            for (category in it.categories){
                suggestions.add(category?.title ?: return@observe)
            }

            for (term in it.terms){
                suggestions.add(term?.text ?: return@observe)
            }
        }

        return suggestions
    }
}
package com.example.oktaviayelpsearchapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AutoCompleteTextView
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
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_business.*


class BusinessActivity : AppCompatActivity() {

    private lateinit var businessViewModel: BusinessViewModel
    private lateinit var adapter: BusinessAdapter
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var longitude : Double = 0.0
    private var latitude : Double = 0.0
    private val LOCATION_PERMISSION_REQ_CODE = 100
    private var searchResult : HashMap<String, String> = HashMap()

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
                if (location != null){
                    latitude = location.latitude
                    longitude = location.longitude
                    getBusinessList()
                }else{
                    val mLocationRequest: LocationRequest = LocationRequest.create()
                    mLocationRequest.interval = 60000
                    mLocationRequest.fastestInterval = 5000
                    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val mLocationCallback: LocationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            if (locationResult.equals(null)) {
                                return
                            }
                            for (l in locationResult.locations) {
                                if (l != null) {
                                    longitude = l.longitude
                                    latitude = l.latitude
                                    getBusinessList()
                                }
                            }
                        }
                    }
                    fusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, mainLooper)
                }
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
                    addMessage(getString(R.string.grant_access))
                }
            }
        }
    }

    private fun addMessage(text: String){
        Toast.makeText(this, text,
            Toast.LENGTH_LONG).show()
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBusinessList(){
        showProgressBar()
        if (longitude == 0.0  || latitude == 0.0) {
            hideProgressBar()
            addMessage(getString(R.string.location_unknown))
        }
        else {
            businessViewModel.getBusiness(latitude, longitude)!!.observe(this) {
                if (it.businesses.isNotEmpty()){
                    hideProgressBar()
                    adapter.refreshData()
                    adapter.addData(it.businesses)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searchBusiness(text:String){
        showProgressBar()
        if (longitude == 0.0 || latitude == 0.0){
            hideProgressBar()
            addMessage(getString(R.string.location_unknown))
        }
        else {
            businessViewModel.searchBusiness(text, latitude, longitude)!!.observe(this){
                if (it.businesses.isNotEmpty()){
                    hideProgressBar()
                    adapter.refreshData()
                    adapter.addData(it.businesses)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupUI() {
        rvBusiness.layoutManager = LinearLayoutManager(this)
        adapter = BusinessAdapter( arrayListOf())
        rvBusiness.addItemDecoration(
            DividerItemDecoration(
                rvBusiness.context, (rvBusiness.layoutManager as LinearLayoutManager).orientation)
        )
        rvBusiness.adapter = adapter
        adapter.onItemClick = { id ->
            getBusinessDetails(id)
        }
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
        val searchSrcTextView = searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)

        searchView.suggestionsAdapter = cursorAdapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchBusiness(query?:"")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isEmpty() == true) {
                    searchSrcTextView.threshold = 1
                }else{
                    val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                    // get suggestions
                    newText.let {
                        newText?.let { autocompleteBusiness(it) }?.forEachIndexed { index, suggestion ->
                            if (suggestion.contains(newText, true)) {
                                cursor.addRow(arrayOf(index, suggestion))
                            }
                        }
                    }
                    cursorAdapter.changeCursor(cursor)
                }

                return true
            }
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener{
            val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
            override fun onSuggestionSelect(p0: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursors = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursors.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)
                //if it has an id, get the business details
                if (searchResult.containsKey("id")){
                    getBusinessDetails(searchResult["id"]!![position].toString())
                }
                //else do the search by keyword
                else{
                    searchBusiness(searchResult["alias"]!![position].toString())
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    fun getBusinessDetails(id : String){
        businessViewModel.getBusinessDetails(id)!!.observe(this){
            val intent = Intent(this, BusinessDetailsActivity::class.java).apply {
                putExtra("business", it)
            }
            startActivity(intent)
        }
    }

    fun autocompleteBusiness(text:String): ArrayList<String> {
        val search : ArrayList<String> = ArrayList()
        if (longitude == 0.0 || latitude == 0.0) {
            hideProgressBar()
            addMessage(getString(R.string.location_unknown))
        } else{
            businessViewModel.autocompleteBusiness(text, latitude, longitude)!!.observe(this){
                for (i in it.businesses){
                    if (i != null) {
                        searchResult["business_id"] = i.id
                        search.addAll(listOf(i.alias))
                    }
                }
                for (i in it.categories){
                    if (i != null) {
                        searchResult["alias"] = i.title
                        search.addAll(listOf(i.title))
                    }
                }
                for (i in it.terms){
                    if (i != null) {
                        searchResult["alias"] = i.text
                        search.addAll(listOf(i.text))
                    }
                }
            }
        }

        return search
    }
}

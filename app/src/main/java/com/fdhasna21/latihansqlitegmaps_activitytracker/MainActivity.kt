package com.fdhasna21.latihansqlitegmaps_activitytracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat.is24HourFormat
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var mMap : GoogleMap
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var myLocation : LatLng    //location based GPS
    private lateinit var myAddress : String     //address based GPS
    private lateinit var myDate : String        //device's date
    private lateinit var myTime : String        //device's time
    private val delayTime : Long = 1000

    private fun getDataEntry(data: TextInputEditText):String{
        return data.text.toString()
    }

    private fun addHistoryToDatabase() {
        val databaseHandler = DatabaseHandler(this)
        databaseHandler.addHistory(
            HistoryModel(
                0,
                getDataEntry(main_date),
                getDataEntry(main_time),
                getDataEntry(main_history),
                getDataEntry(main_address)
            )
        )
        Toast.makeText(this, "New record added.", Toast.LENGTH_SHORT).show()
        main_date.text?.clear()
        main_time.text?.clear()
        main_history.text?.clear()
        main_address.text?.clear()
    }

    @SuppressLint("RestrictedApi")
    private fun getCurrentLocation(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.main_map) as SupportMapFragment
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest()
            .setInterval(5000)
            .setFastestInterval(3000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations) {
                        mapFragment.getMapAsync {
                            mMap = it

                            if (ActivityCompat.checkSelfPermission(
                                    this@MainActivity,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this@MainActivity,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return@getMapAsync
                            }
                            mMap.isMyLocationEnabled = true
                            mMap.uiSettings.isZoomControlsEnabled = true
                            mMap.uiSettings.isCompassEnabled = true
                            mMap.uiSettings.isMapToolbarEnabled = true

                            val locationResult = LocationServices.getFusedLocationProviderClient(
                                this@MainActivity
                            ).lastLocation
                            locationResult.addOnCompleteListener(this@MainActivity) {
                                if (it.isSuccessful && (it.result != null)) {
                                    myLocation = LatLng(it.result.latitude, it.result.longitude)
                                    val geocoder = Geocoder(this@MainActivity)
                                    val geoAddress = geocoder.getFromLocation(it.result.latitude, it.result.longitude, 1)
                                    mMap.clear()
                                    myAddress = geoAddress[0].getAddressLine(0)
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))

                                    //TODO : kalo newLocation sama kayak myLocation-> myLocation = newLocation (tapi myLocation harus di definisikan dahulu, pertama kali jalan buat ngasih nilai myLocation)
                                    mMap.addMarker(MarkerOptions().position(myLocation).title("My Location")).showInfoWindow()
                                    main_address.setText(myAddress)
                                }
                                else {
                                    Toast.makeText(this@MainActivity, "Downloading current location failed.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            },
            Looper.myLooper()
        )
    }

    @SuppressLint("NewApi")
    private fun getCurrentTimestamp(){
        val timeFormat = if(is24HourFormat(this)) "HH:mm:ss" else "KK:mm:ss a"
        myDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(Date())
        myTime = SimpleDateFormat(timeFormat, Locale.getDefault()).format(Date())

        val dateInMainActivity = SimpleDateFormat("EEEE,\ndd MMMM yyyy", Locale.getDefault()).format(Date())
        main_date.setText(dateInMainActivity)
        main_time.setText(myTime)
    }

    private fun getCurrentLocalData(){
        getCurrentLocation() //TODO : klo posisinya ga berubah, gak usah berubah (bikin old + new location!)
        getCurrentTimestamp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.setTitle("Activity Tracker")

        getCurrentTimestamp()
        val handler = Handler()
        handler.postDelayed(
            object : Runnable {
                override fun run(){
                    getCurrentLocalData()
                    handler.postDelayed(this, delayTime)
                } },
            delayTime)

        fd_btn_addActivity.setOnClickListener {
            if(getDataEntry(main_history).isEmpty()){
                Toast.makeText(this, "Activity cannot be empty.", Toast.LENGTH_SHORT).show()
            }
            else{
                addHistoryToDatabase()
                getCurrentLocalData()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.topbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.topbar_history -> {
                val intent = Intent(this, TrackerHistory::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
package com.anahitavakoli.apps.nearby

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import java.lang.Exception

class GpsTracker(var context: Context): LocationListener,Service() {

    private var isGPSEnable = false
    private var isNetworkEnable = false
    private var canGetLocation = false
    private var location : Location ?= null
    var latitude = 0.0
    var longitude = 0.0
    lateinit var locationManager : LocationManager


    companion object{
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATE = 10f
        private const val MIN_TIME_CHANGE_FOR_UPDATE : Long= 60000
    }

    init {
        getLocation()
    }

    private fun getLocation() : Location?{

        try {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if(isGPSEnable && isNetworkEnable){

                canGetLocation = true

                if(isNetworkEnable){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_CHANGE_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE,this)

                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if(location != null){
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }
                }

                if(isGPSEnable){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_CHANGE_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE,this)

                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if(location != null){
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }
                }
            }
        }
        catch (e : Exception){
            e.printStackTrace()
        }
        return location
    }

    @JvmName("getLatitude1")
    fun getLatitude() : Double{
        if(location != null){
            latitude = location!!.latitude
        }
        return latitude
    }

    @JvmName("getLongitude1")
    fun getLongitude() : Double {
        if(location != null){
            latitude = location!!.longitude
        }
        return longitude
    }

    fun canGetLocation(): Boolean{
        getLocation()
        return canGetLocation
    }

    fun stopUsingGPS(){
        if(locationManager != null)
            locationManager.removeUpdates(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun onProviderEnabled(provider: String) {
    }
    override fun onLocationChanged(p0: Location) {
    }


    interface StateGps{
        fun disableGps()
        fun enableGps()
        fun onChangeLocation(location:Location)
    }
}
package com.anahitavakoli.apps.nearby

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.anahitavakoli.myapplication.api.ApiService
import com.anahitavakoli.myapplication.api.IService
import com.anahitavakoli.apps.nearby.model.Address
import com.anahitavakoli.apps.nearby.model.PlaceData
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() , OnMapReadyCallback{

    lateinit var gpsTracker : GpsTracker
    var lat : Double = 0.0
    var lng : Double = 0.0
    val GPS_PERMISSION_CODE = 1414
    lateinit var iService : IService
    val key: String = "service.feb17fd351784011a7a6f572c82055e4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iService = ApiService.retrofit.create(IService::class.java)

        if(ContextCompat.checkSelfPermission(this@MainActivity
                ,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),GPS_PERMISSION_CODE)

        }
        else{
            getUserLocation()
        }


        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        map.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getUserLocation()
    }

    private fun getUserLocation() {
        gpsTracker = GpsTracker(applicationContext)
        if (gpsTracker.canGetLocation()) {

            lat = gpsTracker.getLatitude()
            lng = gpsTracker.getLongitude()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        var position = LatLng(lat,lng)
        var marker = MarkerOptions().position(position).title("My Position").snippet("This is test position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))

        var position2 = LatLng(20.344222,36.743598)
        var position3 = LatLng(20.644222,36.643598)
        var position4 = LatLng(20.844222,36.243598)
        var marker2 = MarkerOptions().position(position2).title("My Position2").snippet("This is test position2").icon(BitmapDescriptorFactory.fromResource(
            R.drawable.icon_map
        ))


        map?.addMarker(marker)
        map?.addMarker(marker2)
        //map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map?.isMyLocationEnabled = true
        map?.uiSettings?.isZoomControlsEnabled = true

        map?.addPolyline(PolylineOptions().add(position,position2).color(R.color.material_dynamic_neutral10).width(5f))
        var camera = CameraPosition.builder().target(position).zoom(15f).build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(camera))

        map?.addCircle(CircleOptions().center(position).radius(1000.0).fillColor(Color.parseColor("#56565656")))

        map?.addPolygon(PolygonOptions().add(position2,position3,position4).strokeColor(R.color.material_dynamic_neutral40).fillColor(
            R.color.material_dynamic_primary95
        ))


        var lat2 = 35.7562941
        var lng2 = 51.3446611
        iService.getPosition(key,lat2,lng2).enqueue(object: Callback<Address>{
            override fun onResponse(call: Call<Address>, response: Response<Address>) {
                var position8 = LatLng(lat2,lng2)
                var marker8 = MarkerOptions().position(position8).title(response.body()!!.route_name).snippet(
                    response.body()!!.formatted_address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                map?.addMarker(marker8)
            }

            override fun onFailure(call: Call<Address>, t: Throwable) {
            }

        })

        iService.search(key,"Fastfood", lat2 ,lng2).enqueue(object: Callback<PlaceData>{
            override fun onResponse(call: Call<PlaceData>, response: Response<PlaceData>) {
                for(i in response.body()!!.items){

                    var position = LatLng(i.location.latitude,i.location.longitude)
                    var marker = MarkerOptions().position(position).title("${i.title}").snippet("${i.neighbourhood}").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    map?.addMarker(marker)

                }
            }

            override fun onFailure(call: Call<PlaceData>, t: Throwable) {
                Log.e("","")
            }

        })
    }
}
package com.jagl.globalsight.ui.view

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jagl.globalsight.R
import com.jagl.globalsight.data.model.Coordinates

class MapActivity: FragmentActivity(), OnMapReadyCallback {

    companion object{
        const val USER_COORDINATES = "MapActivity_User"
        const val BUSSINES_COORDINATES = "MapActivity_Bussines"
    }

    private var googleMap: GoogleMap? = null
    private var user: Coordinates? = null
    private var bussines: Coordinates? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        user = intent.getParcelableExtra(USER_COORDINATES)
        bussines = intent.getParcelableExtra(BUSSINES_COORDINATES)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        addMarker(user)
        addMarker(bussines)
    }

    private fun addMarker(coordinates: Coordinates?) {
        var position = LatLng(-34.0,151.0)
        if (coordinates != null){
            position = LatLng(coordinates.latitude ?:0.0, coordinates.longitude ?:0.0)
        }
        googleMap?.addMarker(
            MarkerOptions()
                .position(position)
        )
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 9f))
    }
}
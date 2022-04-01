package com.jagl.globalsight.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.net.LocalServerSocket
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jagl.globalsight.data.model.Coordinates
import com.jagl.globalsight.data.model.ResponseYelpApi
import com.jagl.globalsight.data.network.YelpApiClient
import com.jagl.globalsight.databinding.ActivityMainBinding
import com.jagl.globalsight.ui.adapter.BusinessAdapter
import com.jagl.globalsight.util.Constants.DEFAULT_LATITUDE
import com.jagl.globalsight.util.Constants.DEFAULT_LONGITUDE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private var DEFAULT_COORDINATES = Coordinates(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            doRequestBussines(isGranted)
        }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.search.clearFocus()
                binding.message.visibility = View.GONE
                query?.let {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    makeQuerry(query, DEFAULT_COORDINATES)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun setAdapter(data: ResponseYelpApi?, recyclerView: RecyclerView, context: Context) {
        Log.d(TAG, "Init Adapter")
        val adapter = BusinessAdapter(data, context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        Log.d(TAG, "Finish the init adapter")

    }

    private fun doRequestBussines(isLocationGranted: Boolean) {
        lifecycleScope.launch {
            DEFAULT_COORDINATES = getCoordinates(isLocationGranted)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCoordinates(isLocationGranted: Boolean): Coordinates =
        suspendCancellableCoroutine { continuation ->
            if (isLocationGranted) {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    continuation.resume(getCoordinatesFromLocation(it.result))
                }
            } else {
                continuation.resume(DEFAULT_COORDINATES)
            }
        }

    private fun getCoordinatesFromLocation(location: Location?): Coordinates {
        return if (location == null) DEFAULT_COORDINATES
        else Coordinates(location.latitude, location.longitude)
    }


    private fun makeQuerry(term: String, coordinates: Coordinates) {
        binding.progress.visibility = View.VISIBLE
        val service = YelpApiClient.createService()
        lifecycleScope.launch(Dispatchers.Main) {
            val result = service.getSearch(coordinates.latitude!!, coordinates.longitude!!, term)
            result?.let {
                if (it.businesses?.count() != 0) {
                    setAdapter(it, binding.recycler, binding.root.context)
                } else {
                    binding.message.visibility = View.VISIBLE
                    setAdapter(it, binding.recycler, binding.root.context)
                }
                binding.progress.visibility = View.GONE
            }
        }
    }

}



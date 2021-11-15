package com.mas.mypoints.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mas.mypoints.R
import com.mas.mypoints.data.Point
import com.mas.mypoints.databinding.ActivityMapsBinding


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var pointsList = arrayListOf<Point>()

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val pointsListResult =
                    result.data?.getParcelableArrayListExtra<Point>(ListPointsActivity.LIST_POINTS_EXTRA)
                if (pointsListResult != null && pointsListResult.count() != pointsList.count()) {
                    mMap.clear()
                    pointsListResult.forEach { point ->
                        addPointToMap(point)
                    }
                    pointsList.clear()
                    pointsList = pointsListResult
                }
                val point = result.data?.getParcelableExtra<Point>("PointToMapResult")
                point?.let { cameraMove(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startForResult.launch(ListPointsActivity.setIntent(this, pointsList))
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        with(mMap.uiSettings) {
            isCompassEnabled = true
            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = true
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnMapLongClickListener {
            var str = ""
            try {
                val address = Geocoder(baseContext).getFromLocation(it.latitude, it.longitude, 1)
                if (!address.isEmpty()) {
                    str = getAddress(address[0])
                }
            } catch (e: Exception) {
                Toast.makeText(baseContext, "Не удалось получить адрес", Toast.LENGTH_SHORT).show()

            }
            showDialog(Point(str, it.latitude, it.longitude))
        }

        pointsList.add(Point("Spider Man", 37.4219999, -122.0862462, true))
        pointsList.add(Point("Iron Man", 37.4629101, -122.2449094))

        pointsList.forEach { point ->
            addPointToMap(point)
        }

        cameraMove(pointsList.get(0))


    }

    private fun cameraMove(point: Point) {
        val googlePlex = CameraPosition.builder()
            .target(LatLng(point.latitude, point.longitude))
            .zoom(10f)
            .bearing(0f)
            .tilt(45f)
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null)
    }

    private fun addPointToMap(point: Point) {
        val settings = MarkerOptions()
            .position(LatLng(point.latitude, point.longitude))
            .title(point.description)
        if (point.alert) {
            settings.icon(
                bitmapDescriptorFromVector(
                    baseContext,
                    R.drawable.ic_baseline_emoji_people_24
                )
            )
        }
        mMap.addMarker(settings)
    }

    private fun getAddress(address: Address): String {
        var adr = ""
//        adr += if (address.countryName.isNotEmpty()) "%s, ".format(address.countryName) else ""
//        adr += if (address.adminArea.isNotEmpty()) "%s, ".format(address.adminArea) else ""
        adr += if (address.locality.isNotEmpty()) "%s, ".format(address.locality) else ""
        adr += if (address.thoroughfare.isNotEmpty()) "%s, ".format(address.thoroughfare) else ""
        adr += if (address.subThoroughfare.isNotEmpty()) "%s ".format(address.subThoroughfare) else ""

        return adr
    }

    fun showDialog(point: Point) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_point_view, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Point name")
        builder.setView(mDialogView)

        val input = mDialogView.findViewById<EditText>(R.id.input)
        val checkBox = mDialogView.findViewById<CheckBox>(R.id.checkbox)
        input.setText(point.description)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            point.description = input.text.toString()
            point.alert = checkBox.isChecked
            pointsList.add(point)
            Toast.makeText(baseContext, "Point is saved: ${point.description}", Toast.LENGTH_SHORT)
                .show()
            addPointToMap(point)
        })
        builder.setNegativeButton("Cancel", null)

        builder.show()
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
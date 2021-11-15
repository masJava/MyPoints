package com.mas.mypoints.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mas.mypoints.presenter.ListPointsPresenter
import com.mas.mypoints.presenter.PointsListAdapter
import com.mas.mypoints.R
import com.mas.mypoints.data.Point


class ListPointsActivity() : AppCompatActivity(), ViewPointListContract {

    private lateinit var adapter: PointsListAdapter
    private val presenter = ListPointsPresenter(this)
    var pointsList = arrayListOf<Point>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_points)
        pointsList = intent.getParcelableArrayListExtra<Point>(LIST_POINTS_EXTRA) ?: arrayListOf()
        adapter = PointsListAdapter(pointsList, presenter)
        val rvListPoints = findViewById<RecyclerView>(R.id.rvPoints)
        rvListPoints.adapter = adapter
        rvListPoints.layoutManager = GridLayoutManager(baseContext, 1)
    }

    companion object {

        const val LIST_POINTS_EXTRA = "LIST_POINTS_EXTRA"

        fun setIntent(context: Context, points: ArrayList<Point>): Intent {
            return Intent(context, ListPointsActivity::class.java)
                .apply {
                    putParcelableArrayListExtra(LIST_POINTS_EXTRA, points)
                }
        }
    }

    override fun backToMap(pointsList: ArrayList<Point>, point: Point?) {
        val intent = Intent()
        point?.let { intent.putExtra("PointToMapResult", it) }
        intent.putParcelableArrayListExtra(LIST_POINTS_EXTRA, pointsList)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        backToMap(pointsList, null)
    }
}


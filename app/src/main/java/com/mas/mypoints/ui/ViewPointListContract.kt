package com.mas.mypoints.ui

import com.mas.mypoints.data.Point

internal interface ViewPointListContract : ViewContract {
    fun backToMap(pointsList: ArrayList<Point>, point: Point?)
}

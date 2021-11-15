package com.mas.mypoints.presenter

import com.mas.mypoints.data.Point


internal interface PresenterPointListContract : PresenterContract {
    fun goToMap(pointsList: ArrayList<Point>, point: Point)
}

package com.mas.mypoints.presenter

import com.mas.mypoints.data.Point
import com.mas.mypoints.ui.ViewPointListContract

class ListPointsPresenter internal constructor(
    private val viewContract: ViewPointListContract
) : PresenterPointListContract {
    override fun goToMap(pointsList: ArrayList<Point>, point: Point) {
        viewContract.backToMap(pointsList, point)
    }


}

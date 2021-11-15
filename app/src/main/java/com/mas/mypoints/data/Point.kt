package com.mas.mypoints.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Point(
    var description: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var alert: Boolean = false,
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readBoolean()
    )


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeString(description)
        writeDouble(latitude)
        writeDouble(longitude)
        writeBoolean(alert)
    }

    override fun describeContents() = 0


    companion object CREATOR : Parcelable.Creator<Point> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Point {
            return Point(parcel)
        }

        override fun newArray(size: Int): Array<Point?> {
            return arrayOfNulls(size)
        }
    }
}
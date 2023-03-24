package com.example.stayfinder

import android.os.Parcel
import android.os.Parcelable

class Booking(titlename: String?, period: String?, price: Double,status: String?, img: Int?):Parcelable {
    public var titlename: String? = ""
    public var period: String? = ""
    public var price: Double? = 0.0
    public var status: String? = ""
    public var img: Int? = null
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readInt()
    ) {
        titlename = parcel.readString()
        period = parcel.readString()
        price = parcel.readDouble()
        status = parcel.readString()
        img = parcel.readInt()
    }
    init {
        this.titlename = titlename
        this.period = period
        this.price = price
        this.status = status
        this.img = img
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Booking> {
        override fun createFromParcel(parcel: Parcel): Booking {
            return Booking(parcel)
        }

        override fun newArray(size: Int): Array<Booking?> {
            return arrayOfNulls(size)
        }
    }

}
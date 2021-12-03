package com.HomeStudio.QualityFPV.data.product

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity

// The product class to represent a drone product scraped from one of the three main drone websites
@Entity(primaryKeys = ["url", "product_type"], tableName = "product_table")
data class Product(
    var name: String = "Product Name",
    @ColumnInfo
    var product_type: String,
    var img: String,
    var price: String,
    var rating: Double,
    @ColumnInfo
    var url: String,
    var website: String

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readDouble(),
        parcel.readString() as String,
        parcel.readString() as String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(product_type)
        parcel.writeString(img)
        parcel.writeString(price)
        parcel.writeDouble(rating)
        parcel.writeString(url)
        parcel.writeString(website)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
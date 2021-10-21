package com.HomeStudio.QualityFPV.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey
    var name: String = "Product Name",
    var product_type: String,
    var img: String,
    var price: String,
    var rating: Double,
    var url: String

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readDouble(),
        parcel.readString() as String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(product_type)
        parcel.writeString(img)
        parcel.writeString(price)
        parcel.writeDouble(rating)
        parcel.writeString(url)
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
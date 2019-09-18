package com.example.terasystemhrisv3.model

import android.os.Parcel
import android.os.Parcelable

data class Leaves(
  var userID: String,
  var type: String,
  var dateFrom: String,
  var dateTo: String,
  var time: String
) : Parcelable {
  constructor(source: Parcel?) : this(
    source?.readString() ?: "",
    source?.readString() ?: "",
    source?.readString() ?: "",
    source?.readString() ?: "",
    source?.readString() ?: ""
  )

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeString(userID)
    dest?.writeString(type)
    dest?.writeString(dateFrom)
    dest?.writeString(dateTo)
    dest?.writeString(time)
  }

  override fun describeContents(): Int = 0

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<Leaves> = object : Parcelable.Creator<Leaves> {
      override fun createFromParcel(source: Parcel?): Leaves {
        return Leaves(source)
      }

      override fun newArray(size: Int): Array<Leaves> {
        return Array(size) { Leaves(null) }
      }
    }
  }
}
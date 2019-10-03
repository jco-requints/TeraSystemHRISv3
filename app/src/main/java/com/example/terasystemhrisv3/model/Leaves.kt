package com.example.terasystemhrisv3.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Leaves(
        @SerializedName("userID")
        @Expose
        var userID: String?,
        @SerializedName("type")
        @Expose
        var type: String?,
        @SerializedName("dateFrom")
        @Expose
        var dateFrom: String?,
        @SerializedName("dateTo")
        @Expose
        var dateTo: String?,
        @SerializedName("time")
        @Expose
        var time: String?
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
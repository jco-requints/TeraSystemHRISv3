package com.example.terasystemhrisv3.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Logs(
        @SerializedName("userID")
        @Expose
        var userID: String?,
        @SerializedName("date")
        @Expose
        var date: String?,
        @SerializedName("timeIn")
        @Expose
        var timeIn: String?,
        @SerializedName("breakOut")
        @Expose
        var breakOut: String?,
        @SerializedName("breakIn")
        @Expose
        var breakIn: String?,
        @SerializedName("timeOut")
        @Expose
        var timeOut: String?
) : Parcelable {
  constructor(source: Parcel?) : this(
    source?.readString() ?: "",
    source?.readString() ?: "",
    source?.readString() ?: "",
    source?.readString() ?: "",
    source?.readString() ?: "",
    source?.readString() ?: ""
  )

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeString(userID)
    dest?.writeString(date)
    dest?.writeString(timeIn)
    dest?.writeString(breakOut)
    dest?.writeString(breakIn)
    dest?.writeString(timeOut)
  }

  override fun describeContents(): Int = 0

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<Logs> = object : Parcelable.Creator<Logs> {
      override fun createFromParcel(source: Parcel?): Logs {
        return Logs(source)
      }

      override fun newArray(size: Int): Array<Logs> {
        return Array(size) { Logs(null) }
      }
    }
  }
}
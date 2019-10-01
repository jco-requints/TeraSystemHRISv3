package com.example.terasystemhrisv3.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AccountDetails(
        @SerializedName("userID")
        @Expose
        var userID: String?,
        @SerializedName("idNumber")
        @Expose
        var idNumber: String?,
        @SerializedName("firstName")
        @Expose
        var firstName: String?,
        @SerializedName("middleName")
        @Expose
        var middleName: String?,
        @SerializedName("lastName")
        @Expose
        var lastName: String?,
        @SerializedName("emailAddress")
        @Expose
        var emailAddress: String?,
        @SerializedName("mobileNumber")
        @Expose
        var mobileNumber: String?,
        @SerializedName("landlineNumber")
        @Expose
        var landlineNumber: String?
) : Parcelable {
    constructor(source: Parcel?) : this(
        source?.readString() ?: "",
        source?.readString() ?: "",
        source?.readString() ?: "",
        source?.readString() ?: "",
        source?.readString() ?: "",
        source?.readString() ?: "",
        source?.readString() ?: "",
        source?.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(userID)
        dest?.writeString(idNumber)
        dest?.writeString(firstName)
        dest?.writeString(middleName)
        dest?.writeString(lastName)
        dest?.writeString(emailAddress)
        dest?.writeString(mobileNumber)
        dest?.writeString(landlineNumber)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AccountDetails> = object : Parcelable.Creator<AccountDetails> {
            override fun createFromParcel(source: Parcel?): AccountDetails {
                return AccountDetails(source)
            }

            override fun newArray(size: Int): Array<AccountDetails> {
                return Array(size) { AccountDetails(null) }
            }
        }
    }
}
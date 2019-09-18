package com.example.terasystemhrisv3.model

import android.os.Parcel
import android.os.Parcelable

data class AccountDetails(
    var username: String,
    var empID: String,
    var firstName: String,
    var middleName: String?,
    var lastName: String,
    var emailAddress: String,
    var mobileNumber: String,
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
        dest?.writeString(username)
        dest?.writeString(empID)
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
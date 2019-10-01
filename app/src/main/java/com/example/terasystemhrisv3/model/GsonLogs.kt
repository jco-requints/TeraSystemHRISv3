package com.example.terasystemhrisv3.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GsonLogs{
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("timeLogs")
    @Expose
    var timeLogs: Array<Logs>? = null
    @SerializedName("userID")
    @Expose
    var userID: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("timeIn")
    @Expose
    var timeIn: String? = null
    @SerializedName("breakOut")
    @Expose
    var breakOut: String? = null
    @SerializedName("breakIn")
    @Expose
    var breakIn: String? = null
    @SerializedName("timeOut")
    @Expose
    var timeOut: String? = null
}
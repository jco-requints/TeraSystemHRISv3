package com.example.terasystemhrisv3.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GsonAddTimeLog{
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}
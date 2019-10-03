package com.example.terasystemhrisv3.service

import com.example.terasystemhrisv3.model.*
import com.example.terasystemhrisv3.util.URLs
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {
    @FormUrlEncoded
    @POST(URLs.URL_LOGIN)
    suspend fun Login(@Field("userID") userID: String?, @Field("password") password: String?): Response<GsonAccountDetails>

    @FormUrlEncoded
    @POST(URLs.URL_UPDATE_PROFILE)
    suspend fun Update(@Field("userID") userID: String, @Field("firstName") firstName: String,
                       @Field("middleName") middleName: String, @Field("lastName") lastName: String,
                       @Field("emailAddress") emailAddress: String, @Field("mobileNumber") mobileNumber: String,
                       @Field("landline") landline: String): Response<GsonUpdate>

    @FormUrlEncoded
    @POST(URLs.URL_GET_TIME_LOGS)
    suspend fun GetTimeLogs(@Field("userID") userID: String?): Response<GsonLogs>

    @FormUrlEncoded
    @POST(URLs.URL_ADD_TIME_LOG)
    suspend fun AddTimeLog(@Field("userID") userID: String?, @Field("type") type: String?): Response<GsonAddTimeLog>

    @FormUrlEncoded
    @POST(URLs.URL_GET_LEAVES)
    suspend fun GetLeaves(@Field("userID") userID: String?): Response<GsonLeaves>

    @FormUrlEncoded
    @POST(URLs.URL_ADD_LEAVE)
    suspend fun AddLeaveWholeDay(@Field("userID") userID: String, @Field("type") type: String,
                                @Field("dateFrom") dateFrom: String, @Field("dateTo") dateTo: String,
                                @Field("time") time: String): Response<GsonAddLeave>

    @FormUrlEncoded
    @POST(URLs.URL_ADD_LEAVE)
    suspend fun AddLeaveHalfDay(@Field("userID") userID: String, @Field("type") type: String,
                                @Field("dateFrom") dateFrom: String, @Field("time") time: String): Response<GsonAddLeave>
}
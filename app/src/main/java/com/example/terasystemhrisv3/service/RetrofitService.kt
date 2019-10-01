package com.example.terasystemhrisv3.service

import com.example.terasystemhrisv3.model.GsonAccountDetails
import com.example.terasystemhrisv3.model.GsonLogs
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
    @POST(URLs.URL_GET_TIME_LOGS)
    suspend fun GetTimeLogs(@Field("userID") userID: String?): Response<GsonLogs>
}

//interface ApiService {
//    @POST("AppTrainingLogin.htm")
//    suspend fun Login(@Path("userID") userID: String, @Path("password") password: String): Response<AccountDetails>
//    fun GET_TIME_LOGS(@Field("userID") userID: String) : Call<AccountDetails>
//    fun ADD_TIME_LOG(@Field("userID") userID: String) : Call<AccountDetails>
//    fun UPDATE_PROFILE(@Field("userID") userID: String, @Field("firstName") firstName: String,
//                       @Field("middleName") middleName: String, @Field("lastName") lastName: String,
//                       @Field("emailAddress") emailAddress: String, @Field("mobileNumber") mobileNumber: String,
//                       @Field("landline") landline: String): Call<AccountDetails>
//    fun GET_LEAVES(@Field("userID") userID: String) : Call<AccountDetails>
//    fun ADD_LEAVE_WHOLE_DAY(@Field("userID") userID: String, @Field("type") type: String,
//                            @Field("dateFrom") dateFrom: String, @Field("dateTo") dateTo: String,
//                            @Field("time") time: String) : Call<AccountDetails>
//    fun ADD_LEAVE_HALF_DAY(@Field("userID") userID: String, @Field("type") type: String,
//                           @Field("dateFrom") dateFrom: String, @Field("time") time: String) : Call<AccountDetails>
//}


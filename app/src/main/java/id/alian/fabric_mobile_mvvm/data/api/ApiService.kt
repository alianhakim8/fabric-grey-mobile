package id.alian.fabric_mobile_mvvm.data.api

import id.alian.fabric_mobile_mvvm.data.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun signUp(
        @Field("email") email: String,
        @Field("username") userName: String,
        @Field("password") password: String
    ): Response<AuthResponse>
}
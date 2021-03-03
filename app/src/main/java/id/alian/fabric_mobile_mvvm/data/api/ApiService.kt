package id.alian.fabric_mobile_mvvm.data.api

import id.alian.fabric_mobile_mvvm.data.model.AuthResponse
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // auth
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

    // fabric
    @GET("fabric/")
    suspend fun getAllFabric(
        @Header("Authorization") token: String
    ): Response<List<FabricResponse>>

    @POST("fabric/store")
    suspend fun addFabric(
        @Header("Authorization") token: String,
        @Body fabricResponse: FabricResponse
    ): Response<FabricResponse>

    @PUT("fabric/update/{id}")
    suspend fun updateFabric(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body data: FabricResponse
    ): Response<FabricResponse>

    @DELETE("fabric/delete/{id}")
    suspend fun deleteFabric(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}
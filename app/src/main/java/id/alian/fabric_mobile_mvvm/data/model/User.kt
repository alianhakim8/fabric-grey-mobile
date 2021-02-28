package id.alian.fabric_mobile_mvvm.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val userEmail: String,
    @SerializedName("id")
    val userId: Int,
    @SerializedName("name")
    val userName: String,
    val password: String
)
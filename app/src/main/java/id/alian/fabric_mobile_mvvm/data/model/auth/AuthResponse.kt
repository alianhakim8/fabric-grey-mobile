package id.alian.fabric_mobile_mvvm.data.model.auth

data class AuthResponse(
    val message: String,
    val token: String,
    val code: Int
)

package id.alian.fabric_mobile_mvvm.data.repository

import id.alian.fabric_mobile_mvvm.data.api.ApiHelper

class AuthRepository(private val apiHelper: ApiHelper) {
    suspend fun signIn(email: String, password: String) = apiHelper.signIn(email, password)

    suspend fun signUp(email: String, userName: String, password: String) =
        apiHelper.signUp(email, userName, password)
}
package id.alian.fabric_mobile_mvvm.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun signIn(email: String, password: String) = apiService.signIn(email, password)

    suspend fun signUp(email: String, userName: String, password: String) =
        apiService.signUp(email, userName, password)

    suspend fun getAllFabric(token: String) = apiService.getAllFabric(token)
}
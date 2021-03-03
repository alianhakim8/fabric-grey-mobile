package id.alian.fabric_mobile_mvvm.data.api

import id.alian.fabric_mobile_mvvm.data.model.FabricResponse

class ApiHelper(private val apiService: ApiService) {

    // Auth
    suspend fun signIn(email: String, password: String) = apiService.signIn(email, password)

    suspend fun signUp(email: String, userName: String, password: String) =
        apiService.signUp(email, userName, password)

    // Fabric
    suspend fun getAllFabric(token: String) = apiService.getAllFabric(token)

    suspend fun addFabric(token: String, body: FabricResponse) = apiService.addFabric(token, body)

    suspend fun updateFabric(token: String, id: String) = apiService.updateFabric(token, id)

    suspend fun deleteFabric(token: String, id: String) = apiService.deleteFabric(token, id)
}
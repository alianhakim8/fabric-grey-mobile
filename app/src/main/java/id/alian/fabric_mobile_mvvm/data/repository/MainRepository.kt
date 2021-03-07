package id.alian.fabric_mobile_mvvm.data.repository

import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse

class MainRepository(private val api: ApiHelper) {

    // Auth
    suspend fun signIn(email: String, password: String) = api.signIn(email, password)

    suspend fun signUp(email: String, userName: String, password: String) =
        api.signUp(email, userName, password)

    // Fabric
    suspend fun getAllFabric(token: String) = api.getAllFabric(token)

    suspend fun getLastFabric(token: String) = api.getLastFabric(token)

    suspend fun addFabric(token: String, body: FabricResponse) = api.addFabric(token, body)

    suspend fun updateFabric(token: String, id: String, data: FabricResponse) =
        api.updateFabric(token, id, data)

    suspend fun deleteFabric(token: String, id: String) = api.deleteFabric(token, id)
}
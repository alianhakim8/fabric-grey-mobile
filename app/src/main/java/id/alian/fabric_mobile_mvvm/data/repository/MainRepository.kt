package id.alian.fabric_mobile_mvvm.data.repository

import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse

class MainRepository(private val apiHelper: ApiHelper) {

    // Auth
    suspend fun signIn(email: String, password: String) = apiHelper.signIn(email, password)

    suspend fun signUp(email: String, userName: String, password: String) =
        apiHelper.signUp(email, userName, password)

    // Fabric
    suspend fun getAllFabric(token: String) = apiHelper.getAllFabric(token)

    suspend fun addFabric(token: String, body: FabricResponse) = apiHelper.addFabric(token, body)

    suspend fun updateFabric(token: String, id: String, data: FabricResponse) =
        apiHelper.updateFabric(token, id, data)

    suspend fun deleteFabric(token: String, id: String) = apiHelper.deleteFabric(token, id)
}
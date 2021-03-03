package id.alian.fabric_mobile_mvvm.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FabricResponse(
    val id: Int,
    @SerializedName("fabric_type")
    val fabricType: String,
    @SerializedName("machine_id")
    val machineID: Int,
    @SerializedName("brand")
    val fabricBrand: String,
    @SerializedName("po_number")
    val poNumber: Int
) : Serializable
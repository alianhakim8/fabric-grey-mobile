package id.alian.fabric_mobile_mvvm.data.model.fabric

import com.google.gson.annotations.SerializedName

data class FabricDetail(
    val id: Int,
    @SerializedName("fabric_id")
    val fabricID: Int,
    @SerializedName("qty")
    val quantity: Int,
    @SerializedName("oil_dirty")
    val oilDirty: String,
    val slub: Int,
    @SerializedName("hole_small")
    val holeSmall: Int,
    @SerializedName("hole_big")
    val holeBig: Int,
    val description: String,
    val fabric: FabricResponse
)

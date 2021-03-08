package id.alian.fabric_mobile_mvvm.ui.main.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.alian.fabric_mobile_mvvm.data.model.fabric.FabricDetailResponse
import id.alian.fabric_mobile_mvvm.databinding.ItemDetailFabricBinding

class FabricDetailAdapter() : RecyclerView.Adapter<FabricDetailAdapter.ViewHolder>() {

    private var fabricDetailList = emptyList<FabricDetailResponse>()

    inner class ViewHolder(val binding: ItemDetailFabricBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDetailFabricBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvFabricQty.text = "\t: ${fabricDetailList[position].data.quantity}"
        holder.binding.tvFabricDirty.text = "\t: ${fabricDetailList[position].data.oilDirty}"
        holder.binding.tvFabricSlub.text = "\t: ${fabricDetailList[position].data.slub}"
    }

    override fun getItemCount(): Int {
        return fabricDetailList.size
    }

    fun setData(newFabricList: List<FabricDetailResponse>) {
        fabricDetailList = newFabricList
        notifyDataSetChanged()
    }
}
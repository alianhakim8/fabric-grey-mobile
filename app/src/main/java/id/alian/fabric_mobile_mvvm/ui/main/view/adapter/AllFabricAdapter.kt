package id.alian.fabric_mobile_mvvm.ui.main.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.AllFabricItemBinding
import retrofit2.Response

class AllFabricAdapter : RecyclerView.Adapter<AllFabricAdapter.ViewHolder>() {

    private var fabricList = emptyList<FabricResponse>()

    inner class ViewHolder(val binding: AllFabricItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AllFabricItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvFabricType.text = " : ${fabricList[position].fabricType}"
        holder.binding.tvFabricBrand.text = " : ${fabricList[position].fabricBrand}"

        // handle on item click
    }

    override fun getItemCount(): Int {
        return fabricList.size
    }

    fun setData(newFabricList: List<FabricResponse>) {
        fabricList = newFabricList
        notifyDataSetChanged()
    }

}
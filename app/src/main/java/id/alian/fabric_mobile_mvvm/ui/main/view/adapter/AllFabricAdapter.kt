package id.alian.fabric_mobile_mvvm.ui.main.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import id.alian.fabric_mobile_mvvm.data.model.fabric.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.AllFabricItemBinding
import id.alian.fabric_mobile_mvvm.utils.OnItemClickListener
import java.util.*
import kotlin.collections.ArrayList

class AllFabricAdapter(private val itemClick: OnItemClickListener) :
    RecyclerView.Adapter<AllFabricAdapter.ViewHolder>(), Filterable {

    private var fabricList = emptyList<FabricResponse>()
    private var fabricFilterList: List<FabricResponse> = ArrayList()

    init {
        fabricFilterList = fabricList
    }

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

        val id = fabricList[position].id
        val fabricBrand = fabricList[position].fabricBrand
        val fabricType = fabricList[position].fabricType
        val machineId = fabricList[position].machineID
        val poNumber = fabricList[position].poNumber
        val data = FabricResponse(id, fabricType, machineId, fabricBrand, poNumber)

        // handle on item click
        holder.binding.root.setOnClickListener {
            itemClick.onItemClick("detail", data)
        }
        holder.binding.imUpdate.setOnClickListener {
            itemClick.onItemClick("update", data)
        }
    }

    override fun getItemCount(): Int {
        return fabricList.size
    }

    fun setData(newFabricList: List<FabricResponse>) {
        fabricList = newFabricList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (constraint == null || constraint.isEmpty()) {
                    fabricFilterList = fabricList
                } else {
                    val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT)
                        .trim()
                    val resultList = ArrayList<FabricResponse>()
                    for (fabricItem in fabricList) {
                        if (fabricItem.fabricBrand.toLowerCase(Locale.ROOT)
                                .contains(filterPattern)
                        ) {
                            resultList.add(fabricItem)
                        }
                    }
                    fabricFilterList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = fabricFilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                fabricList = results?.values as ArrayList<FabricResponse>
                notifyDataSetChanged()
            }

        }
    }
}
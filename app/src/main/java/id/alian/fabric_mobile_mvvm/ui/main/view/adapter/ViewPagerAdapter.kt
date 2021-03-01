package id.alian.fabric_mobile_mvvm.ui.main.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.alian.fabric_mobile_mvvm.data.model.ViewPagerModel
import id.alian.fabric_mobile_mvvm.databinding.ItemViewPagerBinding

class ViewPagerAdapter(private val data: List<ViewPagerModel>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(binding: ItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val kenBurnsView = binding.kbvFabricMachine
        private val title = binding.tvTitle

        fun setSampleData(data: ViewPagerModel) {
            Picasso.get().load(data.imageUrl).into(kenBurnsView)
            title.text = data.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            ItemViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.setSampleData(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
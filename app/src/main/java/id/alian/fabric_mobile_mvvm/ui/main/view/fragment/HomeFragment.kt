package id.alian.fabric_mobile_mvvm.ui.main.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.data.model.ViewPagerModel
import id.alian.fabric_mobile_mvvm.databinding.FragmentHomeBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.adapter.ViewPagerAdapter
import id.alian.fabric_mobile_mvvm.ui.main.view.fabric.FabricActivity

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var b: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentHomeBinding.bind(view)

        // set image to viewpager2
        setSliderImage()

        val token = activity?.intent?.getStringExtra("token")
        Log.d("TAG", "onViewCreated: $token")
        b.fabric.setOnClickListener {
            Intent(requireContext(), FabricActivity::class.java).also {
                it.putExtra("token", token)
                Log.d("TAG", "onViewCreated: $token")
                startActivity(it)
            }
        }
    }

    private fun setSliderImage() {
        val viewPager2 = b.viewPagerBanner
        val fabricDashboard: ArrayList<ViewPagerModel> = ArrayList()
        val adapter = ViewPagerAdapter(fabricDashboard)
        viewPager2.adapter = adapter

        val sample1 = ViewPagerModel(
            "Fabric Machine 1",
            "https://images.unsplash.com/photo-1612685181136-55a7f2548066?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1297&q=80"
        )
        fabricDashboard.add(sample1)

        val sample2 = ViewPagerModel(
            "Fabric Machine 2",
            "https://images.unsplash.com/photo-1612677257608-90309859bbec?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1489&q=80"
        )
        fabricDashboard.add(sample2)

        val sample3 = ViewPagerModel(
            "Fabric Machine 3",
            "https://images.unsplash.com/photo-1585765530409-3f1f87773b22?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1977&q=80"
        )
        fabricDashboard.add(sample3)
    }

    override fun onResume() {
        super.onResume()
        val token = activity?.intent?.getStringExtra("token")
    }
}
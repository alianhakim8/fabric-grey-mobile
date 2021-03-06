package id.alian.fabric_mobile_mvvm.ui.main.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.data.model.ViewPagerModel
import id.alian.fabric_mobile_mvvm.databinding.FragmentHomeBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.adapter.ViewPagerAdapter
import id.alian.fabric_mobile_mvvm.ui.main.view.fabric.FabricActivity
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var b: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentHomeBinding.bind(view)

        // set image to viewpager2
        setSliderImage()

        setupViewModel()

        val token = activity?.intent?.getStringExtra("token")
        Log.d("TAG", "onViewCreated: $token")
        getLastFabric(token!!)
        b.fabric.setOnClickListener {
            Intent(requireContext(), FabricActivity::class.java).also {
                it.putExtra("token", token)
                Log.d("TAG", "onViewCreated: $token")
                startActivity(it)
            }
        }
        greeting()
    }

    private fun setSliderImage() {
        val viewPager2 = b.viewPagerBanner
        val fabricDashboard: ArrayList<ViewPagerModel> = ArrayList()
        val adapter = ViewPagerAdapter(fabricDashboard)
        viewPager2.adapter = adapter

        val sample1 = ViewPagerModel(
            "Fabric Machine",
            "https://images.unsplash.com/photo-1612685181136-55a7f2548066?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1297&q=80"
        )
        fabricDashboard.add(sample1)
    }

    @SuppressLint("SetTextI18n")
    private fun getLastFabric(token: String) {
        if (context?.connect() == true) {
            viewModel.getLastFabric(token).observe(this, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        b.progressBar.visibility = View.GONE
                        b.ponumber.visibility = View.VISIBLE
                        b.fabricType.visibility = View.VISIBLE
                        b.tvMachine.visibility = View.VISIBLE
                        b.ponumber.text = resource.data?.body()?.data?.poNumber.toString()
                        b.fabricType.text =
                            "Fabric Type\t: ${resource.data?.body()?.data?.fabricType.toString()}"
                        b.tvMachine.text =
                            "Machine\t: ${resource.data?.body()?.data?.machineID.toString()}"
                    }

                    Status.LOADING -> {
                        b.progressBar.visibility = View.VISIBLE
                        b.ponumber.visibility = View.GONE
                        b.fabricType.visibility = View.GONE
                        b.tvMachine.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        b.progressBar.visibility = View.GONE
                        b.fabricType.visibility = View.VISIBLE
                        b.tvMachine.visibility = View.VISIBLE
                        b.ponumber.text = "Error to get last fabric"
                    }
                }
            })
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        val token = activity?.intent?.getStringExtra("token")
        getLastFabric(token!!)
    }

    private fun greeting() {
        val greeting: String
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        val hour = cal[Calendar.HOUR_OF_DAY]
        greeting = when (hour) {
            in 12..16 -> {
                "Good Afternoon,";
            }
            in 17..20 -> {
                "Good Evening,";
            }
            in 21..23 -> {
                "Good Night,";
            }
            else -> {
                "Good Morning,";
            }
        }
        b.topAppBar.subtitle = "$greeting User"

    }

}
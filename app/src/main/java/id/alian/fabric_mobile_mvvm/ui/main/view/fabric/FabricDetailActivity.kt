package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.data.model.fabric.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.ActivityFabricDetailBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.adapter.FabricDetailAdapter
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Global
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect

class FabricDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFabricDetailBinding
    private lateinit var viewModel: MainViewModel

    private val fabricAdapter by lazy {
        FabricDetailAdapter()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFabricDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getSerializableExtra(Global.DATA) as FabricResponse

        binding.tvFabricBrand.text = "\t: ${data.fabricBrand}"
        binding.tvFabricType.text = "\t: ${data.fabricType}"
        binding.tvMachine.text = "\t: ${data.machineID}"
        binding.tvPoNumber.text = "\t: ${data.poNumber}"

        val id = data.id.toString()
        setupViewModel()
        setupRecyclerView()
        getFabricDetail("test", id)

        binding.tbFabricDetail.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = fabricAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("SetTextI18n")
    private fun getFabricDetail(token: String, id: String) {
        viewModel.getFabricDetail(token, id).observe(this, {
            if (this.connect()) {
                viewModel.getFabricDetail("Bearer $token", id).observe(this, { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.body().also {
                                binding.recyclerView.visibility = View.VISIBLE
                                if (it != null) {
                                    fabricAdapter.setData(it)
                                } else {
                                    binding.recyclerView.visibility = View.GONE
                                }
                                Log.d("TAG", "setupObservers: $it")
                            }
                        }
                        Status.LOADING -> {
                            binding.recyclerView.visibility = View.GONE
                        }

                        Status.ERROR -> {
                            binding.recyclerView.visibility = View.GONE
                            Log.d("TAG", "setupObservers: ${resource.message}")
                        }
                        else -> TODO()
                    }
                })
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.tvFabricType.text = "No Internet"
            }
        })
    }

}
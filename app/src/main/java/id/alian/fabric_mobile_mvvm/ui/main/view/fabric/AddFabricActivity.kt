package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.ActivityAddFabricBinding
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Global
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect

class AddFabricActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFabricBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFabricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = intent.getStringExtra(Global.TOKEN).toString()

        setupViewModel()
        binding.btnAdd.setOnClickListener {
            val fabricType = binding.etFabricType.text.toString().trim()
            val fabricBrand = binding.etFabricBrand.text.toString().trim()
            val machineId = Integer.parseInt(binding.etMachineId.text.toString().trim())
            val poNumber = Integer.parseInt(binding.etPoNumber.text.toString().trim())
            val body = FabricResponse(0, fabricType, machineId, fabricBrand, poNumber)
            addFabric(token, body)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun addFabric(token: String, body: FabricResponse) {
        if (this.connect()) {
            viewModel.addFabric("Bearer $token", body).observe(this, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnAdd.visibility = View.VISIBLE
                        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnAdd.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnAdd.visibility = View.VISIBLE
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> TODO()
                }
            })
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

}
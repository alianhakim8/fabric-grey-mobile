package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.content.Intent
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
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect

class AddFabricActivity : AppCompatActivity() {

    private lateinit var b: ActivityAddFabricBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAddFabricBinding.inflate(layoutInflater)
        setContentView(b.root)

        val token = intent.getStringExtra("token").toString()

        setupViewModel()
        b.btnAdd.setOnClickListener {
            val fabricType = b.etFabricType.text.toString().trim()
            val fabricBrand = b.etFabricBrand.text.toString().trim()
            val machineId = Integer.parseInt(b.etMachineId.text.toString().trim())
            val poNumber = Integer.parseInt(b.etPoNumber.text.toString().trim())
            val body = FabricResponse(0, fabricType, machineId, fabricBrand, poNumber)
            addFabric(token, body)
        }

        b.tbAddFabric.setNavigationOnClickListener {
            Intent(this, FabricActivity::class.java).also {
                startActivity(it)
                finish()
            }
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
                        b.progressBar.visibility = View.GONE
                        b.btnAdd.visibility = View.VISIBLE
                        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    Status.LOADING -> {
                        b.progressBar.visibility = View.VISIBLE
                        b.btnAdd.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        b.progressBar.visibility = View.GONE
                        b.btnAdd.visibility = View.VISIBLE
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

}
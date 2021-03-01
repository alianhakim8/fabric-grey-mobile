package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.ActivityAddFabricBinding
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status

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
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun connect(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun addFabric(token: String, body: FabricResponse) {
        if (connect()) {
            viewModel.addFabric(token, body).observe(this, { resource ->
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
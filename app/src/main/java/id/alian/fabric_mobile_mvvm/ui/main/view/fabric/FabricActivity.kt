package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.databinding.ActivityFabricBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.adapter.AllFabricAdapter
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status

class FabricActivity : AppCompatActivity() {

    private lateinit var b: ActivityFabricBinding
    private lateinit var viewModel: MainViewModel

    private val fabricAdapter by lazy {
        AllFabricAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFabricBinding.inflate(layoutInflater)
        setContentView(b.root)

        val token = intent.getStringExtra("token").toString()
        setupViewModel()
        setupRecyclerView()
        if (connect()) {
            setupObservers(token)
        } else {
            b.recyclerView.visibility = View.GONE
            b.progressBar.visibility = View.GONE
            b.fabAddFabric.visibility = View.GONE
            b.imNoInternet.visibility = View.VISIBLE
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupObservers(token: String) {
        viewModel.getAllFabric(token).observe(this, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.body().let {
                        if (it != null) {
                            b.progressBar.visibility = View.GONE
                            b.recyclerView.visibility = View.VISIBLE
                            b.fabAddFabric.visibility = View.VISIBLE
                            fabricAdapter.setData(it)
                        } else {
                            b.progressBar.visibility = View.GONE
                            b.recyclerView.visibility = View.GONE
                            b.fabAddFabric.visibility = View.GONE
                            b.imErrorNotification.visibility = View.VISIBLE
                            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Status.LOADING -> {
                    b.progressBar.visibility = View.VISIBLE
                    b.recyclerView.visibility = View.GONE
                    b.fabAddFabric.visibility = View.GONE
                }

                Status.ERROR -> {
                    b.progressBar.visibility = View.GONE
                    b.recyclerView.visibility = View.GONE
                    b.fabAddFabric.visibility = View.GONE
                    b.imErrorNotification.visibility = View.VISIBLE
                }
            }
        })
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

    private fun setupRecyclerView() {
        b.recyclerView.adapter = fabricAdapter
        b.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
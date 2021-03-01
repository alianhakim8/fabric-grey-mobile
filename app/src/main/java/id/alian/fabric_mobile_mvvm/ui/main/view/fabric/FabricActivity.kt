package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        Log.d("TAG", "onCreate: $token")
        setupViewModel()
        setupRecyclerView()
        getFabrics(token)

        b.swipeRefresh.setOnRefreshListener {
            getFabrics(token)
        }

        b.fabAddFabric.setOnClickListener {
            Intent(this, AddFabricActivity::class.java).also {
                it.putExtra("token", token)
                startActivity(it)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupRecyclerView() {
        b.recyclerView.adapter = fabricAdapter
        b.recyclerView.layoutManager = LinearLayoutManager(this)
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

    private fun getFabrics(token: String) {
        if (connect()) {
            viewModel.getAllFabric("Bearer $token").observe(this, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.body().also {
                            b.recyclerView.visibility = View.VISIBLE
                            b.fabAddFabric.visibility = View.VISIBLE
                            if (it != null) {
                                b.swipeRefresh.isRefreshing = false
                                b.laNotFound.visibility = View.GONE
                                fabricAdapter.setData(it)
                            } else {
                                b.swipeRefresh.isRefreshing = false
                                b.recyclerView.visibility = View.GONE
                                b.fabAddFabric.visibility = View.GONE
                                b.laNotFound.visibility = View.VISIBLE
                            }
                            Log.d("TAG", "setupObservers: $it")
                        }
                    }
                    Status.LOADING -> {
                        b.swipeRefresh.isRefreshing = true
                        b.recyclerView.visibility = View.GONE
                        b.fabAddFabric.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        b.swipeRefresh.isRefreshing = false
                        b.recyclerView.visibility = View.GONE
                        b.fabAddFabric.visibility = View.GONE
                        b.laNotFound.visibility = View.VISIBLE
                        Log.d("TAG", "setupObservers: ${resource.message}")
                    }
                }
            })
        } else {
            b.recyclerView.visibility = View.GONE
            b.fabAddFabric.visibility = View.GONE
            b.laNotFound.visibility = View.VISIBLE
            b.swipeRefresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        val token = intent.getStringExtra("token").toString()
        getFabrics(token)
    }
}
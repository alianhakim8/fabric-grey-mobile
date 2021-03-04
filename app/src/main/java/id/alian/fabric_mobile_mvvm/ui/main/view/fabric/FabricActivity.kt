package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.ActivityFabricBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.DashboardActivity
import id.alian.fabric_mobile_mvvm.ui.main.view.adapter.AllFabricAdapter
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.OnItemClickListener
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect
import id.alian.fabric_mobile_mvvm.utils.hideKeyboard

class FabricActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var b: ActivityFabricBinding
    private lateinit var viewModel: MainViewModel

    private val fabricAdapter by lazy {
        AllFabricAdapter(this)
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
        searchFabric(token)

        b.fabAddFabric.setOnClickListener {
            Intent(this, AddFabricActivity::class.java).also {
                it.putExtra("token", token)
                startActivity(it)
            }
        }

        b.tbFabric.setNavigationOnClickListener {
            Intent(this, DashboardActivity::class.java).also {
                it.putExtra("token", token)
                startActivity(it)
                finish()
            }
        }
    }

    private fun searchFabric(token: String) {
        b.svFabric.queryHint = "Search Brand..."
        b.svFabric.isFocusable = false;
        b.svFabric.isIconified = false;
        b.svFabric.clearFocus();
        b.svFabric.imeOptions = EditorInfo.IME_ACTION_DONE
        b.svFabric.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                b.svFabric.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isEmpty() == true) {
                    getFabrics(token)
                    b.svFabric.clearFocus()
                    b.svFabric.imeOptions = EditorInfo.IME_ACTION_DONE
                } else {
                    fabricAdapter.filter.filter(newText)
                    b.svFabric.imeOptions = EditorInfo.IME_ACTION_DONE
                }
                return false
            }
        })


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

    private fun getFabrics(token: String) {
        if (this.connect()) {
            viewModel.getAllFabric("Bearer $token").observe(this, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.body().also {
                            b.progressBar.visibility = View.GONE
                            b.recyclerView.visibility = View.VISIBLE
                            b.fabAddFabric.visibility = View.VISIBLE
                            if (it != null) {
                                b.progressBar.visibility = View.GONE
                                b.laNotFound.visibility = View.GONE
                                fabricAdapter.setData(it)
                            } else {
                                b.progressBar.visibility = View.GONE
                                b.recyclerView.visibility = View.GONE
                                b.fabAddFabric.visibility = View.GONE
                                b.laNotFound.visibility = View.VISIBLE
                            }
                            Log.d("TAG", "setupObservers: $it")
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
                        b.laNotFound.visibility = View.VISIBLE
                        Log.d("TAG", "setupObservers: ${resource.message}")
                    }
                }
            })
        } else {
            b.progressBar.visibility = View.GONE
            b.recyclerView.visibility = View.GONE
            b.fabAddFabric.visibility = View.GONE
            b.laNotFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        val token = intent.getStringExtra("token").toString()
        viewModel.getAllFabric("Bearer $token").observe(this, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.body().also {
                        b.progressBar.visibility = View.GONE
                        b.recyclerView.visibility = View.VISIBLE
                        b.fabAddFabric.visibility = View.VISIBLE
                        if (it != null) {
                            b.progressBar.visibility = View.GONE
                            b.laNotFound.visibility = View.GONE
                            fabricAdapter.setData(it)
                        } else {
                            b.progressBar.visibility = View.GONE
                            b.recyclerView.visibility = View.GONE
                            b.fabAddFabric.visibility = View.GONE
                            b.laNotFound.visibility = View.VISIBLE
                        }
                        Log.d("TAG", "setupObservers: $it")
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
                    b.laNotFound.visibility = View.VISIBLE
                    Log.d("TAG", "setupObservers: ${resource.message}")
                }
            }
        })
    }

    override fun onItemClick(status: String, data: FabricResponse?) {
        val token = intent.getStringExtra("token").toString()
        if (status == "update") {
            Intent(this, UpdateFabricActivity::class.java).also {
                it.putExtra("token", token)
                it.putExtra("data", data)
                startActivity(it)
            }
        } else {
            Intent(this, FabricDetailActivity::class.java).also {
                it.putExtra("token", token)
                it.putExtra("data", data)
                startActivity(it)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
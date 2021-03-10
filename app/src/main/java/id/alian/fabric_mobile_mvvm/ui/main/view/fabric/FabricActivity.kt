package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.data.model.fabric.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.ActivityFabricBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.adapter.AllFabricAdapter
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Global
import id.alian.fabric_mobile_mvvm.utils.OnItemClickListener
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect

class FabricActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityFabricBinding
    private lateinit var viewModel: MainViewModel

    private val fabricAdapter by lazy {
        AllFabricAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFabricBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = intent.getStringExtra("token").toString()
        Log.d("TAG", "onCreate: $token")
        setupViewModel()
        setupRecyclerView()
        getFabrics(token)
        searchFabric(token)

        binding.fabAddFabric.setOnClickListener {
            Intent(this, AddFabricActivity::class.java).also {
                it.putExtra("token", token)
                startActivity(it)
            }
        }
    }

    private fun searchFabric(token: String) {
        binding.svFabric.queryHint = "Search Brand..."
        binding.svFabric.isFocusable = false
        binding.svFabric.isIconified = false
        binding.svFabric.clearFocus()
        binding.svFabric.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.svFabric.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svFabric.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isEmpty() == true) {
                    getFabrics(token)
                } else {
                    fabricAdapter.filter.filter(newText)
                    binding.svFabric.imeOptions = EditorInfo.IME_ACTION_DONE
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
        binding.recyclerView.adapter = fabricAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.fabAddFabric.animate().translationY(
                        binding.fabAddFabric.height +
                                resources.getDimension(R.dimen.app_icon_size)
                    ).setInterpolator(LinearInterpolator()).duration = 200
                } else if (dy < 0) {
                    binding.fabAddFabric.animate().translationY(0f)
                        .setInterpolator(LinearInterpolator()).duration = 200
                }
            }
        })
    }

    private fun getFabrics(token: String) {
        if (this.connect()) {
            viewModel.getAllFabric("Bearer $token").observe(this, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.body().also {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.fabAddFabric.visibility = View.VISIBLE
                            if (it != null) {
                                binding.progressBar.visibility = View.GONE
                                binding.laNotFound.visibility = View.GONE
                                fabricAdapter.setData(it)
                            } else {
                                binding.progressBar.visibility = View.GONE
                                binding.recyclerView.visibility = View.GONE
                                binding.fabAddFabric.visibility = View.GONE
                                binding.laNotFound.visibility = View.VISIBLE
                            }
                            Log.d("TAG", "setupObservers: $it")
                        }
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.fabAddFabric.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.fabAddFabric.visibility = View.GONE
                        binding.laNotFound.visibility = View.VISIBLE
                        Log.d("TAG", "setupObservers: ${resource.message}")
                    }
                    else -> TODO()
                }
            })
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.fabAddFabric.visibility = View.GONE
            binding.laNotFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        val token = intent.getStringExtra("token").toString()
        viewModel.getAllFabric("Bearer $token").observe(this, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.body().also {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.fabAddFabric.visibility = View.VISIBLE
                        if (it != null) {
                            binding.progressBar.visibility = View.GONE
                            binding.laNotFound.visibility = View.GONE
                            fabricAdapter.setData(it)
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.GONE
                            binding.fabAddFabric.visibility = View.GONE
                            binding.laNotFound.visibility = View.VISIBLE
                        }
                        Log.d("TAG", "setupObservers: $it")
                    }
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.fabAddFabric.visibility = View.GONE
                }

                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.fabAddFabric.visibility = View.GONE
                    binding.laNotFound.visibility = View.VISIBLE
                    Log.d("TAG", "setupObservers: ${resource.message}")
                }
                else -> TODO()
            }
        })
    }

    override fun onItemClick(status: String, data: FabricResponse?) {
        val token = intent.getStringExtra(Global.TOKEN).toString()
        if (status == "update") {
            Intent(this, UpdateFabricActivity::class.java).also {
                it.putExtra(Global.TOKEN, token)
                it.putExtra(Global.DATA, data)
                startActivity(it)
            }
        } else {
            Intent(this, FabricDetailActivity::class.java).also {
                it.putExtra(Global.TOKEN, token)
                it.putExtra(Global.DATA, data)
                startActivity(it)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
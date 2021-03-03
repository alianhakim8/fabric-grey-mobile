package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.ActivityUpdateFabricBinding
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect
import id.alian.fabric_mobile_mvvm.utils.hideKeyboard

class UpdateFabricActivity : AppCompatActivity() {

    private lateinit var b: ActivityUpdateFabricBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityUpdateFabricBinding.inflate(layoutInflater)
        setContentView(b.root)
        setupViewModel()

        val token = intent.getStringExtra("token").toString()
        val data = intent.getSerializableExtra("data") as FabricResponse
        val id = data.id.toString()
        deleteFabric(token, id)
        b.etUpdateType.editText?.setText(data.fabricType)
        b.etUpdateType.editText?.setText(data.fabricBrand)
        b.etUpdateType.editText?.setText(data.machineID)
        b.etUpdateType.editText?.setText(data.poNumber)

        b.btnUpdate.setOnClickListener {
            updateFabric(token, id)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun deleteFabric(token: String, id: String) {
        b.tbUpdateFabric.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(resources.getString(R.string.title_dialog))
                        .setMessage(resources.getString(R.string.supporting_text))
                        .setNegativeButton(resources.getString(R.string.decline)) { dialog, _ ->
                            dialog.cancel()
                        }
                        .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                            // delete fabric
                            deleteObservers(token, id)
                            finish()
                        }
                        .show()
                    this.hideKeyboard(b.root)
                    true
                }
                else -> false
            }
        }

    }

    private fun deleteObservers(token: String, id: String) {
        if (this.connect()) {
            viewModel.deleteFabric("Bearer $token", id)
                .observe(this, { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Toast.makeText(
                                this,
                                "Delete Success",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        Status.LOADING -> {
                            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()

                        }

                        Status.ERROR -> {
                            Toast.makeText(this, "Failed Delete", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFabric(token: String, id: String) {
        viewModel.updateFabric(token, id).observe(this, {
            if (this.connect()) {
                viewModel.deleteFabric("Bearer $token", id)
                    .observe(this, { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                Toast.makeText(
                                    this,
                                    "Update Success",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            Status.LOADING -> {
                                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()

                            }

                            Status.ERROR -> {
                                Toast.makeText(this, "Failed Delete", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
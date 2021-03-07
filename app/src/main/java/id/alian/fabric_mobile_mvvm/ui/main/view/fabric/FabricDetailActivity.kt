package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.ActivityFabricDetailBinding
import id.alian.fabric_mobile_mvvm.utils.Global

class FabricDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFabricDetailBinding

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

        binding.tbFabricDetail.setNavigationOnClickListener {
            finish()
        }
    }
}
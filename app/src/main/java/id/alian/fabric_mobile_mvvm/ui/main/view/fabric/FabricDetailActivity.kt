package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.alian.fabric_mobile_mvvm.data.model.FabricResponse
import id.alian.fabric_mobile_mvvm.databinding.ActivityFabricDetailBinding

class FabricDetailActivity : AppCompatActivity() {

    private lateinit var b: ActivityFabricDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFabricDetailBinding.inflate(layoutInflater)
        setContentView(b.root)

        val data = intent.getSerializableExtra("data") as FabricResponse

        b.tvFabricBrand.text = "\t: ${data.fabricBrand}"
        b.tvFabricType.text = "\t: ${data.fabricType}"
        b.tvMachine.text = "\t: ${data.machineID.toString()}"
        b.tvPoNumber.text = "\t: ${data.poNumber.toString()}"

        b.tbFabricDetail.setNavigationOnClickListener {
            finish()
        }
    }
}
package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.alian.fabric_mobile_mvvm.databinding.ActivityFabricDetailBinding

class FabricDetailActivity : AppCompatActivity() {

    private lateinit var b: ActivityFabricDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFabricDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}
package id.alian.fabric_mobile_mvvm.ui.main.view.fabric

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.alian.fabric_mobile_mvvm.databinding.ActivityAddFabricDetailBinding

class AddFabricDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFabricDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFabricDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
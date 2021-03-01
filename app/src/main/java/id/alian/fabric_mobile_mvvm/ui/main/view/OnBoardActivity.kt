package id.alian.fabric_mobile_mvvm.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.alian.fabric_mobile_mvvm.databinding.ActivityOnBoardBinding

class OnBoardActivity : AppCompatActivity() {

    private lateinit var b: ActivityOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityOnBoardBinding.inflate(layoutInflater)
        actionBar?.hide()
        setContentView(b.root)
        onButtonClick()
    }

    private fun onButtonClick() {
        b.btnToSignIn.setOnClickListener {
            Intent(this, AuthActivity::class.java).also {
                it.putExtra("status", "sign in")
                startActivity(it)
            }
            finish()
        }

        b.btnToSignUp.setOnClickListener {
            Intent(this, AuthActivity::class.java).also {
                it.putExtra("status", "sign up")
                startActivity(it)
            }
            finish()
        }
    }
}
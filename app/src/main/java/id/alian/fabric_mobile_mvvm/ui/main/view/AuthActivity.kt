package id.alian.fabric_mobile_mvvm.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.databinding.ActivityAuthBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.fragment.SignInFragment
import id.alian.fabric_mobile_mvvm.ui.main.view.fragment.SignUpFragment

class AuthActivity : AppCompatActivity() {

    private lateinit var b: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(b.root)

        actionBar?.hide()

        val status = intent.getStringExtra("status")
        val signInFragment = SignInFragment()
        val signUpFragment = SignUpFragment()

        if (status == "sign in") {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, signInFragment)
                commit()
            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, signUpFragment)
                commit()
            }
        }
    }


}
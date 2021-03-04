package id.alian.fabric_mobile_mvvm.ui.main.view.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.databinding.FragmentSignInBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.DashboardActivity
import id.alian.fabric_mobile_mvvm.ui.main.view.MainActivity
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect
import id.alian.fabric_mobile_mvvm.utils.hideKeyboard

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var b: FragmentSignInBinding
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentSignInBinding.bind(view)
        setupViewModel()
        loginTextWatcher()

        b.btnSignIn.setOnClickListener {
            signIn()
        }

        // icon back button on click listener
        b.materialToolbar.setNavigationOnClickListener {
            it.findNavController().navigate(R.id.action_signInFragment_to_onBoardFragment)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun loginTextWatcher() {
        val email = b.etEmail.editText?.text.toString().trim()
        val password = b.etPassword.editText?.text.toString().trim()

        if (email.isEmpty() && password.isEmpty()) {
            b.etEmail.editText?.doOnTextChanged { text, _, _, _ ->
                b.btnSignIn.isEnabled = !(text.isNullOrEmpty())
            }

            b.etPassword.editText?.doOnTextChanged { text, _, _, _ ->
                b.btnSignIn.isEnabled = !(text.isNullOrEmpty())
            }
        }
    }

    private fun signIn() {
        if (context?.connect() == true) {
            context!!.hideKeyboard(b.root)
            val email = b.etEmail.editText?.text.toString().trim()
            val password = b.etPassword.editText?.text.toString().trim()

            viewModel.signIn(email, password).observe(this, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        b.progressBar.visibility = View.GONE
                        b.btnSignIn.visibility = View.VISIBLE
                        val token = resource.data?.body()?.token
                        if (token != null) {
                            Intent(context, DashboardActivity::class.java).also {
                                it.putExtra("token", token)
                                startActivity(it)
                                activity?.finish()
                            }
                        } else {
                            b.progressBar.visibility = View.GONE
                            b.btnSignIn.visibility = View.VISIBLE
                            Toast.makeText(
                                context,
                                "Email or Password Incorrect",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    Status.LOADING -> {
                        b.progressBar.visibility = View.VISIBLE
                        b.btnSignIn.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        b.btnSignIn.visibility = View.VISIBLE
                        Toast.makeText(
                            context,
                            resource.data?.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } else {
            context?.hideKeyboard(b.root)
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
        }
    }
}
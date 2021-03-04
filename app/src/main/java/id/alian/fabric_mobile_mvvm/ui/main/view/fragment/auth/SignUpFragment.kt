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
import id.alian.fabric_mobile_mvvm.databinding.FragmentSignUpBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.DashboardActivity
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.connect
import id.alian.fabric_mobile_mvvm.utils.hideKeyboard

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var b: FragmentSignUpBinding
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentSignUpBinding.bind(view)
        setupViewModel()
        loginTextWatcher()

        b.btnSignUp.setOnClickListener {
            signUp()
        }

        // icon back button on click listener
        b.materialToolbar.setNavigationOnClickListener {
            it.findNavController().navigate(R.id.action_signUpFragment_to_onBoardFragment)
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
                b.btnSignUp.isEnabled = !(text.isNullOrEmpty())
            }

            b.etPassword.editText?.doOnTextChanged { text, _, _, _ ->
                b.btnSignUp.isEnabled = !(text.isNullOrEmpty())
            }
        }
    }

    private fun signUp() {
        if (context?.connect() == true) {
            context?.hideKeyboard(b.root)
            val email = b.etEmail.editText?.text.toString().trim()
            val userName = b.etPassword.editText?.text.toString().trim()
            val password = b.etPassword.editText?.text.toString().trim()
            viewModel.signUp(email, userName, password).observe(this, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        b.progressBar.visibility = View.GONE
                        b.btnSignUp.visibility = View.VISIBLE
                        val token = resource.data?.body()?.token
                        if (token != null) {
                            Intent(context, DashboardActivity::class.java).also {
                                it.putExtra("token", token)
                                startActivity(it)
                                activity?.finish()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Email already exists",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    Status.LOADING -> {
                        b.progressBar.visibility = View.VISIBLE
                        b.btnSignUp.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        b.progressBar.visibility = View.GONE
                        b.btnSignUp.visibility = View.VISIBLE
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            context?.hideKeyboard(b.root)
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
        }
    }
}
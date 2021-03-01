package id.alian.fabric_mobile_mvvm.ui.main.view.fragment.auth

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.databinding.FragmentSignUpBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.DashboardActivity
import id.alian.fabric_mobile_mvvm.ui.main.view.OnBoardActivity
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.AuthViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.hideKeyboard

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var b: FragmentSignUpBinding
    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentSignUpBinding.bind(view)
        setupViewModel()
        loginTextWatcher()

        b.btnSignUp.setOnClickListener {
            requireContext().hideKeyboard(b.root)
            val email = b.etEmail.editText?.text.toString().trim()
            val userName = b.etPassword.editText?.text.toString().trim()
            val password = b.etPassword.editText?.text.toString().trim()
            if (connect()) {
                viewModel.signUp(email, userName, password).observe(this, { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            b.progressBar.visibility = View.GONE
                            b.btnSignUp.visibility = View.VISIBLE
                            val token = resource.data?.body()?.token
                            if (token != null) {
                                Intent(requireContext(), DashboardActivity::class.java).also {
                                    it.putExtra("token", token)
                                    startActivity(it)
                                    requireActivity().finish()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
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
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else {
                requireContext().hideKeyboard(b.root)
                Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
            }
        }

        // icon back button on click listener
        b.materialToolbar.setNavigationOnClickListener {
            Intent(requireContext(), OnBoardActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(AuthViewModel::class.java)
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

    private fun connect(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}
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
import id.alian.fabric_mobile_mvvm.databinding.FragmentSignInBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.DashboardActivity
import id.alian.fabric_mobile_mvvm.ui.main.view.OnBoardActivity
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.MainViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status
import id.alian.fabric_mobile_mvvm.utils.hideKeyboard

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var b: FragmentSignInBinding
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentSignInBinding.bind(view)

        setupViewModel()
        loginTextWatcher()

        b.btnLogin.setOnClickListener {
            if (connect()) {
                requireContext().hideKeyboard(b.root)
                val email = b.etEmail.editText?.text.toString().trim()
                val password = b.etPassword.editText?.text.toString().trim()

                viewModel.signIn(email, password).observe(this, { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            b.progressBar.visibility = View.GONE
                            b.btnLogin.visibility = View.VISIBLE
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
                                    "Email or Password Incorrect",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        Status.LOADING -> {
                            b.progressBar.visibility = View.VISIBLE
                            b.btnLogin.visibility = View.GONE
                        }

                        Status.ERROR -> {
                            b.progressBar.visibility = View.GONE
                            b.btnLogin.visibility = View.VISIBLE
                            Toast.makeText(
                                requireContext(),
                                resource.data?.message(),
                                Toast.LENGTH_SHORT
                            ).show()
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
        ).get(MainViewModel::class.java)
    }

    private fun loginTextWatcher() {
        val email = b.etEmail.editText?.text.toString().trim()
        val password = b.etPassword.editText?.text.toString().trim()

        if (email.isEmpty() && password.isEmpty()) {
            b.etEmail.editText?.doOnTextChanged { text, _, _, _ ->
                b.btnLogin.isEnabled = !(text.isNullOrEmpty())
            }

            b.etPassword.editText?.doOnTextChanged { text, _, _, _ ->
                b.btnLogin.isEnabled = !(text.isNullOrEmpty())
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
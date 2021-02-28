package id.alian.fabric_mobile_mvvm.ui.main.view.fragment

import android.content.Context
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
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.AuthViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var b: FragmentSignInBinding
    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentSignInBinding.bind(view)

        setupViewModel()
        loginTextWatcher()
        b.btnLogin.setOnClickListener {
            setupObservers()
        }
    }

    private fun setupObservers() {
        if (connect()) {
            val email = b.etEmail.editText?.text.toString().trim()
            val password = b.etPassword.editText?.text.toString().trim()
            viewModel.signIn(email, password).observe(requireActivity(), {
                if (it.data?.isSuccessful == true) {
                    it?.let {
                        Toast.makeText(
                            requireContext(),
                            it.data?.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("AUTH", "setupObservers: ${it.data?.body()?.token}")
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiString))
        ).get(AuthViewModel::class.java)
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
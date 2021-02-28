package id.alian.fabric_mobile_mvvm.ui.main.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.api.RetrofitBuilder
import id.alian.fabric_mobile_mvvm.databinding.FragmentSignUpBinding
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.AuthViewModel
import id.alian.fabric_mobile_mvvm.ui.main.viewmodel.ViewModelFactory
import id.alian.fabric_mobile_mvvm.utils.Status
import retrofit2.HttpException

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var b: FragmentSignUpBinding
    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b = FragmentSignUpBinding.bind(view)
        setupViewModel()
        loginTextWatcher()

        b.btnSignUp.setOnClickListener {
            setupObservers()
        }
    }

    private fun setupObservers() {
        val email = b.etEmail.editText?.text.toString().trim()
        val userName = b.etPassword.editText?.text.toString().trim()
        val password = b.etPassword.editText?.text.toString().trim()
        viewModel.signUp(email, userName, password).observe(this, Observer {
            try {
                if (it.data?.isSuccessful!!) {
                    it?.let {
                        when (it.status) {
                            Status.SUCCESS -> {
                                Log.d("AUTH", "setupObservers: ${it.data?.body()?.token}")
                                Toast.makeText(
                                    requireContext(),
                                    it.data?.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Status.ERROR -> {
                                b.progressBar.visibility = View.GONE
                                b.btnSignUp.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                            Status.LOADING -> {
                                b.progressBar.visibility = View.VISIBLE
                                b.btnSignUp.visibility = View.GONE
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(
                    requireContext(),
                    "Exception",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Throwable) {
                Toast.makeText(
                    requireContext(),
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

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
                b.btnSignUp.isEnabled = !(text.isNullOrEmpty())
            }

            b.etPassword.editText?.doOnTextChanged { text, _, _, _ ->
                b.btnSignUp.isEnabled = !(text.isNullOrEmpty())
            }
        }
    }
}
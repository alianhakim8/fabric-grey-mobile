package id.alian.fabric_mobile_mvvm.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.alian.fabric_mobile_mvvm.data.api.ApiHelper
import id.alian.fabric_mobile_mvvm.data.repository.AuthRepository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(AuthRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
package id.alian.fabric_mobile_mvvm.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.alian.fabric_mobile_mvvm.data.repository.AuthRepository
import id.alian.fabric_mobile_mvvm.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun signIn(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = authRepository.signIn(email, password)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred !"))
        }
    }

    fun signUp(email: String, userName: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = authRepository.signUp(email, userName, password)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred"))
        }
    }
}
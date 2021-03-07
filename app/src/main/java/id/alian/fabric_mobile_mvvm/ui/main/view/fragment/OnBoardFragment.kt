package id.alian.fabric_mobile_mvvm.ui.main.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.databinding.FragmentOnBoardBinding

class OnBoardFragment : Fragment(R.layout.fragment_on_board) {

    private lateinit var binding: FragmentOnBoardBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnBoardBinding.bind(view)

        binding.btnToSignIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_onBoardFragment_to_signInFragment)
        }
        binding.btnToSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_onBoardFragment_to_signUpFragment)
        }
    }
}
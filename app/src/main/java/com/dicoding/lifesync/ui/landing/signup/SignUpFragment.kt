package com.dicoding.lifesync.ui.landing.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.dicoding.lifesync.R
import com.dicoding.lifesync.data.Result
import com.dicoding.lifesync.databinding.FragmentSignUpBinding
import com.dicoding.lifesync.utils.ViewModelFactory

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val signUpViewModel by viewModels<SignUpViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btSignUpBack.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_signUpFragment_to_landingFragment)
        )

        binding.btSignUp.setOnClickListener {v ->
            signUpViewModel.register(
                binding.tfSignUpUsername.editText?.text.toString(),
                binding.tfSignUpEmail.editText?.text.toString(),
                binding.tfSignUpPassword.editText?.text.toString()
            ).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {

                        }
                        is Result.Success -> {
                            v.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                        }
                        is Result.Error -> {
                            if (result.error == "409") {
                                Toast.makeText(requireActivity(), "User already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireActivity(), "Login Error", Toast.LENGTH_SHORT).show()
                            }
                            Log.e("Sign In", result.error)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
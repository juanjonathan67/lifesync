package com.dicoding.lifesync.ui.landing.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.dicoding.lifesync.R
import com.dicoding.lifesync.data.Result
import com.dicoding.lifesync.databinding.FragmentSignInBinding
import com.dicoding.lifesync.ui.main.MainActivity
import com.dicoding.lifesync.utils.ViewModelFactory

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val signInViewModel by viewModels<SignInViewModel> { ViewModelFactory.getInstance(requireActivity()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btSignInBack.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_signInFragment_to_landingFragment)
        )

        binding.btSignIn.setOnClickListener{_ ->
            signInViewModel.login(
                binding.tfSignInEmailOrUsername.editText?.text.toString(),
                binding.tfSignInPassword.editText?.text.toString()
            )
                .observe(viewLifecycleOwner) {result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {

                        }
                        is Result.Success -> {
                            val mainActivityIntent = Intent(requireActivity(), MainActivity::class.java)
                            mainActivityIntent.putExtra(MainActivity.EXTRA_USER, result.data)
                            startActivity(mainActivityIntent)
                        }
                        is Result.Error -> {
                            if(result.error == "401") {
                                Toast.makeText(requireActivity(), "Wrong password", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireActivity(), "User not found", Toast.LENGTH_SHORT).show()
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
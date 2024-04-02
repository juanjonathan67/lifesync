package com.dicoding.lifesync.ui.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.dicoding.lifesync.R
import com.dicoding.lifesync.databinding.FragmentLandingBinding

class LandingFragment : Fragment() {
    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btSignIn.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_landingFragment_to_signInFragment)
        )
        binding.btSignUp.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_landingFragment_to_signUpFragment)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
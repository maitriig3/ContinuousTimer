package com.hkngtech.continuoustimer.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hkngtech.continuoustimer.databinding.FragmentSplashBinding
import com.hkngtech.continuoustimer.ui.base.BaseFragment
import com.hkngtech.continuoustimer.utils.navigate

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            navigate(SplashFragmentDirections.actionSplashFragmentToScheduleFragment())
        },2*1000)
    }

}
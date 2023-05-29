package com.hkngtech.continuoustimer.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hkngtech.continuoustimer.R

typealias inflaterType<VB> = (inflater : LayoutInflater)->VB

abstract class BaseFragment<VB : ViewBinding> (private val bindingInflater : inflaterType<VB>) : Fragment() {

    private var _binding : VB? = null
    val binding : VB
    get() = _binding as VB



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = bindingInflater.invoke(inflater)
        return binding.root
    }
}
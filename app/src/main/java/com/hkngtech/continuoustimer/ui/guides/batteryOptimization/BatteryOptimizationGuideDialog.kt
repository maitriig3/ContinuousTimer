package com.hkngtech.continuoustimer.ui.guides.batteryOptimization

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.hkngtech.continuoustimer.databinding.DialogBatteryOptimizationGuideBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BatteryOptimizationGuideDialog: DialogFragment() {

    private var guideInterface: GuideInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        guideInterface = parentFragment?.activity as GuideInterface
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogBatteryOptimizationGuideBinding.inflate(layoutInflater)

        binding.btnPositive.setOnClickListener {
            guideInterface?.batteryOptimizationGuide(true)
            dismiss()
        }

        binding.btnNegative.setOnClickListener {
            guideInterface?.batteryOptimizationGuide(false)
            dismiss()
        }


        return AlertDialog.Builder(requireContext()).setView(binding.root).create()
    }

    interface GuideInterface {

        fun batteryOptimizationGuide(allowed: Boolean)

    }



}
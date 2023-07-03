package com.hkngtech.continuoustimer.ui.guides.batteryOptimization

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.hkngtech.continuoustimer.databinding.DialogWhyBatteryOptimizationBinding
import com.hkngtech.continuoustimer.ui.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhyBatteryOptimizationDialog: DialogFragment() {

    private var guideInterface: GuideInterface? = null
    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        guideInterface = parentFragment?.requireContext() as GuideInterface
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogWhyBatteryOptimizationBinding.inflate(layoutInflater)

        binding.btnPositive.setOnClickListener {
            settingsViewModel.setBatteryOptimization(binding.checkBoxDontAskAgain.isChecked)
            guideInterface?.whyBatteryOptimization(true)
            dismiss()
        }

        binding.btnNegative.setOnClickListener {
            settingsViewModel.setBatteryOptimization(binding.checkBoxDontAskAgain.isChecked)
            guideInterface?.whyBatteryOptimization(false)
            dismiss()
        }


        return AlertDialog.Builder(requireContext()).setView(binding.root).create()
    }

    interface GuideInterface {

        fun whyBatteryOptimization(allowed: Boolean)

    }



}
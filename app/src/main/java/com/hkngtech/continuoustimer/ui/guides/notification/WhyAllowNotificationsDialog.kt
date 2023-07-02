package com.hkngtech.continuoustimer.ui.guides.notification

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.hkngtech.continuoustimer.databinding.DialogWhyAllowNotificationsBinding
import com.hkngtech.continuoustimer.ui.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhyAllowNotificationsDialog : DialogFragment() {

    private lateinit var guideInterface: GuideInterface
    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        guideInterface = parentFragment as GuideInterface
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogWhyAllowNotificationsBinding.inflate(layoutInflater)

        binding.btnPositive.setOnClickListener {
            settingsViewModel.setAllowNotifications(binding.checkBoxDontAskAgain.isChecked)
            guideInterface.whyAllowNotification(true)
            dismiss()
        }

        binding.btnNegative.setOnClickListener {
            settingsViewModel.setAllowNotifications(binding.checkBoxDontAskAgain.isChecked)
            guideInterface.whyAllowNotification(false)
            dismiss()
        }


        return AlertDialog.Builder(requireContext()).setView(binding.root).create()
    }

    interface GuideInterface {

        fun whyAllowNotification(allowed: Boolean)

    }

}
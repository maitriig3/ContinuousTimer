package com.hkngtech.continuoustimer.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkngtech.continuoustimer.databinding.FragmentHistoryBinding
import com.hkngtech.continuoustimer.databinding.FragmentHistoryDetailsBinding
import com.hkngtech.continuoustimer.ui.base.BaseFragment
import com.hkngtech.continuoustimer.ui.history.adapter.HistoryAdapter
import com.hkngtech.continuoustimer.ui.history.adapter.HistoryDetailsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HistoryDetailsFragment : BaseFragment<FragmentHistoryDetailsBinding>(FragmentHistoryDetailsBinding::inflate) {

    private val historyViewModel by viewModels<HistoryViewModel>()
    private val args: HistoryDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recViewHistoryDetails.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launchWhenCreated {
            historyViewModel.getHistoryDetails(args.historyId).collectLatest {
                binding.recViewHistoryDetails.adapter = HistoryDetailsAdapter(it) { which, historyDetailsTask ->


                }
            }
        }
    }


}
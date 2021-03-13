package com.mayandro.bitcointrend.ui.home.dashboard

import android.os.Bundle
import android.view.View
import com.mayandro.bitcointrend.databinding.FragmentDashboardBinding
import com.mayandro.bitcointrend.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(), DashboardInteractor {
    private val dashboardViewModel: DashboardViewModel by viewModel()

    override fun getViewBinding(): FragmentDashboardBinding = FragmentDashboardBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.viewInteractor = this
    }
}
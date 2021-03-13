package com.mayandro.bitcointrend.di

import com.mayandro.bitcointrend.ui.home.dashboard.DashboardViewModel
import com.mayandro.bitcointrend.ui.home.detail.DetailViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory { androidApplication().resources }

    viewModel { DashboardViewModel() }

    viewModel { DetailViewModel() }
}
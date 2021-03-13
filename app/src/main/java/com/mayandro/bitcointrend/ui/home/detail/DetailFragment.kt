package com.mayandro.bitcointrend.ui.home.detail

import android.os.Bundle
import android.view.View
import com.mayandro.bitcointrend.databinding.FragmentDetailBinding
import com.mayandro.bitcointrend.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment: BaseFragment<FragmentDetailBinding>(), DetailInteractor {
    private val detailViewModel: DetailViewModel by viewModel()

    override fun getViewBinding(): FragmentDetailBinding = FragmentDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel.viewInteractor = this
    }
}
package com.mayandro.bitcointrend.ui.home

import com.mayandro.bitcointrend.databinding.ActivityHomeBinding
import com.mayandro.bitcointrend.ui.base.BaseActivity

class HomeActivity: BaseActivity<ActivityHomeBinding>() {

    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)
}
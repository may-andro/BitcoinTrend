package com.mayandro.bitcointrend.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val uiModule = module {
    factory { androidApplication().resources }
}
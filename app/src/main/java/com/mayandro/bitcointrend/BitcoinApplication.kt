package com.mayandro.bitcointrend

import android.app.Application
import com.mayandro.bitcointrend.di.uiModule
import com.mayandro.datasource.di.dataSourceModule
import com.mayandro.domain.di.domainModule
import com.mayandro.remote.di.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BitcoinApplication: Application()  {

    override fun onCreate() {
        super.onCreate()
        // Start Koin

        startKoin{
            androidLogger()
            androidContext(this@BitcoinApplication)
            modules(
                listOf(
                    remoteModule,
                    dataSourceModule,
                    domainModule,
                    uiModule
                )
            )
        }
    }
}
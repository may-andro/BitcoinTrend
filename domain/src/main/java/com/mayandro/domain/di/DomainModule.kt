package com.mayandro.domain.di

import com.mayandro.datasource.factory.DataSourceFactory
import com.mayandro.domain.repository.ChartRepository
import com.mayandro.domain.repository.ChartRepositoryImpl
import com.mayandro.domain.usecase.GetChartDataUseCase
import org.koin.dsl.module

val domainModule = module() {
    single {
        provideChartRepository(dataSourceFactory = get())
    }

    factory {
        GetChartDataUseCase(chartRepository = get())
    }
}

private fun provideChartRepository(
    dataSourceFactory: DataSourceFactory
): ChartRepository {
    return ChartRepositoryImpl(dataSourceFactory = dataSourceFactory)
}
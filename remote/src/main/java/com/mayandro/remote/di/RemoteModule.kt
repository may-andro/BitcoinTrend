package com.mayandro.remote.di

import com.mayandro.remote.RemoteDataSource
import com.mayandro.remote.RemoteDataSourceImpl
import com.mayandro.remote.retrofit.RetrofitApi
import com.mayandro.remote.utils.BASE_URL
import com.mayandro.remote.utils.REQUEST_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module{
    single { provideHttpLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideServiceApi(get()) }
    single { provideRemoteDataSource(get()) }
}

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}

private fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .build()

    return (okHttpClient)
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideServiceApi(retrofit: Retrofit): RetrofitApi = retrofit.create(RetrofitApi::class.java)

private fun provideRemoteDataSource(
    retrofitApi: RetrofitApi
): RemoteDataSource {
    return RemoteDataSourceImpl(retrofitApi)
}
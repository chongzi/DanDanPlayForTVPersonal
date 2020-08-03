package com.seiko.common.di

import com.seiko.common.BuildConfig
import com.seiko.common.util.okhttp.TrafficStatsRequestInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofitBuilder(moshi: Moshi): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
    }

    @Provides
    fun provideCommonClientBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(TrafficStatsRequestInterceptor())
        }
        return builder
    }
}
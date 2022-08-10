package co.kr.woowahan_banchan.di

import co.kr.woowahan_banchan.BuildConfig
import co.kr.woowahan_banchan.data.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideBestService(
        retrofit: Retrofit
    ): BestService {
        return retrofit.create(BestService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainDishService(
        retrofit: Retrofit
    ): MainDishService {
        return retrofit.create(MainDishService::class.java)
    }

    @Provides
    @Singleton
    fun provideSideDishService(
        retrofit: Retrofit
    ): SideDishService {
        return retrofit.create(SideDishService::class.java)
    }

    @Provides
    @Singleton
    fun provideSoupDishService(
        retrofit: Retrofit
    ): SoupDishService {
        return retrofit.create(SoupDishService::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailService(
        retrofit: Retrofit
    ): DetailService {
        return retrofit.create(DetailService::class.java)
    }
}
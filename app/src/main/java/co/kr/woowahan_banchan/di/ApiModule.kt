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
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(httpLoggingInterceptor)
    }.build()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.apiUrl)
        .addConverterFactory(gsonConverterFactory)
        .client(httpClient)
        .build()

    @Provides
    @Singleton
    fun provideBestService(
        retrofit: Retrofit
    ): BestService = retrofit.create(BestService::class.java)

    @Provides
    @Singleton
    fun provideMainDishService(
        retrofit: Retrofit
    ): MainDishService = retrofit.create(MainDishService::class.java)

    @Provides
    @Singleton
    fun provideSideDishService(
        retrofit: Retrofit
    ): SideDishService = retrofit.create(SideDishService::class.java)

    @Provides
    @Singleton
    fun provideSoupDishService(
        retrofit: Retrofit
    ): SoupDishService = retrofit.create(SoupDishService::class.java)

    @Provides
    @Singleton
    fun provideDetailService(
        retrofit: Retrofit
    ): DetailService = retrofit.create(DetailService::class.java)
}
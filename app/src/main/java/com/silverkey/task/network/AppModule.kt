package com.silverkey.task.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.silverkey.task.BuildConfig
import com.silverkey.task.db.AppDatabase
import com.silverkey.task.network.api_helper.home.HomeApiHelper
import com.silverkey.task.network.api_helper_impl.home.HomeApiHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideRegisterApiHelper(apiHelper: HomeApiHelperImpl): HomeApiHelper = apiHelper

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "user_database",
        ).build()

    @Provides
    fun providerArticleDao(database: AppDatabase) = database.articleDao()
}
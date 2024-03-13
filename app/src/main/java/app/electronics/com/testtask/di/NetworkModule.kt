package app.electronics.com.testtask.di

import app.electronics.com.testtask.data.source.network.PexelsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

private const val BASE_URL = "https://api.pexels.com/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providePexelsApiService(): PexelsApiService {
        val interceptor = Interceptor { chain ->
            chain.request().newBuilder()
                .header(
                    "Authorization",
                    "CAYpbP7bITUOPYZ4JmWA2Im22UMLE4zNfMPcWORNKfTITwI13qzoVHPx"
                )
                .build().run {
                    chain.proceed(this)
                }
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}
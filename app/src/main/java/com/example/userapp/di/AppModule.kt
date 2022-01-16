package com.example.userapp.di

import android.content.Context
import com.example.userapp.BuildConfig
import com.example.userapp.BuildConfig.BASE_URL
import com.example.userapp.api.TokenInterceptor
import com.example.userapp.data.repo.Repository
import com.example.userapp.data.repo.RepositoryImpl
import com.example.userapp.data.source.local.LocalDataSource
import com.example.userapp.data.source.local.LocalDataSourceImpl
import com.example.userapp.data.source.local.UserDatabase
import com.example.userapp.data.source.local.dao.UserDao
import com.example.userapp.data.source.remote.RemoteDataSource
import com.example.userapp.data.source.remote.RemoteDataSourceImpl
import com.example.userapp.api.UserService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesUserDao(@ApplicationContext context: Context): UserDao {
        return UserDatabase.getDatabase(context).userDao()
    }

    @Provides
    @Singleton
    fun providesRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): Repository {
        return RepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun providesLocalDataSource(userDao: UserDao): LocalDataSource {
        return LocalDataSourceImpl(userDao)
    }

    @Provides
    @Singleton
    fun providesRemoteDataSource(userService: UserService): RemoteDataSource {
        return RemoteDataSourceImpl(userService)
    }

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofit(tokenInterceptor: TokenInterceptor): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }

        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
    }
}
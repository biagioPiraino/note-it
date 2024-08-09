package com.solutions.note_it.modules

import com.solutions.note_it.BuildConfig
import com.solutions.note_it.interfaces.CategoriesApi
import com.solutions.note_it.interfaces.NotesApi
import com.solutions.note_it.interfaces.TasksApi
import com.solutions.note_it.interfaces.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /*
    * These are the building blocks of the network module
    * 1) A or multiple HttpClient/s that actually perform the API calls
    * 2) As many as base urls Singleton as needed according to how many different API the app calls
    * 3) As many as Retrofit Singleton as needed according to how many different API the app calls
    * 4) As many as Services as needed according to the actual services includes in the above APIs
    *
    * In the example below if i need to add another service for another API I need to:
    * 1) Create the base url API Singleton
    * 2) Create a Retrofit using the above base url and the existing HttpClient
    * 3) Create services according to how many interfaces are implemented in the API
    * */

    @Provides
    @Singleton
    @Named("NoteItApiBaseUrl")
    fun provideNoteItApiBaseUrl(): String = BuildConfig.NOTE_IT_API_BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(authenticator: Authenticator): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(authenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("NoteItApiRetrofit")
    fun provideNoteItApiRetrofit(
        @Named("NoteItApiBaseUrl") baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNotesApiServices(@Named("NoteItApiRetrofit") retrofit: Retrofit): NotesApi {
        return retrofit.create(NotesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTasksApiServices(@Named("NoteItApiRetrofit") retrofit: Retrofit): TasksApi {
        return retrofit.create(TasksApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoriesApiServices(@Named("NoteItApiRetrofit") retrofit: Retrofit): CategoriesApi {
        return retrofit.create(CategoriesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUsersApiServices(@Named("NoteItApiRetrofit") retrofit: Retrofit): UsersApi {
        return retrofit.create(UsersApi::class.java)
    }
}

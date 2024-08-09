package com.solutions.note_it.modules

import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.solutions.note_it.BuildConfig
import com.solutions.note_it.services.AuthorizationServices
import com.solutions.note_it.utils.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthorizationModule {

    @Provides
    @Singleton
    fun provideAuthProvider(): Auth0 {
        return Auth0(
            BuildConfig.AUTH0_CLIENT_ID,
            BuildConfig.AUTH0_DOMAIN
        )
    }

    @Provides
    @Singleton
    fun provideClient(authProvider: Auth0): AuthenticationAPIClient {
        return AuthenticationAPIClient(authProvider)
    }

    @Provides
    @Singleton
    fun provideAuthorizationServices(authProvider: Auth0): AuthorizationServices {
        return AuthorizationServices(authProvider)
    }

    @Provides
    @Singleton
    fun provideAuthenticator(services: AuthorizationServices): Authenticator {
        return TokenAuthenticator(services)
    }
}
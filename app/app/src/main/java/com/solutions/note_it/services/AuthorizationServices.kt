package com.solutions.note_it.services

import android.content.Context
import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.solutions.note_it.BuildConfig
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthorizationServices @Inject constructor(private val authProvider: Auth0) {
    private var cachedCredentials: Credentials? = null
    private val loggingTag: String = "Auth provider:"

    suspend fun login(context: Context): Boolean = suspendCoroutine { continuation ->
        WebAuthProvider.login(authProvider).withScheme(BuildConfig.AUTH0_SCHEME)
            .withScope("openid profile email read:current_user update:current_user_metadata")
            .withAudience(BuildConfig.NOTE_IT_API_AUDIENCE)
            .start(context, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    Log.e(loggingTag, error.toString())
                    continuation.resume(false)
                }

                override fun onSuccess(result: Credentials) {
                    Log.i(loggingTag, "Login successful")
                    cachedCredentials = result
                    continuation.resume(true)
                }
            })
    }

    fun getAccessToken(): String {
        return cachedCredentials?.accessToken ?: ""
    }

    suspend fun getUserProfile(): UserProfile? = suspendCoroutine { continuation ->
        val client = AuthenticationAPIClient(authProvider)
        val accessToken = getAccessToken()

        client.userInfo(accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    Log.e(loggingTag, error.toString())
                    continuation.resume(null)
                }

                override fun onSuccess(result: UserProfile) {
                    Log.i(loggingTag, "Returning user profile...")
                    continuation.resume(result)
                }
            })
    }

    suspend fun logout(context: Context): Boolean = suspendCoroutine { continuation ->
        WebAuthProvider.logout(authProvider).withScheme(BuildConfig.AUTH0_SCHEME)
            .start(context, object: Callback<Void?, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    Log.e(loggingTag, error.toString())
                    continuation.resume(false)
                }

                override fun onSuccess(result: Void?) {
                    Log.i(loggingTag, "Logout successful")
                    cachedCredentials = null
                    continuation.resume(true)
                }
            })
    }
}

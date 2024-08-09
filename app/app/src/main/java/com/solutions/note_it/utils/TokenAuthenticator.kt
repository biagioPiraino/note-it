package com.solutions.note_it.utils

import com.solutions.note_it.services.AuthorizationServices
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val services: AuthorizationServices
) : Authenticator {
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request {
        val accessToken = services.getAccessToken()
        return response.request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}
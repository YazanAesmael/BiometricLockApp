package com.android.biometric

enum class AuthenticationError(val errorCode : Int) {
    CANCELLED(13),
    AUTHENTICATION_DIALOG_DISMISSED(10),
    TOO_MANY_ATTEMPT(7)
}
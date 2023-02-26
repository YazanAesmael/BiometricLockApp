package com.android.biometric

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.android.biometric.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val log = "I'm Dead"
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var biometricPrompt : BiometricPrompt
    private lateinit var biometricManager: BiometricManager

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBiometricAuthentication()
        checkBiometricFeatureState()

        binding.tvMainActivity.setOnClickListener {
            if (isBiometricFeatureAvailable()) {
                biometricPrompt.authenticate(buildBiometricPrompt())
            }
        }
    }

    private fun setupBiometricAuthentication() {
        Log.d(log, "setupBiometricAuthentication()")
        biometricManager = BiometricManager.from(this)
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, biometricCallback)
    }
    private fun checkBiometricFeatureState() {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> finish()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Log.d(log, "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Log.d(log, "You have not registered any biometric credentials")
            BiometricManager.BIOMETRIC_SUCCESS -> { Log.d(log, "BIOMETRIC_SUCCESS") }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.d(log, "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED")
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.d(log, "BIOMETRIC_ERROR_UNSUPPORTED")
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Log.d(log, "BIOMETRIC_STATUS_UNKNOWN")
            }
        }
    }
    private fun buildBiometricPrompt(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verify your identity")
            .setNegativeButtonText("Cancel")
            .setConfirmationRequired(false) //Allows user to authenticate without performing an action, such as pressing a button, after their biometric credential is accepted.
            .build()
    }
    private fun isBiometricFeatureAvailable(): Boolean {
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }
    private val biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            Toast.makeText(baseContext, "welcome!", Toast.LENGTH_SHORT).show()
            super.onAuthenticationSucceeded(result)
            navigateTo<SecureActivity>()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)

            if (errorCode != AuthenticationError.AUTHENTICATION_DIALOG_DISMISSED.errorCode
                && errorCode != AuthenticationError.CANCELLED.errorCode) {
                Toast.makeText(baseContext, "error!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
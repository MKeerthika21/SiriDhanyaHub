package com.siridhanya.hub.security

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.siridhanya.hub.R
import com.siridhanya.hub.databinding.ActivityAuthBinding
import com.siridhanya.hub.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var securityManager: SecurityManager

    private lateinit var binding: ActivityAuthBinding
    private var currentPin = ""
    private var isSetupMode = false
    private var firstPinAttempt = ""
    private var lockoutTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isSetupMode = !securityManager.isPinSet()
        updateUi()
        setupKeypad()

        if (!isSetupMode && !securityManager.isLockedOut()) {
            showBiometricPrompt()
        }

        checkLockout()
    }

    private fun updateUi() {
        if (isSetupMode) {
            binding.tvTitle.text = if (firstPinAttempt.isEmpty()) {
                getString(R.string.auth_setup_pin)
            } else {
                getString(R.string.auth_confirm_pin)
            }
            binding.tvSubtitle.text = getString(R.string.auth_create_pin_subtitle)
            binding.btnBiometric.visibility = View.INVISIBLE
        } else {
            binding.tvTitle.text = getString(R.string.auth_welcome_back)
            binding.tvSubtitle.text = getString(R.string.auth_enter_pin)
            binding.btnBiometric.visibility = View.VISIBLE
        }
        updatePinIndicators()
    }

    private fun setupKeypad() {
        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9,
        )

        buttons.forEach { btn ->
            btn.setOnClickListener {
                if (securityManager.isLockedOut()) return@setOnClickListener
                if (currentPin.length < 4) {
                    currentPin += btn.text
                    updatePinIndicators()
                    if (currentPin.length == 4) {
                        handlePinCompletion()
                    }
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            if (currentPin.isNotEmpty()) {
                currentPin = currentPin.dropLast(1)
                updatePinIndicators()
            }
        }

        binding.btnBiometric.setOnClickListener {
            showBiometricPrompt()
        }
    }

    private fun updatePinIndicators() {
        val dots = listOf(binding.dot1, binding.dot2, binding.dot3, binding.dot4)
        dots.forEachIndexed { index, view ->
            if (index < currentPin.length) {
                view.setBackgroundResource(R.drawable.pin_dot_on)
            } else {
                view.setBackgroundResource(R.drawable.pin_dot_off)
            }
        }
    }

    private fun handlePinCompletion() {
        if (isSetupMode) {
            if (firstPinAttempt.isEmpty()) {
                firstPinAttempt = currentPin
                currentPin = ""
                updateUi()
            } else {
                if (currentPin == firstPinAttempt) {
                    securityManager.savePin(currentPin)
                    startMainActivity()
                } else {
                    Toast.makeText(this, getString(R.string.auth_pin_mismatch), Toast.LENGTH_SHORT).show()
                    currentPin = ""
                    firstPinAttempt = ""
                    updateUi()
                }
            }
        } else {
            if (securityManager.verifyPin(currentPin)) {
                startMainActivity()
            } else {
                currentPin = ""
                updatePinIndicators()
                if (securityManager.isLockedOut()) {
                    checkLockout()
                } else {
                    Toast.makeText(this, getString(R.string.auth_incorrect_pin), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkLockout() {
        if (securityManager.isLockedOut()) {
            val remaining = securityManager.getRemainingLockoutTime()
            startLockoutTimer(remaining)
        }
    }

    private fun startLockoutTimer(millis: Long) {
        lockoutTimer?.cancel()
        lockoutTimer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvSubtitle.text = getString(
                    R.string.auth_lockout_message,
                    millisUntilFinished / 1000
                )
                binding.keypad.alpha = 0.5f
            }

            override fun onFinish() {
                binding.keypad.alpha = 1.0f
                updateUi()
            }
        }.start()
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startMainActivity()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.auth_biometric_title))
            .setSubtitle(getString(R.string.auth_biometric_subtitle))
            .setNegativeButtonText(getString(R.string.auth_biometric_negative))
            .build()

        try {
            biometricPrompt.authenticate(promptInfo)
        } catch (e: Exception) {
            // Biometrics not available
            binding.btnBiometric.visibility = View.GONE
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        lockoutTimer?.cancel()
    }
}

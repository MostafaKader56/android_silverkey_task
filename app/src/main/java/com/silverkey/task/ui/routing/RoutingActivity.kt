package com.silverkey.task.ui.routing

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.silverkey.task.ui.home.HomeActivity
import com.silverkey.task.ui.onboarding.OnboardingActivity
import com.silverkey.task.utils.SharedPreferencesModule

class RoutingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition { true }

        Handler(Looper.getMainLooper()).postDelayed({
            openNextActivity()
        }, 2000)

//        lifecycleScope.launch {
//            delay(2000)
//            openNextActivity()
//        }
    }

    private fun openNextActivity() {
        startActivity(
            Intent(
                this@RoutingActivity,
                if (SharedPreferencesModule.getBooleanValue(SharedPreferencesModule.IS_ONBOARDING_SHOWN, false))
                    HomeActivity::class.java
                else
                    OnboardingActivity::class.java
            )
        )
        finish()
    }
}
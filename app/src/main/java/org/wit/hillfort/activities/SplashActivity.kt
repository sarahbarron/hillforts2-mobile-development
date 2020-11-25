package org.wit.hillfort.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp


// Splash screen activity for initial loading of the app
class SplashActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app:MainApp
    //    splash timer
    private val SPLASH_TIME_OUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        info("Splash Screen Activated")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Once the timer has finished start the Authentication Activity
        Handler().postDelayed(
            {
                startActivity(Intent(this, AuthenticationActivity::class.java))
                // Animate the loading of new activity
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }, SPLASH_TIME_OUT)
    }
}
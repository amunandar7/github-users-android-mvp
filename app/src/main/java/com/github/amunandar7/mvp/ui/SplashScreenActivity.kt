package com.github.amunandar7.mvp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.github.amunandar7.mvp.R
import com.github.amunandar7.mvp.ui.userlist.UserListActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed(
            Runnable {
                startActivity(Intent(this, UserListActivity::class.java))
                finish()
            }
            , INTERVAL_SPLASH)
    }

    companion object {
        const val INTERVAL_SPLASH: Long = 1000 * 3
    }
}

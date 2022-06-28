package com.mle.weatherappnew.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mle.weatherappnew.R
import com.mle.weatherappnew.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherMainFragment.newInstance())
                .commitNow()
        }
    }
}
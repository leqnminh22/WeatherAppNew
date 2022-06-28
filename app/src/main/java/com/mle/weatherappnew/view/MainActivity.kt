package com.mle.weatherappnew.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_main -> {
                    showCity()
                    true
                }
                R.id.action_list -> {
                    showCitiesList()
                    true
                }
                else -> false
            }
        }
    }

    private fun showCity() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, WeatherMainFragment.newInstance())
            .commit()
    }

    private fun showCitiesList() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, WeatherListFragment.newInstance())
            .commit()
    }
}
package com.example.weather.ui

import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        setupUi(binding)
        setContentView(binding.root)
    }

    private fun setupUi(binding: ActivityMainBinding) {
        binding.apply {
            lifecycleScope.launch {
                viewModel.uiState.flowWithLifecycle(lifecycle)
                    .collect {
                        dateTv.text = it.date
                        temperatureTv.text = getString(R.string.temperature, it.temp)
                        weatherTv.text = it.weather

                        if (it.temp != "") {
                            temperatureTv.visibility = VISIBLE
                        }

                        rootLayout.setBackgroundResource(it.bgImg)
                    }
            }

            searchView.setOnQueryTextListener(
                QueryTextListener(searchView) {
                    viewModel.getAllWeather(it)
                }
            )
        }
    }
}

private class QueryTextListener(
    private val searchView: SearchView,
    private val onSearchClicked: (String) -> Unit
) :
    OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        searchView.clearFocus()
        Log.d(TAG, query!!)
        onSearchClicked(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}

package com.example.rutubetesttask.presentation.forecast

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.rutubetesttask.R
import com.example.rutubetesttask.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.round

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    private val viewModel by viewModels<ForecastViewModel>()
    private lateinit var binding: FragmentForecastBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentForecastBinding.bind(view)

        val arguments = ForecastFragmentArgs.fromBundle(requireArguments())
        viewModel.init(arguments.lat, arguments.long)
        binding.cityNameTextView.text = arguments.nameCity

        setupListeners()
        setupObserve()
    }

    private fun setupListeners() {
        binding.refreshButton.setOnClickListener {
            viewModel.refreshForecast()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forecastStateFlow.collect {
                    if (it.isError) {
                        showError()
                    } else if (it.isLoading) {
                        showLoading()
                    } else {
                        showSuccess()
                        binding.temperatureTextView.text = "${round(it.temperature)}°C"
                    }
                }
            }
        }
    }

    private fun showSuccess() {
        binding.successGroup.visibility = View.VISIBLE
        binding.containerView.showSuccess()
    }

    private fun showLoading() {
        binding.successGroup.visibility = View.GONE
        binding.containerView.showPending()
    }

    private fun showError() {
        binding.successGroup.visibility = View.GONE
        binding.containerView.showError {
            viewModel.refreshForecast()
        }
    }
}
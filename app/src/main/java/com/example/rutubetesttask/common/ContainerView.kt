package com.example.rutubetesttask.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.rutubetesttask.databinding.PartLoadingBinding

class ContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {


    private var binding: PartLoadingBinding
    private var tryAgainAction: (() -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        binding = PartLoadingBinding.inflate(inflater, this, false)
        addView(binding.root)

        binding.againButton.setOnClickListener {
            tryAgainAction?.invoke()
        }
    }


    fun showPending() {
        hideAll()
        binding.progressBar.visibility = VISIBLE
    }

    fun showError(tryAgain: (() -> Unit)? = null) {
        hideAll()
        binding.errorContainer.visibility = VISIBLE
        tryAgainAction = tryAgain
        binding.againButton.visibility = if (tryAgain == null) GONE else VISIBLE

    }

    fun showSuccess() {
        hideAll()
    }

    private fun hideAll() {
        binding.errorContainer.visibility = GONE
        binding.progressBar.visibility = GONE
    }
}
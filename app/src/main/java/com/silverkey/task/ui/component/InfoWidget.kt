package com.silverkey.task.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.silverkey.task.databinding.ComponentInfoWidgetBinding

class InfoWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ComponentInfoWidgetBinding =
        ComponentInfoWidgetBinding.inflate(LayoutInflater.from(context), this)

    fun setMessage(message: String) {
        binding.errorMessage.text = message
    }

    fun setImage(imageRes: Int) {
        binding.errorImage.setImageResource(imageRes)
    }

    fun setButtonVisibility(isVisible: Boolean) {
        binding.btnRetry.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setOnRetryClickListener(listener: () -> Unit) {
        binding.btnRetry.setOnClickListener {
            listener.invoke()
        }
    }

    fun show() {
        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.GONE
    }
}

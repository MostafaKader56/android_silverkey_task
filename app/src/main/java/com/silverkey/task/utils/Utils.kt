package com.silverkey.task.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.silverkey.task.SilverKeyTaskApplication
import com.silverkey.task.ui.home.HomeActivity

object Utils {
    fun isSystemDarkModeEnabled(context: Context): Boolean {
        val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return uiMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun toggleDarkMode() {
        val currentMode = SharedPreferencesModule.getBooleanValue(
            SharedPreferencesModule.IS_DARK_MODE_ENABLED,
            isSystemDarkModeEnabled(SilverKeyTaskApplication.instance)
        )
        AppCompatDelegate.setDefaultNightMode(
            if (currentMode.not()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        SharedPreferencesModule.setBooleanValue(
            SharedPreferencesModule.IS_DARK_MODE_ENABLED, currentMode.not()
        )
    }

    fun Any?.logCat(title: String = "LogCat") {
        Log.wtf(title, if (this is String) this else this.toString())
    }

    fun Any?.toast() {
        Toast.makeText(SilverKeyTaskApplication.instance, this.toString(), Toast.LENGTH_LONG).show()
    }

    fun Activity.getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            private var isScrolling = false

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (this@getScrollListener !is HomeActivity) return
                val bottomNav = this@getScrollListener.binding.bottomNav
                if (!isScrolling) {
                    isScrolling = true
                    if (dy > 0 && bottomNav.translationY == 0f) {
                        bottomNav.animate().translationY(bottomNav.height.toFloat())
                            .setDuration(150).start()
                    } else if (dy < 0 && bottomNav.translationY == bottomNav.height.toFloat()) {
                        bottomNav.animate().translationY(0f).setDuration(150).start()
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScrolling = false
                }
            }
        }
    }

    fun Activity.getNestedScrollListener(): NestedScrollView.OnScrollChangeListener {
        return NestedScrollView.OnScrollChangeListener { _: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (this !is HomeActivity) return@OnScrollChangeListener

            val bottomNav = binding.bottomNav
            if (scrollY > oldScrollY && bottomNav.translationY == 0f) {
                bottomNav.animate().translationY(bottomNav.height.toFloat()).setDuration(150)
                    .start()
            } else if (scrollY < oldScrollY && bottomNav.translationY == bottomNav.height.toFloat()) {
                bottomNav.animate().translationY(0f).setDuration(150).start()
            }
        }
    }


    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
        val wrapper = object : Observer<T> {
            override fun onChanged(value: T) {
                observer(value)
                removeObserver(this)
            }
        }
        observe(owner, wrapper)
    }

    fun Context.share(shareText: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    @Suppress("DEPRECATION")
    inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // New signature added in API 33+
            getParcelable(key, T::class.java)
        } else {
            // Old deprecated signature
            getParcelable(key) as? T
        }
    }
}
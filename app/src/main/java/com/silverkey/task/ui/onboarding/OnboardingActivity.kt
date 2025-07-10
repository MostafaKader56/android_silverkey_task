package com.silverkey.task.ui.onboarding

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.silverkey.task.R
import com.silverkey.task.base.BaseActivity
import com.silverkey.task.databinding.ActivityOnboardingBinding
import com.silverkey.task.model.onboarding.OnboardingItem
import com.silverkey.task.ui.home.HomeActivity
import com.silverkey.task.utils.SharedPreferencesModule
import com.silverkey.task.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>() {
    override val bindingFactory: (LayoutInflater) -> ActivityOnboardingBinding
        get() = ActivityOnboardingBinding::inflate

    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun initialization() {
        setupViewPager()
        updateUI(0)
    }

    override fun setListeners() {
        setupClickListeners()
    }

    private fun setupViewPager() {
        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.onboarding_img_1,
                getString(R.string.onboarind_title_1),
                getString(R.string.onboarind_desc_1)
            ),
            OnboardingItem(
                R.drawable.onboarding_img_2,
                getString(R.string.onboarind_title_2),
                getString(R.string.onboarind_desc_2)
            ),
            OnboardingItem(
                R.drawable.onboarding_img_3,
                getString(R.string.onboarind_title_3),
                getString(R.string.onboarind_desc_3)
            ),
            OnboardingItem(
                R.drawable.onboarding_img_4,
                getString(R.string.onboarind_title_4),
                getString(R.string.onboarind_desc_4)
            )
        )

        onboardingAdapter = OnboardingAdapter(onboardingItems)
        binding.viewPager.adapter = onboardingAdapter
        binding.dotsIndicator.attachTo(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateUI(position)
            }
        })
    }

    private fun setupClickListeners() {
        binding.buttonNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < onboardingAdapter.itemCount - 1) {
                binding.viewPager.currentItem = current + 1
            } else {
                finishOnboarding()
            }
        }

        binding.buttonSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun updateUI(position: Int) {
        val lastIndex = onboardingAdapter.itemCount - 1
        val isLast = position == lastIndex

        binding.buttonNext.visibility = View.VISIBLE
        binding.buttonNext.text =
            if (isLast) getString(R.string.finish)
            else getString(R.string.next)

        binding.buttonSkip.visibility = if (isLast) View.GONE else View.VISIBLE
    }


    private fun finishOnboarding() {
        // Save onboarding completion status
        SharedPreferencesModule.setBooleanValue(SharedPreferencesModule.IS_ONBOARDING_SHOWN, true)

        // Navigate to main activity
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
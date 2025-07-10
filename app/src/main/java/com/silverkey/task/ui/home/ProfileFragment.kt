package com.silverkey.task.ui.home

import androidx.fragment.app.activityViewModels
import com.silverkey.task.base.BaseFragment
import com.silverkey.task.base.Inflate
import com.silverkey.task.databinding.FragmentProfileBinding
import com.silverkey.task.utils.Utils.getNestedScrollListener
import com.silverkey.task.viewmodels.home.FavoriteViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, FavoriteViewModel>() {
    override val inflate: Inflate<FragmentProfileBinding>
        get() = FragmentProfileBinding::inflate

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    override fun getInjectViewModel() = favoriteViewModel

    override fun initialization() {
        handleScrollingState()
    }

    override fun listeners() {

    }

    override fun initializeViewModel() {

    }

    private fun handleScrollingState() {
        binding.scrollView.setOnScrollChangeListener(
            requireActivity().getNestedScrollListener()
        )
    }
}
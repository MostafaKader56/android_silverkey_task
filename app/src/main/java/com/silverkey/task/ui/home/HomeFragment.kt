package com.silverkey.task.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.silverkey.task.R
import com.silverkey.task.base.BaseFragment
import com.silverkey.task.base.BaseUiResource
import com.silverkey.task.base.Inflate
import com.silverkey.task.databinding.FragmentHomeBinding
import com.silverkey.task.model.home.Article
import com.silverkey.task.utils.UIErrorHandler
import com.silverkey.task.utils.Utils
import com.silverkey.task.utils.Utils.getScrollListener
import com.silverkey.task.utils.Utils.logCat
import com.silverkey.task.viewmodels.home.FavoriteViewModel
import com.silverkey.task.viewmodels.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val inflate: Inflate<FragmentHomeBinding>
        get() = FragmentHomeBinding::inflate

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    private val homeViewModel: HomeViewModel by viewModels()
    override fun getInjectViewModel() = homeViewModel

    private val articlesAdapter = ArticlesAdapter()
    private val skeletonAdapter = SkeletonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.getArticles()
    }

    override fun initialization() {
        setupArticlesRecyclerView()
        setupSkeletonArticlesRecyclerView()
        setupScrollingHideBottomNavBar()
    }

    override fun listeners() {
        handleDarkModeMenuLogic()
        handleArticlesListener()
        handleSwipeToRefresh()
        handlerOnErrorRetryClicked()
    }

    override fun initializeViewModel() {
        getFavoriteArticlesUrls()
        handleArticlesResponse()
        handleOnArticleAddedToOrRemovedFromFavorite()
    }

    private fun handleOnArticleAddedToOrRemovedFromFavorite() {
        favoriteViewModel.onArticleFavoriteStatusChangedLiveData.observe(viewLifecycleOwner) {
            for ((url, value) in it) {
                articlesAdapter.updateFavoriteStatusByUrl(url, value)
            }
        }
    }

    private fun getFavoriteArticlesUrls() {
        favoriteViewModel.getFavoriteArticlesUrlsMap()?.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty().not()) {
                articlesAdapter.updateFavoriteStatus(it.associateWith { true })
            }
        }
    }

    private fun handlerOnErrorRetryClicked() {
        binding.errorWidget.setOnRetryClickListener {
            homeViewModel.getArticles()
        }
    }

    private fun setupSkeletonArticlesRecyclerView() {
        binding.apply {
            rvSkeleton.layoutManager = LinearLayoutManager(requireContext())
            rvSkeleton.adapter = skeletonAdapter
        }
    }

    private fun handleSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.getArticles()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun handleArticlesResponse() {
        homeViewModel.getArticlesLiveData.observe(viewLifecycleOwner) {
            when (it) {
                BaseUiResource.LoadingState -> {
                    binding.apply {
                        shimmerContainer.visibility = View.VISIBLE
                        swipeRefreshLayout.visibility = View.GONE
                        errorWidget.hide()

                        shimmerContainer.startShimmer()
                    }
                }

                is BaseUiResource.FailureState -> {
                    binding.apply {
                        shimmerContainer.visibility = View.GONE
                        swipeRefreshLayout.visibility = View.GONE
                        errorWidget.show()

                        shimmerContainer.stopShimmer()
                        errorWidget.setMessage(
                            if (it.errorType != null) {
                                UIErrorHandler.getErrorMessageLocalized(
                                    requireContext(),
                                    it.errorType
                                )
                            } else it.message ?: getString(R.string.something_went_wrong)
                        )
                    }
                }

                is BaseUiResource.SuccessState -> {
                    binding.apply {
                        shimmerContainer.visibility = View.GONE
                        swipeRefreshLayout.visibility = View.VISIBLE
                        errorWidget.hide()

                        shimmerContainer.stopShimmer()
                        articlesAdapter.updateArticles(it.data ?: listOf())
                    }
                }
            }
        }
    }

    private fun setupScrollingHideBottomNavBar() {
        val listener = requireActivity().getScrollListener()
        binding.recycler.addOnScrollListener(
            listener
        )
        binding.rvSkeleton.addOnScrollListener(
            listener
        )
    }

    private fun setupArticlesRecyclerView() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articlesAdapter
        }
    }


    private fun handleDarkModeMenuLogic() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.dark_mode -> {
                    Utils.toggleDarkMode()
                    true
                }

                else -> false
            }
        }
    }

    private fun handleArticlesListener() {
        articlesAdapter.setOnClickListener(
            object : ArticlesAdapter.ClickListener {
                override fun onItemClicked(
                    article: Article,
                    position: Int,
                    favoriteState: Boolean
                ) {
                    requireView().findNavController().navigate(
                        R.id.article_details_fragment, bundleOf(
                            "article" to article,
                            "is_favorite" to favoriteState
                        ), NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setEnterAnim(R.anim.slide_in_down)
                            .setExitAnim(R.anim.slide_out_down)
                            .setPopEnterAnim(R.anim.slide_in_up)
                            .setPopExitAnim(R.anim.slide_out_up)
                            .build()
                    )
                }

                override fun onFavoriteClicked(
                    article: Article,
                    position: Int,
                    oldFavoriteState: Boolean
                ) {
                    if (oldFavoriteState) {
                        favoriteViewModel.removeFavoriteArticle(article)
                    } else {
                        favoriteViewModel.addFavoriteArticle(article)
                    }
                }
            }
        )
    }
}
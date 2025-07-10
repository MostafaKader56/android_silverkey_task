package com.silverkey.task.ui.home

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.silverkey.task.R
import com.silverkey.task.base.BaseFragment
import com.silverkey.task.base.Inflate
import com.silverkey.task.databinding.FragmentFavoriteBinding
import com.silverkey.task.model.home.Article
import com.silverkey.task.utils.Utils.getScrollListener
import com.silverkey.task.utils.Utils.observeOnce
import com.silverkey.task.viewmodels.home.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {
    override val inflate: Inflate<FragmentFavoriteBinding>
        get() = FragmentFavoriteBinding::inflate

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    override fun getInjectViewModel() = favoriteViewModel

    private val articlesAdapter = ArticlesAdapter()

    override fun initialization() {
        setupRecyclerView()
    }

    override fun listeners() {
        setupOnFavoriteClicked()
        setupEmptyStateListener()
    }

    override fun initializeViewModel() {
        handleArticlesResponse()
        setupScrollingHideBottomNavBar()
        handleOnArticleAddedToOrRemovedFromFavorite()
    }

    private fun setupEmptyStateListener() {
        binding.emptyStateWidget.apply {
            setImage(R.drawable.ic_empty)
            setMessage(getString(R.string.no_favorites))
            setButtonVisibility(false)
        }

        articlesAdapter.setEmptyStateListener(object : ArticlesAdapter.EmptyStateListener {
            override fun onEmptyState(isEmpty: Boolean) {
                binding.emptyStateWidget.isVisible = isEmpty
                binding.recycler.isVisible = isEmpty.not()
            }
        })
    }

    private fun setupOnFavoriteClicked() {
        articlesAdapter.setOnClickListener(object : ArticlesAdapter.ClickListener {
            override fun onItemClicked(
                article: Article, position: Int, favoriteState: Boolean
            ) {
                requireView().findNavController().navigate(
                    R.id.article_details_fragment,
                    bundleOf(
                        "article" to article, "is_favorite" to favoriteState
                    ),
                    NavOptions.Builder().setLaunchSingleTop(true).setEnterAnim(R.anim.slide_in_down)
                        .setExitAnim(R.anim.slide_out_down).setPopEnterAnim(R.anim.slide_in_up)
                        .setPopExitAnim(R.anim.slide_out_up).build()
                )
            }

            override fun onFavoriteClicked(
                article: Article, position: Int, oldFavoriteState: Boolean
            ) {
                if (oldFavoriteState) {
                    favoriteViewModel.removeFavoriteArticle(article)
                } else {
                    favoriteViewModel.addFavoriteArticle(article)
                }
            }
        })
    }

    private fun handleOnArticleAddedToOrRemovedFromFavorite() {
        favoriteViewModel.onArticleFavoriteStatusChangedLiveData.observe(viewLifecycleOwner) {
            for ((url, value) in it) {
                articlesAdapter.updateFavoriteStatusByUrl(url, value)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articlesAdapter
        }
    }

    private fun setupScrollingHideBottomNavBar() {
        binding.recycler.addOnScrollListener(
            requireActivity().getScrollListener()
        )
    }

    private fun handleArticlesResponse() {
        favoriteViewModel.getAllFavoriteArticles()?.observeOnce(viewLifecycleOwner) { articles ->
            articlesAdapter.updateAllData(articles,
                articles.associate { article -> article.url to true })
        }
    }
}
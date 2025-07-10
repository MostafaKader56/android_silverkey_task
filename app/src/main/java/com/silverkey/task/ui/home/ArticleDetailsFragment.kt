package com.silverkey.task.ui.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.silverkey.task.R
import com.silverkey.task.base.BaseFragment
import com.silverkey.task.base.Inflate
import com.silverkey.task.databinding.FragmentArticleDetailsBinding
import com.silverkey.task.model.home.Article
import com.silverkey.task.utils.RelativeTimeHelper
import com.silverkey.task.utils.Utils.getNestedScrollListener
import com.silverkey.task.utils.Utils.getParcelableCompat
import com.silverkey.task.utils.Utils.share
import com.silverkey.task.viewmodels.home.FavoriteViewModel

class ArticleDetailsFragment : BaseFragment<FragmentArticleDetailsBinding, FavoriteViewModel>() {
    override val inflate: Inflate<FragmentArticleDetailsBinding>
        get() = FragmentArticleDetailsBinding::inflate

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    private var isFavorite: Boolean = false
    private lateinit var article: Article

    override fun getInjectViewModel() = favoriteViewModel

    private fun getPassedData() {
        val safeArgs = requireArguments()
        isFavorite = safeArgs.getBoolean("is_favorite")
        val passedArticle = safeArgs.getParcelableCompat<Article>("article")
        if (passedArticle == null) {
            requireView().findNavController().popBackStack()
        } else {
            article = passedArticle
        }
    }

    override fun initialization() {
        getPassedData()
        viewToolbarBackIcon()
        bindDataToViews()
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

    private fun bindDataToViews() {
        binding.apply {
            tvArticleTitle.text = article.title
            tvTimeAgo.text =
                RelativeTimeHelper.getRelativeTime(requireContext(), article.publishedAt)
            tvArticleDescription.text = article.description
            tvAuthor.text = article.author
            Glide.with(requireContext())
                .load(article.urlToImage)
                .into(ivArticleImage)
        }
    }

    private fun viewToolbarBackIcon() {
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            requireView().findNavController().popBackStack()
        }
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.article_details_app_bar_menu, menu)

                try {
                    if (menu.javaClass.simpleName == "MenuBuilder") {
                        val m = menu.javaClass.getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean::class.javaPrimitiveType
                        )
                        m.isAccessible = true
                        m.invoke(menu, true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val favoriteItem = menu.findItem(R.id.change_favorite_state)
                if (isFavorite) {
                    favoriteItem.setIcon(R.drawable.ic_favorite_active)
                    favoriteItem.title = getString(R.string.remove_from_favorite)
                } else {
                    favoriteItem.setIcon(R.drawable.ic_favorite)
                    favoriteItem.title = getString(R.string.add_to_favorite)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.share -> {
                        requireContext().share(article.url)
                        true
                    }

                    R.id.change_favorite_state -> {
                        if (isFavorite) {
                            favoriteViewModel.removeFavoriteArticle(article)
                        } else {
                            favoriteViewModel.addFavoriteArticle(article)
                        }
                        isFavorite = !isFavorite
                        requireActivity().invalidateMenu()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
}
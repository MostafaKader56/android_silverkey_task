package com.silverkey.task.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.silverkey.task.R
import com.silverkey.task.databinding.ItemArticleBinding
import com.silverkey.task.model.home.Article
import com.silverkey.task.utils.RelativeTimeHelper

class ArticlesAdapter() : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    private var listener: ClickListener? = null
    private var emptyStateListener: EmptyStateListener? = null
    private val favorites: MutableMap<String, Boolean> = mutableMapOf()
    private var recyclerView: RecyclerView? = null
    private var articles: List<Article> = listOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun setOnClickListener(listener: ClickListener) {
        this.listener = listener
    }

    fun setEmptyStateListener(listener: EmptyStateListener) {
        this.emptyStateListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position], favorites[articles[position].url] ?: false)
    }

    override fun getItemCount(): Int = articles.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
        emptyStateListener?.onEmptyState(articles.isEmpty())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAllData(newArticles: List<Article>, favoriteArticlesUrlsMap: Map<String, Boolean>) {
        articles = newArticles
        favorites.clear()
        favorites.putAll(favoriteArticlesUrlsMap)
        notifyDataSetChanged()
        emptyStateListener?.onEmptyState(articles.isEmpty())
    }

    fun updateFavoriteStatusByPosition(position: Int, isFavorite: Boolean) {
        if (position != -1) {
            favorites[articles[position].url] = isFavorite
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
            if (viewHolder != null && viewHolder is ArticleViewHolder) {
                viewHolder.changeFavoriteState(
                    isFavorite
                )
            } else {
                notifyItemChanged(position)
            }
        }
    }

    fun updateFavoriteState(position: Int, isFavorite: Boolean) {
        if (favorites[articles[position].url] == isFavorite) return
        favorites[articles[position].url] = isFavorite
        val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
        if (viewHolder != null && viewHolder is ArticleViewHolder) {
            viewHolder.changeFavoriteState(
                isFavorite
            )
        } else {
            notifyItemChanged(position)
        }
    }

    fun updateFavoriteStatusByUrl(url: String, isFavorite: Boolean) {
        if (favorites[url] == isFavorite) return
        val position = articles.indexOfFirst { article ->
            article.url == url
        }
        if (position != -1) {
            favorites[articles[position].url] = isFavorite
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
            if (viewHolder != null && viewHolder is ArticleViewHolder) {
                viewHolder.changeFavoriteState(
                    isFavorite
                )
            } else {
                notifyItemChanged(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavoriteStatus(favoriteArticlesUrlsMap: Map<String, Boolean>) {
        favorites.clear()
        favorites.putAll(favoriteArticlesUrlsMap)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        favorites[articles[position].url] = false
        articles = articles.filterIndexed { index, _ -> index != position }
        notifyItemRemoved(position)
        emptyStateListener?.onEmptyState(articles.isEmpty())
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener?.onItemClicked(
                    articles[bindingAdapterPosition], bindingAdapterPosition,
                    favorites[articles[bindingAdapterPosition].url] ?: false
                )
            }
            binding.btnFavorite.setOnClickListener {
                listener?.onFavoriteClicked(
                    articles[bindingAdapterPosition],
                    bindingAdapterPosition,
                    favorites[articles[bindingAdapterPosition].url] ?: false
                )
            }
        }

        fun bind(
            article: Article,
            isFavorite: Boolean,
        ) {
            binding.apply {
                tvArticleTitle.text = article.title
                tvArticleDescription.text = article.description
                tvTimeAgo.text =
                    RelativeTimeHelper.getRelativeTime(binding.root.context, article.publishedAt)
                changeFavoriteState(isFavorite)
                Glide.with(binding.root.context).load(article.urlToImage).into(ivArticleImage)
            }
        }

        fun changeFavoriteState(isFavorite: Boolean) {
            binding.btnFavorite.apply {
                if (isFavorite) {
                    setIconResource(R.drawable.ic_favorite_active)
                } else {
                    setIconResource(R.drawable.ic_favorite)
                }
            }
        }
    }

    interface ClickListener {
        fun onItemClicked(article: Article, position: Int, favoriteState: Boolean)
        fun onFavoriteClicked(article: Article, position: Int, oldFavoriteState: Boolean)
    }

    interface EmptyStateListener {
        fun onEmptyState(isEmpty: Boolean)
    }
}
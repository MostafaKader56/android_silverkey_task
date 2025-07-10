package com.silverkey.task.viewmodels.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.silverkey.task.base.BaseViewModel
import com.silverkey.task.model.home.Article
import com.silverkey.task.network.ArticlesNetworkResource
import com.silverkey.task.repos.home.FavoriteArticlesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteArticlesRepository: FavoriteArticlesRepository,
) : BaseViewModel() {

    private val onArticleFavoriteStatusChangedResponse =
        MutableLiveData<MutableMap<String, Boolean>>()
    val onArticleFavoriteStatusChangedLiveData: LiveData<MutableMap<String, Boolean>>
        get() = onArticleFavoriteStatusChangedResponse

    fun removeFavoriteArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteArticlesRepository.deleteArticle(article.url)
            val map = onArticleFavoriteStatusChangedResponse.value ?: mutableMapOf()
            map[article.url] = false
            onArticleFavoriteStatusChangedResponse.postValue(map)
        }
    }

    fun addFavoriteArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteArticlesRepository.insertArticle(article)
            val map = onArticleFavoriteStatusChangedResponse.value ?: mutableMapOf()
            map[article.url] = true
            onArticleFavoriteStatusChangedResponse.postValue(map)
        }
    }

    fun getFavoriteArticlesUrlsMap(): LiveData<List<String>>? {
        return favoriteArticlesRepository.getAllArticlesUrls()
    }

    fun getAllFavoriteArticles() = favoriteArticlesRepository.getAllArticles()

    override fun <T> onSuccessfulResponse(id: Int, resource: ArticlesNetworkResource<T>?) {

    }

    override fun onFailedResponse(id: Int, resource: ArticlesNetworkResource<String>?) {

    }
}
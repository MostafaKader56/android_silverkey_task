package com.silverkey.task.viewmodels.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.silverkey.task.R
import com.silverkey.task.base.BaseUiResource
import com.silverkey.task.base.BaseViewModel
import com.silverkey.task.model.home.Article
import com.silverkey.task.network.ArticlesNetworkResource
import com.silverkey.task.repos.home.HomeRepository
import com.silverkey.task.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel() {

    private val getArticlesResponse = MutableLiveData<BaseUiResource<List<Article>>>()
    val getArticlesLiveData: LiveData<BaseUiResource<List<Article>>>
        get() = getArticlesResponse

    fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            getArticlesResponse.postValue(BaseUiResource.LoadingState)
            handleResponse(
                R.id.get_articles,
                homeRepository.getArticles(Constants.SOURCE),
            )
        }
    }

    override fun <T> onSuccessfulResponse(id: Int, resource: ArticlesNetworkResource<T>?) {
        when (id) {
            R.id.get_articles -> {
                getArticlesResponse.value = BaseUiResource.SuccessState(
                    data = (resource?.articles as? List<Article>?)
                )
            }
        }
    }

    override fun onFailedResponse(
        id: Int, resource: ArticlesNetworkResource<String>?
    ) {
        when (id) {
            R.id.get_articles -> {
                getArticlesResponse.value = BaseUiResource.FailureState(
                    message = resource?.articles,
                    errorType = resource?.error,
                )
            }
        }
    }
}
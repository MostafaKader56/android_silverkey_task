package com.silverkey.task.network.api_helper.home

import com.silverkey.task.model.home.Article
import com.silverkey.task.network.ArticlesNetworkResource
import retrofit2.Response

interface HomeApiHelper {
    suspend fun getArticles(source: String): Response<ArticlesNetworkResource<List<Article>>>?
}
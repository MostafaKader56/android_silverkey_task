package com.silverkey.task.network

import com.silverkey.task.model.home.Article
import com.silverkey.task.utils.Constants.GET_ARTICLES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(GET_ARTICLES)
    suspend fun getArticles(
        @Query("source") source: String,
        @Query("apiKey") apiKey: String
    ): Response<ArticlesNetworkResource<List<Article>>>?
}
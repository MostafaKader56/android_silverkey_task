package com.silverkey.task.network.api_helper_impl.home

import com.silverkey.task.network.ApiService
import com.silverkey.task.network.api_helper.home.HomeApiHelper
import com.silverkey.task.utils.Constants
import javax.inject.Inject

class HomeApiHelperImpl @Inject constructor(private val apiService: ApiService) :
    HomeApiHelper {
    override suspend fun getArticles(source: String) =
        apiService.getArticles(
            source = source,
            apiKey = Constants.API_KEY
        )
}
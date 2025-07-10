package com.silverkey.task.repos.home

import com.silverkey.task.network.api_helper.home.HomeApiHelper
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeApiHelper: HomeApiHelper
) {
    suspend fun getArticles(source: String) = try {
        homeApiHelper.getArticles(source)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

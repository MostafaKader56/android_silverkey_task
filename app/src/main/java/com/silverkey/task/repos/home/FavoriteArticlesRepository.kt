package com.silverkey.task.repos.home

import com.silverkey.task.db.FavoriteArticleDao
import com.silverkey.task.model.home.Article
import javax.inject.Inject


class FavoriteArticlesRepository @Inject constructor(private val favoriteArticleDao: FavoriteArticleDao) {

    fun getAllArticles() = try {
        favoriteArticleDao.getAllFavoriteArticles()
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

    fun getAllArticlesUrls() = try {
        favoriteArticleDao.getAllFavoriteUrls()
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

    suspend fun insertArticle(article: Article) = try {
        favoriteArticleDao.insert(article)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }

    suspend fun deleteArticle(url: String) = try {
        favoriteArticleDao.deleteByUrl(url)
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}
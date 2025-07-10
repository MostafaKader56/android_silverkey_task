package com.silverkey.task.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.silverkey.task.model.home.Article

@Dao
interface FavoriteArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Query("DELETE FROM articles_table WHERE url = :url")
    suspend fun deleteByUrl(url: String): Int

    @Query("SELECT * FROM articles_table")
    fun getAllFavoriteArticles(): LiveData<List<Article>>

    @Query("SELECT url FROM articles_table")
    fun getAllFavoriteUrls(): LiveData<List<String>>
}
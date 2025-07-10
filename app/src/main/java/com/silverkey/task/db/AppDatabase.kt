package com.silverkey.task.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.silverkey.task.model.home.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): FavoriteArticleDao
}
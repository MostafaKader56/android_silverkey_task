package com.silverkey.task.model.home

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "articles_table")
@Parcelize
data class Article(
    @PrimaryKey
    @ColumnInfo(name = "url")
    val url: String,

    @SerializedName("author")
    @ColumnInfo(name = "author")
    val author: String?,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,

    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String?,

    @SerializedName("urlToImage")
    @ColumnInfo(name = "image_url")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    @ColumnInfo(name = "published_at")
    val publishedAt: String
) : Parcelable

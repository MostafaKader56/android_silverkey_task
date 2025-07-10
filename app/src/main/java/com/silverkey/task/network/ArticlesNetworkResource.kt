package com.silverkey.task.network

import com.silverkey.task.base.BaseErrorType

open class ArticlesNetworkResource<out T>(
    open val status: String? = null,
    open val articles: T? = null,
    open val code: String? = null,
    open val source: String? = null,
    open val message: String? = null,
    open val error: BaseErrorType? = null,
)
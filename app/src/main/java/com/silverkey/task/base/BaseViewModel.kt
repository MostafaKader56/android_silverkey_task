package com.silverkey.task.base

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.silverkey.task.SilverKeyTaskApplication
import com.silverkey.task.network.ArticlesNetworkResource
import com.silverkey.task.utils.NetworkUtils
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

@ViewModelScoped
abstract class BaseViewModel() : ViewModel() {
    suspend fun executeRequest(
        id: Int,
        request: suspend () -> Unit,
    ) {
        if (NetworkUtils.isConnected(SilverKeyTaskApplication.instance)) {
            request.invoke()
        } else {
            withContext(Dispatchers.Main) {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.NoInternetConnection,
                    ),
                )
            }
        }
    }

    suspend fun <T : Any> handleResponse(
        id: Int,
        response: Response<ArticlesNetworkResource<T>>?,
    ) {
        withContext(Dispatchers.Main) {
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    when (responseBody?.status) {
                        "ok" -> {
                            onSuccessfulResponse(
                                id = id,
                                resource = responseBody,
                            )
                        }

                        "error" -> {
                            handleErrorMethod(responseBody.code, id)
                        }
                    }
                } else if (!response.isSuccessful && response.code() == 500 || response.code() == 502) {
                    onFailedResponse(
                        id = id,
                        resource = ArticlesNetworkResource(
                            error = BaseErrorType.ServerError,
                        ),
                    )
                } else if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    val responseBody: ArticlesNetworkResource<String>? =
                        if (errorBody != null) {
                            Gson().fromJson(
                                errorBody, object :
                                    TypeToken<ArticlesNetworkResource<Any?>>() {}.type
                            )
                        } else {
                            null
                        }

                    when (response.code()) {
                        500 -> {
                            onFailedResponse(
                                id = id,
                                resource = ArticlesNetworkResource(
                                    error = BaseErrorType.ServerError,
                                ),
                            )
                        }

                        else -> {
                            handleErrorMethod(responseBody?.code, id)
                        }
                    }

                } else {
                    onFailedResponse(
                        id = id,
                        resource = ArticlesNetworkResource(
                            error = BaseErrorType.ServerError,
                        ),
                    )
                }
            } else {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.ServerError,
                    ),
                )
            }
        }
    }

    private fun handleErrorMethod(code: String?, id: Int) {
        when (code) {
            "noInternetConnection" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.NoInternetConnection,
                    ),
                )
            }

            "apiKeyDisabled" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.ApiKeyDisabled,
                    ),
                )
            }

            "apiKeyExhausted" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.ApiKeyExhausted,
                    ),
                )
            }

            "apiKeyInvalid" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.ApiKeyInvalid,
                    ),
                )
            }

            "apiKeyMissing" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.ApiKeyMissing,
                    ),
                )
            }

            "apiUsage" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.ApiUsage,
                    ),
                )
            }

            "parameterInvalid" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.ParameterInvalid,
                    ),
                )
            }

            "parametersMissing" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.ParametersMissing,
                    ),
                )
            }

            "rateLimited" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.RateLimited,
                    ),
                )
                return
            }

            "sourcesTooMany" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.SourcesTooMany,
                    ),
                )
            }

            "sourceDoesNotExist" -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.SourceDoesNotExist,
                    ),
                )
            }

            else -> {
                onFailedResponse(
                    id = id,
                    resource = ArticlesNetworkResource(
                        error = BaseErrorType.UnexpectedError,
                    ),
                )
            }
        }
    }

    abstract fun <T> onSuccessfulResponse(
        id: Int, resource: ArticlesNetworkResource<T>?,
    )

    abstract fun onFailedResponse(
        id: Int,
        resource: ArticlesNetworkResource<String>?,
    )
}
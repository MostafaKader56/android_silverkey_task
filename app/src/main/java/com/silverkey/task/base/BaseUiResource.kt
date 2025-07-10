package com.silverkey.task.base

sealed class BaseUiResource<out T> {
    data object LoadingState : BaseUiResource<Nothing>()

    data class FailureState(
        val message: String? = null,
        val errorType: BaseErrorType? = null,
        val errors: Map<String, List<String>>? = null,
    ) : BaseUiResource<Nothing>()

    data class SuccessState<T>(
        val message: String? = null,
        val data: T? = null,
    ) : BaseUiResource<T>()
}
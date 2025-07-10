package com.silverkey.task.utils

import android.content.Context
import com.silverkey.task.R
import com.silverkey.task.base.BaseErrorType

object UIErrorHandler {
    fun getErrorMessageLocalized(context: Context, type: BaseErrorType): String {
        return when (type) {
            BaseErrorType.NoInternetConnection -> context.getString(R.string.error_no_internet)
            BaseErrorType.ApiKeyDisabled -> context.getString(R.string.error_api_key_disabled)
            BaseErrorType.ApiKeyExhausted -> context.getString(R.string.error_api_key_exhausted)
            BaseErrorType.ApiKeyInvalid -> context.getString(R.string.error_api_key_invalid)
            BaseErrorType.ApiKeyMissing -> context.getString(R.string.error_api_key_missing)
            BaseErrorType.ApiUsage -> context.getString(R.string.error_api_usage)
            BaseErrorType.ParameterInvalid -> context.getString(R.string.error_parameter_invalid)
            BaseErrorType.ParametersMissing -> context.getString(R.string.error_parameters_missing)
            BaseErrorType.RateLimited -> context.getString(R.string.error_rate_limited)
            BaseErrorType.SourcesTooMany -> context.getString(R.string.error_sources_too_many)
            BaseErrorType.SourceDoesNotExist -> context.getString(R.string.error_source_not_exist)
            BaseErrorType.UnexpectedError -> context.getString(R.string.error_unexpected)
            BaseErrorType.ServerError -> context.getString(R.string.error_server)
        }
    }
}

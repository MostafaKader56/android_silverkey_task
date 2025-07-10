package com.silverkey.task.base

enum class BaseErrorType {
    NoInternetConnection,
    ApiKeyDisabled,
    ApiKeyExhausted,
    ApiKeyInvalid,
    ApiKeyMissing,
    ApiUsage,
    ParameterInvalid,
    ParametersMissing,
    RateLimited,
    SourcesTooMany,
    SourceDoesNotExist,
    UnexpectedError,
    ServerError,
}
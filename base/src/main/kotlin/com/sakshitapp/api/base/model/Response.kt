package com.sakshitapp.api.base.model

data class Response<T>(
    val code: Int = 200,
    val data: T?,
    val error: String? = null
)
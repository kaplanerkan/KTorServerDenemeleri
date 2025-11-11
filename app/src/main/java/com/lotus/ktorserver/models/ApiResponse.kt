package com.lotus.ktorserver.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val urun: Urun? = null,
    val error: String? = null
)

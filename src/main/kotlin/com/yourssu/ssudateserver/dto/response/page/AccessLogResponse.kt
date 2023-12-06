package com.yourssu.ssudateserver.dto.response.page

import java.time.LocalDateTime

class AccessLogResponse(
    val id: Long,
    val ip: String?,
    val os: String?,
    val requestURL: String,
    val method: String,
    val requestBody: String?,
    val responseBody: String?,
    val createdAt: LocalDateTime,
)

package com.yourssu.ssudateserver.dto.response

data class RefreshTokenResponseDto(
    val accessToken: String,
    val refreshToken: String,
)

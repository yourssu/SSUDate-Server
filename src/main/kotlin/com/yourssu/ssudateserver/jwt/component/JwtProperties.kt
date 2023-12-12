package com.yourssu.ssudateserver.jwt.component

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secretKey: String,
    val accessExpTime: Int,
    val refreshExpTime: Int,
)

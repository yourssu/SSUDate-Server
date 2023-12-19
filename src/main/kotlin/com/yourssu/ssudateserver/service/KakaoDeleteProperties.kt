package com.yourssu.ssudateserver.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "kakao")
class KakaoDeleteProperties {
    lateinit var removeUrl: String
    lateinit var adminKey: String
}

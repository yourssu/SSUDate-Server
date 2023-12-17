package com.yourssu.ssudateserver.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "front")
class FrontProperties {
    lateinit var url: String
}

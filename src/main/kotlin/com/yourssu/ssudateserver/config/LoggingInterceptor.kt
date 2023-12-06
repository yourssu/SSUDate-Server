package com.yourssu.ssudateserver.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.ssudateserver.entity.AccessLog
import com.yourssu.ssudateserver.repository.AccessLogRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoggingInterceptor(
    private val objectMapper: ObjectMapper,
    private val accessLogRepository: AccessLogRepository,
) : HandlerInterceptor {
    @Throws(Exception::class)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        @Nullable ex: java.lang.Exception?,
    ) {
        val clientIP = getClientIp(request)
        val clientOS = getClientOS(request)
        val cachingRequest: ContentCachingRequestWrapper = request as ContentCachingRequestWrapper
        val cachingResponse: ContentCachingResponseWrapper = response as ContentCachingResponseWrapper
        val excludedURIs = listOf(
            "/logs",
            "/v3/api-docs",
            "/swagger-resources",
            "/swagger-ui",
            "/webjars",
            "/swagger",
            "/favicon",
        )
        if (!excludedURIs.any { request.requestURI.startsWith(it) }) {
            accessLogRepository.save(
                AccessLog(
                    ip = clientIP,
                    os = clientOS,
                    requestURL = request.requestURL.toString(),
                    method = request.method,
                    requestBody = objectMapper.readTree(cachingRequest.contentAsByteArray).toString(),
                    responseBody = objectMapper.readTree(cachingResponse.contentAsByteArray).toString(),
                    createdAt = LocalDateTime.now(),
                ),
            )
        }
    }

    private fun getClientIp(request: HttpServletRequest): String {
        val xForwardedForHeader = request.getHeader("X-Forwarded-For")
        return if (xForwardedForHeader != null && xForwardedForHeader.isNotEmpty()) {
            xForwardedForHeader.split(",").first().trim()
        } else {
            request.remoteAddr
        }
    }

    private fun getClientOS(request: HttpServletRequest): String {
        return try {
            val osName = request.getHeader("User-Agent")
            osName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }
}

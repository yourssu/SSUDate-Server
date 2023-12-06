package com.yourssu.ssudateserver.controller

import com.yourssu.ssudateserver.dto.response.page.AccessLogPageResponse
import com.yourssu.ssudateserver.service.AccessLogService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class AccessLogController(private val accessLogService: AccessLogService) {
    @GetMapping("/logs")
    fun getAccessLog(
        @RequestParam(required = true) password: String,
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) size: Int?,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @RequestParam(required = false)
        start: LocalDateTime?,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @RequestParam(required = false)
        end: LocalDateTime?,
    ): AccessLogPageResponse {
        return AccessLogPageResponse(
            accessLogService.getAccessLog(
                password,
                page ?: 0,
                size ?: 15,
                start ?: LocalDateTime.of(
                    1970,
                    1,
                    1,
                    0,
                    0,
                    0,
                ),
                (end ?: LocalDateTime.now().withSecond(59)),
            ),
        )
    }
}

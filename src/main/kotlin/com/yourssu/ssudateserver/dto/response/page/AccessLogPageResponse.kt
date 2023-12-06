package com.yourssu.ssudateserver.dto.response.page

import com.yourssu.ssudateserver.entity.AccessLog
import org.springframework.data.domain.Page

class AccessLogPageResponse(page: Page<AccessLog>) {
    val logs: List<AccessLogResponse> =
        page.content.map {
            AccessLogResponse(
                it.id!!,
                it.ip,
                it.os,
                it.requestURL,
                it.method,
                it.requestBody,
                it.responseBody,
                it.createdAt,
            )
        }
    val page: PageInfo<AccessLog> = PageInfo(page)
}

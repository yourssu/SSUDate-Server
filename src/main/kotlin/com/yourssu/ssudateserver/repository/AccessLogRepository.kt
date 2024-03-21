package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.entity.AccessLog
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface AccessLogRepository : JpaRepository<AccessLog, Long> {
    fun findAllByCreatedAtIsBetween(
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable,
    ): Page<AccessLog>
}

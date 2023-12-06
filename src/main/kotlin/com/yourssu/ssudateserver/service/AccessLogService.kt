package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.entity.AccessLog
import com.yourssu.ssudateserver.exception.log.InvalidDateRangeException
import com.yourssu.ssudateserver.exception.log.InvalidPasswordException
import com.yourssu.ssudateserver.repository.AccessLogRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AccessLogService(private val accessLogRepository: AccessLogRepository) {
    fun getAccessLog(
        password: String,
        page: Int,
        size: Int,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Page<AccessLog> {
        if (password != "front-end") {
            throw InvalidPasswordException("비밀번호 에러입니다.")
        }

        val pageable = PageRequest.of(page, size, Sort.by("id").descending())
        if (end.isBefore(start)) {
            throw InvalidDateRangeException("종료시간은 시작시간보다 빠를 수 없습니다.")
        }
        return accessLogRepository.findAllByCreatedAtIsBetween(start, end, pageable)
    }
}

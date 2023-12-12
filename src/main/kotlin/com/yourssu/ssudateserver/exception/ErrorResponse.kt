package com.yourssu.ssudateserver.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ErrorResponse(
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd HH:mm:ss",
        locale = "Asia/Seoul",
    )
    val time: LocalDateTime,
    val status: HttpStatus,
    val message: String,
    val requestURI: String
)

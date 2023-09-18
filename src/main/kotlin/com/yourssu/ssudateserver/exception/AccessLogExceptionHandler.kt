package com.yourssu.ssudateserver.exception

import com.yourssu.ssudateserver.exception.log.InvalidDateRangeException
import com.yourssu.ssudateserver.exception.log.InvalidPasswordException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class AccessLogExceptionHandler {
    @ExceptionHandler(InvalidDateRangeException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidDateRangeException(exception: InvalidDateRangeException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(InvalidPasswordException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidPasswordException(exception: InvalidPasswordException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }
}

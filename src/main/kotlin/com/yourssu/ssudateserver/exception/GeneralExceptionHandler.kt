package com.yourssu.ssudateserver.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class GeneralExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.bindingResult.fieldError!!.defaultMessage.toString(), request.requestURI)
    }

//    @ExceptionHandler(HttpMessageNotReadableException::class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException, request: HttpServletRequest): ErrorResponse {
//        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.cause?.message!!, request.requestURI)
//    }
}

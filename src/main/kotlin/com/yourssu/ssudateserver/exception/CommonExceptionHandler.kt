package com.yourssu.ssudateserver.exception

import com.yourssu.ssudateserver.exception.logic.AllCanNotRegisterException
import com.yourssu.ssudateserver.exception.logic.CodeNotFoundException
import com.yourssu.ssudateserver.exception.logic.UnderZeroTicketException
import com.yourssu.ssudateserver.exception.logic.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class CommonExceptionHandler {
    @ExceptionHandler(CodeNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleCodeNotFoundException(exception: CodeNotFoundException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(UnderZeroTicketException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnderZeroTicketException(exception: UnderZeroTicketException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFoundException(exception: UserNotFoundException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(AllCanNotRegisterException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAllCanNotRegisterException(exception: AllCanNotRegisterException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.bindingResult.fieldError!!.defaultMessage.toString(), request.requestURI)
    }
}

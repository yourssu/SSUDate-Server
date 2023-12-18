package com.yourssu.ssudateserver.exception

import com.yourssu.ssudateserver.exception.logic.AllCanNotRegisterException
import com.yourssu.ssudateserver.exception.logic.DuplicateCodeException
import com.yourssu.ssudateserver.exception.logic.InvalidRefreshTokenException
import com.yourssu.ssudateserver.exception.logic.NickNameDuplicateException
import com.yourssu.ssudateserver.exception.logic.RefreshTokenNotFoundException
import com.yourssu.ssudateserver.exception.logic.SelfContactException
import com.yourssu.ssudateserver.exception.logic.UnderZeroTicketException
import com.yourssu.ssudateserver.exception.logic.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class CommonExceptionHandler {
    @ExceptionHandler(UnderZeroTicketException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnderZeroTicketException(
        exception: UnderZeroTicketException,
        request: HttpServletRequest,
    ): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFoundException(exception: UserNotFoundException, request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(AllCanNotRegisterException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAllCanNotRegisterException(
        exception: AllCanNotRegisterException,
        request: HttpServletRequest,
    ): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(NickNameDuplicateException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNickNameDuplicateException(
        exception: NickNameDuplicateException,
        request: HttpServletRequest,
    ): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(RefreshTokenNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleRefreshTokenNotFoundException(
        exception: RefreshTokenNotFoundException,
        request: HttpServletRequest,
    ): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(InvalidRefreshTokenException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidRefreshTokenException(
        exception: InvalidRefreshTokenException,
        request: HttpServletRequest,
    ): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(SelfContactException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleSelfContactException(
        exception: SelfContactException,
        request: HttpServletRequest,
    ): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }

    @ExceptionHandler(DuplicateCodeException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleDuplicateCodeException(
        exception: DuplicateCodeException,
        request: HttpServletRequest,
    ): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.message!!, request.requestURI)
    }
}

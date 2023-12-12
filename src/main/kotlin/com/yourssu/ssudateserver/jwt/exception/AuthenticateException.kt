package com.yourssu.ssudateserver.jwt.exception

import org.springframework.security.core.AuthenticationException

class AuthenticateException(message: String) : AuthenticationException(message)

package com.yourssu.ssudateserver.fixture

import com.yourssu.ssudateserver.dto.security.UserPrincipal
import com.yourssu.ssudateserver.enums.RoleType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class PrincipalFixture {
    companion object {
        fun setPrincipal(oauthName: String = "oauthName1") {
            val userPrincipal = UserPrincipal.of(oauthName, RoleType.USER)
            val authentication = TestingAuthenticationToken(userPrincipal, null, "ROLE_USER")
            SecurityContextHolder.getContext().authentication = authentication
        }
    }
}

package com.yourssu.ssudateserver.dto.security

import com.yourssu.ssudateserver.entity.User
import com.yourssu.ssudateserver.enums.RoleType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

data class UserPrincipal(
    private val id: Long,
    private val oauthName: String,
    private val authorities: Collection<GrantedAuthority>,
    private val oAuth2Attribute: Map<String, Any>,
) : OAuth2User {

    companion object {
        private fun of(id: Long, oauthName: String, role: RoleType, oAuth2Attribute: Map<String, Any>): UserPrincipal {
            val authorities = setOf(SimpleGrantedAuthority(role.value))
            return UserPrincipal(id, oauthName, authorities, oAuth2Attribute)
        }

        fun of(oauthName: String, role: RoleType): UserPrincipal {
            return of(0, oauthName, role, emptyMap())
        }

        fun from(user: User): UserPrincipal = of(user.id!!, user.oauthName, user.role, emptyMap())
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getAttributes(): Map<String, Any> = oAuth2Attribute
    override fun getName(): String = oauthName
}

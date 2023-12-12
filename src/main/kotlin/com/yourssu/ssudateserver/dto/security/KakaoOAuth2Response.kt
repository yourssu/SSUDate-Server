package com.yourssu.ssudateserver.dto.security

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class KakaoOAuth2Response(
    val id: Long,
    val connectedAt: LocalDateTime,
    val properties: Map<String, Any>,
    val kakaoAccount: KakaoAccount,
) {
    data class KakaoAccount(
        val profileNicknameNeedsAgreement: Boolean,
        val profile: Profile,
        val hasEmail: Boolean,
        val emailNeedsAgreement: Boolean,
        val isEmailValid: Boolean,
        val isEmailVerified: Boolean,
        val email: String,
    ) {
        data class Profile(val nickname: String) {
            companion object {
                fun from(attributes: Map<String, Any>): Profile {
                    return Profile(attributes["nickname"].toString())
                }
            }
        }

        companion object {
            fun from(attributes: Map<String, Any>): KakaoAccount {
                return KakaoAccount(
                    profileNicknameNeedsAgreement = attributes["profile_nickname_needs_agreement"].toString()
                        .toBoolean(),
                    profile = Profile.from(attributes.get("profile") as Map<String, Any>),
                    hasEmail = attributes["has_email"].toString().toBoolean(),
                    emailNeedsAgreement = attributes["email_needs_agreement"].toString().toBoolean(),
                    isEmailValid = attributes["is_email_valid"].toString().toBoolean(),
                    isEmailVerified = attributes["is_email_verified"].toString().toBoolean(),
                    email = attributes["email"].toString(),
                )
            }
        }

        fun nickname(): String = profile.nickname
    }

    companion object {
        fun from(attributes: Map<String, Any>): KakaoOAuth2Response {
            return KakaoOAuth2Response(
                id = attributes["id"].toString().toLong(),
                connectedAt = LocalDateTime.parse(
                    attributes["connected_at"].toString(),
                    DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()),
                ),
                properties = attributes["properties"] as Map<String, Any>,
                kakaoAccount = KakaoAccount.from(attributes["kakao_account"] as Map<String, Any>),
            )
        }
    }

    fun email(): String = kakaoAccount.email
    fun nickname(): String = kakaoAccount.nickname()
}

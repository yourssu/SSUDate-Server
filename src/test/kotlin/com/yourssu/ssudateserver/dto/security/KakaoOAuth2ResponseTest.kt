package com.yourssu.ssudateserver.dto.security

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class KakaoOAuth2ResponseTest {

    private val mapper = ObjectMapper()

    @Test
    fun testMapToKakaoAuthResponse() {
        // given
        val serializedResponse = """
            {
                "id": 1234567890,
                "connected_at": "2022-01-02T00:12:34Z",
                "properties": {
                    "nickname": "홍길동"
                },
                "kakao_account": {
                    "profile_nickname_needs_agreement": false,
                    "profile": {
                        "nickname": "홍길동"
                    },
                    "has_email": true,
                    "email_needs_agreement": false,
                    "is_email_valid": true,
                    "is_email_verified": true,
                    "email": "test@gmail.com"
                }
            }
        """.trimIndent()

        val attributes: Map<String, Any> =
            mapper.readValue(serializedResponse, object : TypeReference<Map<String, Any>>() {})

        // when
        val result = KakaoOAuth2Response.from(attributes)

        // then
        assertThat(result).isEqualTo(
            KakaoOAuth2Response(
                id = 1234567890L,
                connectedAt = ZonedDateTime.of(2022, 1, 2, 0, 12, 34, 0, ZoneOffset.UTC)
                    .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                properties = mapOf("nickname" to "홍길동"),
                kakaoAccount = KakaoOAuth2Response.KakaoAccount(
                    profileNicknameNeedsAgreement = false,
                    profile = KakaoOAuth2Response.KakaoAccount.Profile(nickname = "홍길동"),
                    hasEmail = true,
                    emailNeedsAgreement = false,
                    isEmailValid = true,
                    isEmailVerified = true,
                    email = "test@gmail.com",
                ),
            ),
        )
    }
}

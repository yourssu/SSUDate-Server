package com.yourssu.ssudateserver.dto.response

import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import java.time.LocalDateTime

data class UserInfoResponseDto(
    val id: Long,
    var animals: Animals,
    var mbti: MBTI,
    var nickName: String,
    var introduce: String,
    var contact: String,
    var weight: Int,
    var ticket: Int,
    val gender: Gender,
    var code: String,
    val createdAt: LocalDateTime,
)

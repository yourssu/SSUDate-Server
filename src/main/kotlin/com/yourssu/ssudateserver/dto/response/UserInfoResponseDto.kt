package com.yourssu.ssudateserver.dto.response

import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import java.time.LocalDateTime

data class UserInfoResponseDto(
    val id: Long,
    val animals: Animals,
    val mbti: MBTI,
    val nickName: String,
    val introduce: String,
    val contact: String,
    val weight: Int,
    val ticket: Int,
    val gender: Gender,
    val code: String,
    val codeInputChance: Int,
    val createdAt: LocalDateTime,
)

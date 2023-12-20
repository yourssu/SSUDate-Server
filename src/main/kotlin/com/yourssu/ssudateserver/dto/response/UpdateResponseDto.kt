package com.yourssu.ssudateserver.dto.response

import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI

data class UpdateResponseDto(
    val id: Long,
    val animals: Animals,
    val nickName: String,
    val mbti: MBTI,
    val introduce: String,
    val ticket: Int,
    val contact: String,
    val gender: Gender,
    val code: String,
    val codeInputChance: Int,
)

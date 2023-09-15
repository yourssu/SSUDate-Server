package com.yourssu.ssudateserver.dto.response

import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI

class RegisterResponseDto(
    val id: Long,
    val code: String,
    val animals: Animals,
    val nickName: String,
    val mbti: MBTI,
    val introduce: String,
    val contact: String,
    val gender: Gender
)

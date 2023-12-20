package com.yourssu.ssudateserver.dto.response

import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI

data class SearchContactResponseDto(
    val animals: Animals,
    val nickName: String,
    val mbti: MBTI,
    val introduce: String,
    val gender: Gender,
    val contact: String,
    val weight: Int,
)

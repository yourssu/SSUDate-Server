package com.yourssu.ssudateserver.dto.response.page

import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import java.time.LocalDateTime

class SearchResponseDto(
    val animals: Animals,
    val nickName: String,
    val mbti: MBTI,
    val introduce: String,
    val gender: Gender,
    val createdAt: LocalDateTime,
    val weight: Int
)

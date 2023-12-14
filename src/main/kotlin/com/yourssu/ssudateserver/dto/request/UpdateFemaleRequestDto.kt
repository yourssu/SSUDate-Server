package com.yourssu.ssudateserver.dto.request

import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.MBTI
import javax.validation.constraints.Size

data class UpdateFemaleRequestDto(
    val animals: FemaleAnimals,
    @field:Size(message = "닉네임은 최대 9글자 입니다.", min = 1, max = 9)
    val nickName: String,
    val mbti: MBTI,
    @field:Size(message = "소개글은 최대 100글자 입니다.", min = 1, max = 100)
    val introduce: String,
    val contact: String,
)

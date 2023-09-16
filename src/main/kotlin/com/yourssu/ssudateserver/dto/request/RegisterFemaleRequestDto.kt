package com.yourssu.ssudateserver.dto.request

import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.MBTI
import javax.validation.constraints.Size

class RegisterFemaleRequestDto(
    @field:Size(message = "인증코드는 10자리 입니다.", min = 10, max = 10)
    val code: String,
    val animals: FemaleAnimals,
    @field:Size(message = "닉네임은 최대 10글자 입니다.", min = 1, max = 10)
    val nickName: String,
    val mbti: MBTI,
    @field:Size(message = "소개글은 최대 100글자 입니다.", min = 1, max = 100)
    val introduce: String,
    val contact: String,
)

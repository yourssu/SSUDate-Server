package com.yourssu.ssudateserver.dto.request

import com.yourssu.ssudateserver.enums.FemaleAnimals
import com.yourssu.ssudateserver.enums.MBTI
import javax.validation.constraints.Size

class RegisterFemaleRequestDto(
    @field:Size(message = "인증코드는 10자리 입니다.", min = 10, max = 10)
    val code: String,
    val animals: FemaleAnimals,
    val nickName: String,
    val mbti: MBTI,
    val introduce: String,
    val contact: String,
)

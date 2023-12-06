package com.yourssu.ssudateserver.dto.request

import javax.validation.constraints.Size

class ContactRequestDto(
    @field:Size(message = "인증코드는 10자리 입니다.", min = 10, max = 10)
    val code: String,
    val nickName: String,
)

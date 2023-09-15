package com.yourssu.ssudateserver.dto.request

import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.enums.MaleAnimals
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Size

@ApiModel
class RegisterMaleRequestDto(
    @field:Size(message = "인증코드는 10자리 입니다.", min = 10, max = 10)
    val code: String,
    @ApiModelProperty
    val animals: MaleAnimals,
    val nickName: String,
    val mbti: MBTI,
    val introduce: String,
    val contact: String,
)

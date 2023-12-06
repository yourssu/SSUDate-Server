package com.yourssu.ssudateserver.entity

import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Column(name = "animals")
    @field:Enumerated(EnumType.STRING)
    val animals: Animals,

    @field:Column(name = "mbti")
    @field:Enumerated(EnumType.STRING)
    val mbti: MBTI,

    @field:Column(name = "nick_name", unique = true)
    val nickName: String,

    @field:Column(name = "introduction", length = 100)
    val introduction: String,

    @field:Column(name = "contact")
    val contact: String,

    @field:Column(name = "weight")
    var weight: Int = 0,

    @field:Column(name = "gender")
    @field:Enumerated(EnumType.STRING)
    val gender: Gender,

    @field:Column(name = "created_at")
    val createdAt: LocalDateTime,
)

package com.yourssu.ssudateserver.entity

import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import com.yourssu.ssudateserver.enums.MBTI
import com.yourssu.ssudateserver.enums.RoleType
import com.yourssu.ssudateserver.exception.logic.UnderZeroChanceInputCodeException
import com.yourssu.ssudateserver.exception.logic.UnderZeroTicketException
import java.time.LocalDateTime
import java.time.LocalDateTime.now
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
    var mbti: MBTI,
    @field:Column(name = "nick_name", unique = true)
    var nickName: String,
    @field:Column(name = "o_auth_name", unique = true)
    val oauthName: String,
    @field:Column(name = "introduction", length = 100)
    var introduce: String,
    @field:Column(name = "contact")
    var contact: String,
    @field:Column(name = "weight")
    var weight: Int = 0,
    @field:Column(name = "ticket")
    var ticket: Int = 2,
    @field:Column(name = "gender")
    @field:Enumerated(EnumType.STRING)
    val gender: Gender,
    @field:Column(name = "role")
    @field:Enumerated(EnumType.STRING)
    val role: RoleType,
    @field:Column(name = "code")
    var code: String,
    @field:Column(name = "code_input_chance")
    var codeInputChance: Int = 1,
    @field:Column(name = "created_at")
    val createdAt: LocalDateTime,
) {
    fun updateInfo(
        nickName: String,
        mbti: MBTI,
        introduce: String,
        contact: String,
    ): User {
        this.nickName = nickName
        this.mbti = mbti
        this.introduce = introduce
        this.contact = contact
        return this
    }

    fun contactTo(toUser: User): Follow {
        if (ticket <= 0) {
            throw UnderZeroTicketException("이용권이 필요한 기능입니다. 이용권을 충전해 주세요.")
        }
        ticket--

        toUser.increaseWeight()
        return Follow(fromUserId = this.id!!, toUserId = toUser.id!!, createdAt = now())
    }

    fun registerCode(toUser: User): Code {
        if (codeInputChance <= 0) {
            throw UnderZeroChanceInputCodeException("코드입력 기회가 남아있지 않아요.")
        }

        codeInputChance--
        ticket++
        toUser.ticket++

        return Code(fromCode = code, toCode = toUser.code, createdAt = now())
    }

    private fun increaseWeight() {
        weight++
    }
}

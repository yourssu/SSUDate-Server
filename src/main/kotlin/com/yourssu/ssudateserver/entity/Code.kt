package com.yourssu.ssudateserver.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "code")
class Code(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Column(name = "from_code")
    val fromCode: String,

    @field:Column(name = "to_code")
    val toCode: String,

    @field:Column(name = "created_at")
    val createdAt: LocalDateTime,

)

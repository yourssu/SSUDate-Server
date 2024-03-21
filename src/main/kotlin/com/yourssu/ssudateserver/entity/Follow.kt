package com.yourssu.ssudateserver.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "follow")
class Follow(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @field:Column(name = "from_user_id")
    val fromUserId: Long,
    @field:Column(name = "to_user_id")
    val toUserId: Long,
    @field:Column(name = "created_at")
    val createdAt: LocalDateTime,
)

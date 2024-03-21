package com.yourssu.ssudateserver.repository

import com.yourssu.ssudateserver.entity.Follow
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository : JpaRepository<Follow, Long> {
    fun findAllByFromUserId(fromUserId: Long): List<Follow>

    fun findByFromUserIdAndToUserId(
        fromUserId: Long,
        toUserId: Long,
    ): Follow?
}

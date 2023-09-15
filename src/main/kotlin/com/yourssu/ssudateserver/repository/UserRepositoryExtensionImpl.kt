package com.yourssu.ssudateserver.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.yourssu.ssudateserver.entity.QUser
import com.yourssu.ssudateserver.entity.User
import com.yourssu.ssudateserver.enums.Animals
import com.yourssu.ssudateserver.enums.Gender
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryExtensionImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : UserRepositoryExtension {
    override fun getUserWithAnimals(gender: Gender, animals: Animals, pageable: Pageable): Page<User> {
        val content = jpaQueryFactory.selectFrom(QUser.user)
            .where(
                eqGender(gender),
                eqAnimals(animals)
            )
            .orderBy(QUser.user.weight.asc())
            .orderBy(QUser.user.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
        val count = jpaQueryFactory
            .select(QUser.user.count())
            .from(QUser.user)
            .where(
                eqGender(gender),
                eqAnimals(animals)
            )
        return PageImpl(content, pageable, count.fetchOne()!!)
    }
    private fun eqGender(gender: Gender): BooleanExpression? {
        return QUser.user.gender.eq(gender)
    }
    private fun eqAnimals(animals: Animals): BooleanExpression? {
        if (animals == Animals.ALL) {
            return null
        }
        return QUser.user.animals.eq(animals)
    }
}

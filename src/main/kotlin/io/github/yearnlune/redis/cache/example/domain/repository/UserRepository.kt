package io.github.yearnlune.redis.cache.example.domain.repository

import io.github.yearnlune.redis.cache.example.domain.entity.User
import io.github.yearnlune.redis.cache.example.util.UUID36
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, UUID36> {

    fun findUsersByIdIn(ids: List<UUID36>): List<User>
}
package io.github.yearnlune.redis.cache.example.domain.dto

import io.github.yearnlune.redis.cache.example.util.UUID36
import java.time.LocalDateTime

class UserDTO {

    data class Summary(
        override val id: UUID36,
        val name: String,
        val bio: String?,
        val location: String?,
        val email: String,
        val homepage: String?,
        val updatedAt: LocalDateTime?
    ) : CacheBaseDTO
}
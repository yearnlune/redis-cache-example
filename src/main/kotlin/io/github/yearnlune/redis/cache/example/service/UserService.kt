package io.github.yearnlune.redis.cache.example.service

import io.github.yearnlune.redis.cache.example.config.CacheConfig
import io.github.yearnlune.redis.cache.example.domain.dto.UserDTO
import io.github.yearnlune.redis.cache.example.domain.repository.UserRepository
import io.github.yearnlune.redis.cache.example.util.UUID36
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val cacheService: CacheService
) {

    @Cacheable(cacheNames = ["user:summary"], key = "#id")
    fun findUserSummary(id: UUID36): UserDTO.Summary = userRepository.findById(id).orElseThrow().toSummary()

    fun findUserSummariesWithUserIds(ids: List<UUID36>) =
        cacheService.findCollectionByIdsWithCache(ids, CacheConfig.USER_KEY_PREFIX, userRepository::findUsersByIdIn)
            .filterIsInstance<UserDTO.Summary>()
}
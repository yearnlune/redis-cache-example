package io.github.yearnlune.redis.cache.example.service

import io.github.yearnlune.redis.cache.example.config.CacheConfig
import io.github.yearnlune.redis.cache.example.domain.dto.UserDTO
import io.github.yearnlune.redis.cache.example.domain.repository.UserRepository
import io.github.yearnlune.redis.cache.example.util.UUID36
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
    val cacheService: CacheService
) {

    @Cacheable(cacheNames = ["user:summary"], key = "#id")
    fun findUserSummary(id: UUID36): UserDTO.Summary = userRepository.findById(id).orElseThrow().toSummary()

    @Transactional
    @CachePut(cacheNames = ["user:summary"], key = "#id")
    fun updateUser(id: UUID36, userSummary: UserDTO.Summary): UserDTO.Summary {
        val user = userRepository.findById(id).orElseThrow()
        return user.setWithUserSummary(userSummary).toSummary()
    }

    @Transactional
    @CacheEvict(cacheNames = ["user:summary"], key = "#id")
    fun deleteUser(id: UUID36) {
        val user = userRepository.findById(id).orElseThrow()
        user.deleted = true
    }

    fun findUserSummariesWithUserIds(ids: Set<UUID36>) =
        cacheService.findCollectionByIdsWithCache(ids, CacheConfig.USER_KEY_PREFIX, userRepository::findUsersByIdIn)
            .filterIsInstance<UserDTO.Summary>()
}
package io.github.yearnlune.redis.cache.example.controller

import io.github.yearnlune.redis.cache.example.service.UserService
import io.github.yearnlune.redis.cache.example.util.UUID36
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    val userService: UserService
) {

    @GetMapping("/{id}")
    fun findUserSummary(@PathVariable id: UUID36) = ResponseEntity.ok(userService.findUserSummary(id))

    @PostMapping
    fun findUserSummaries(@RequestBody ids: List<UUID36>) =
        ResponseEntity.ok(userService.findUserSummariesWithUserIds(ids))
}
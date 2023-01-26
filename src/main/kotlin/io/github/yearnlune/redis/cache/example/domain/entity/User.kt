package io.github.yearnlune.redis.cache.example.domain.entity

import io.github.yearnlune.redis.cache.example.domain.dto.UserDTO
import io.github.yearnlune.redis.cache.example.util.UUID36
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "OT_USER")
@Entity
class User(

    @Id
    @Column(columnDefinition = "char(36)")
    val id: UUID36,

    val name: String,

    val bio: String,

    val location: String,

    val email: String,

    val homepage: String,

    @UpdateTimestamp
    val updatedAt: LocalDateTime,

    @CreationTimestamp
    val createdAt: LocalDateTime,

    @Column
    val deleted: Boolean = false
) : CacheBase {

    fun toSummary(): UserDTO.Summary = UserDTO.Summary(id, name, bio, location, email, homepage)

    override fun toCacheBaseDTO(): UserDTO.Summary = toSummary()
}
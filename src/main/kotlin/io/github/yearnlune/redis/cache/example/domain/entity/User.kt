package io.github.yearnlune.redis.cache.example.domain.entity

import io.github.yearnlune.redis.cache.example.domain.dto.UserDTO
import io.github.yearnlune.redis.cache.example.util.UUID36
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "OT_USER")
@Entity
@Where(clause = "deleted = false")
class User(

    @Id
    @Column(columnDefinition = "char(36)")
    val id: UUID36,

    var name: String,

    var bio: String?,

    var location: String?,

    var email: String,

    var homepage: String?,

    @UpdateTimestamp
    var updatedAt: LocalDateTime,

    @CreationTimestamp
    val createdAt: LocalDateTime,

    var deleted: Boolean = false
) : CacheBase {

    fun toSummary(): UserDTO.Summary = UserDTO.Summary(id, name, bio, location, email, homepage, updatedAt)

    fun setWithUserSummary(summary: UserDTO.Summary): User {
        name = summary.name
        bio = summary.bio
        location = summary.location
        email = summary.email
        homepage = summary.homepage
        updatedAt = LocalDateTime.now()

        return this
    }

    override fun toCacheBaseDTO(): UserDTO.Summary = toSummary()
}
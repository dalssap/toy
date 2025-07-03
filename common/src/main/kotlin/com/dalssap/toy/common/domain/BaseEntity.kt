package com.dalssap.toy.common.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.ZonedDateTime


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    @CreatedBy
    open var createdBy: String = "SYSTEM",

    @LastModifiedBy
    open var updatedBy: String = "SYSTEM",

    @Column(updatable = false)
    @CreatedDate
    open var createdAt: ZonedDateTime = ZonedDateTime.now(),

    @LastModifiedDate
    open var updatedAt: ZonedDateTime = createdAt,
)


@MappedSuperclass
abstract class LearningEntity(learnedAt: ZonedDateTime? = null): BaseEntity() {
    open var learnedDate: LocalDate = learnedAt?.toLocalDate() ?: LocalDate.now()
    open var learnedAt: ZonedDateTime = learnedAt ?: createdAt
}

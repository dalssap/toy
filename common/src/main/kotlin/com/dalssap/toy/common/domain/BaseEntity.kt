package com.dalssap.toy.common.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    @CreatedBy
    val createdBy: String = "SYSTEM",

    @LastModifiedBy
    val updatedBy: String = "SYSTEM",

    @Column(updatable = false)
    @CreatedDate
    val createdAt: ZonedDateTime = ZonedDateTime.now(),

    @LastModifiedDate
    val updatedAt: ZonedDateTime = createdAt,
)

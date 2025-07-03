package com.dalssap.toy.common.domain.common

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

class RepositoryFunction private constructor()

fun <ID : Any, T : Any> JpaRepository<T, ID>.findByIdOrElseThrow(
    id: ID,
    exception: () -> Exception = { RuntimeException("Entity not found: ${this::class.simpleName}") }
): T = findByIdOrNull(id) ?: throw exception()

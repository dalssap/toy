package com.dalssap.toy.common.domain.concept.reposiroty

import com.dalssap.toy.common.domain.concept.Concept
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ConceptRepository: JpaRepository<Concept, Long> {

    @Query("""
        SELECT c
          FROM Concept c
    JOIN FETCH c.leaves
         WHERE c.id = :id
    """)
    fun loadById(id: Long): Concept?
}

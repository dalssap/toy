package com.dalssap.toy.common.domain.concept.reposiroty

import com.dalssap.toy.common.domain.concept.Concept
import org.springframework.data.jpa.repository.JpaRepository

interface ConceptRepository: JpaRepository<Concept, Long>

package com.dalssap.toy.common.domain.concept

import com.dalssap.toy.common.domain.concept.reposiroty.ConceptRepository
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull


@DataJpaTest
class ConceptTest(
    @Autowired val conceptRepository: ConceptRepository,
    @Autowired val entityManager: EntityManager
) {

    @Test
    fun saveTest() {
        val entity = Concept("category", "name", "meaning")

        conceptRepository.save(entity)
        entityManager.clear()

        val saved = conceptRepository.findByIdOrNull(1)

        assertThat(saved).isNotNull
    }
}
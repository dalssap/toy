package com.dalssap.toy.common.domain.concept

import com.dalssap.toy.common.QueryCountHolder
import com.dalssap.toy.common.domain.common.findByIdOrElseThrow
import com.dalssap.toy.common.domain.concept.reposiroty.ConceptRepository
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull


@DataJpaTest
class ConceptTest(
    @Autowired val conceptRepository: ConceptRepository,
    @Autowired val entityManager: EntityManager,
) {

    @Test
    fun saveTest() {
        val entity = Concept("name", "meaning")

        val id = conceptRepository.save(entity).id
        entityManager.clear()

        val saved = conceptRepository.findByIdOrNull(id)

        assertThat(saved).isNotNull
    }

    @Test
    @DisplayName("컨셉 로드 이후 재귀적 leaf 추가 로드를 없애기 위해 자체적 자료구조 추가")
    fun tidyLeavesTest() {
        val map = mapOf(
            "depth1-1" to mapOf("depth2" to "depth2 value"),
            "depth1-2" to "depth1-2 value",
            "depth1-3" to mapOf("depth3" to "depth3 value")
        )

        val kanji = Kanji.of("key", map)
        val id = conceptRepository.save(kanji).id
        entityManager.clear()

        val concept = conceptRepository.findByIdOrElseThrow(id)

        assertThat(concept.tidyLeaves.size).isEqualTo(3)
        assertThat(concept.tidyLeaves[0].tidyLeaves.size).isOne
        assertThat(concept.tidyLeaves[0].tidyLeaves[0].label).isEqualTo("DEPTH2")
        assertThat(concept.tidyLeaves[0].tidyLeaves[0].content).isEqualTo("depth2 value")
        assertThat(concept.tidyLeaves[1].tidyLeaves).isEmpty()
        assertThat(concept.tidyLeaves[2].tidyLeaves.size).isOne
        assertThat(concept.tidyLeaves[2].tidyLeaves[0].label).isEqualTo("DEPTH3")
        assertThat(concept.tidyLeaves[2].tidyLeaves[0].content).isEqualTo("depth3 value")
    }

    @Test
    @DisplayName("컨셉 로드시 쿼리 한번으로 가져오는지 확인")
    fun queryExecutionTest() {
        QueryCountHolder.clear()

        //given
        val map = mapOf(
            "depth1-1" to mapOf("depth2" to "depth2 value"),
            "depth1-2" to "depth1-2 value",
            "depth1-3" to mapOf("depth3" to "depth3 value")
        )

        val kanji = Kanji.of("key", map)
        val id = conceptRepository.save(kanji).id

        //when
        val concept = conceptRepository.loadById(id)

        //then
        assertThat(concept).isNotNull
        assertThat(QueryCountHolder.selectCount()).isOne
    }
}
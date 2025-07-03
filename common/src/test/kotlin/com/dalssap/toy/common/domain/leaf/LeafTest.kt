package com.dalssap.toy.common.domain.leaf

import com.dalssap.toy.common.domain.concept.Concept
import com.dalssap.toy.common.domain.concept.Kanji
import com.dalssap.toy.common.domain.concept.Kanji.Companion.KUNON
import com.dalssap.toy.common.domain.concept.Kanji.Companion.KUN_EXAMPLES
import com.dalssap.toy.common.domain.concept.Kanji.Companion.ON
import com.dalssap.toy.common.domain.concept.Kanji.Companion.ON_EXAMPLES
import com.dalssap.toy.common.domain.concept.reposiroty.ConceptRepository
import com.dalssap.toy.common.domain.concept.reposiroty.KanjiRepository
import com.dalssap.toy.common.domain.leaf.repository.LeafRepository
import com.dalssap.toy.common.function.join
import com.dalssap.toy.common.function.toMap
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest


@DataJpaTest
class LeafTest(
    @Autowired val conceptRepository: ConceptRepository,
    @Autowired val kanjiRepository: KanjiRepository,
    @Autowired val leafRepository: LeafRepository,
    @Autowired val entityManager: EntityManager
) {

    @ParameterizedTest
    @DisplayName("원시 타입의 경우 key value 로 저장된다.")
    @MethodSource("primitiveTestCases")
    fun primitiveTest(map: Map<String, Any>) {
        val leaves = Leaf.of(CONCEPT, map)
        
        assertThat(leaves).hasSize(1)
        assertThat(leaves[0].label).isEqualTo(map.keys.first())
        assertThat(leaves[0].content).isEqualTo(map[map.keys.first()].toString())
    }

    @ParameterizedTest
    @DisplayName("원시 타입의 리스트의 경우 하나의 key value 로 저장된다.")
    @MethodSource("primitiveListOneTestCases")
    fun primitiveListOneTest(map: Map<String, Any>) {
        val leaves = Leaf.of(CONCEPT, map)

        assertThat(leaves).hasSize(1)
        assertThat(leaves[0].label).isEqualTo(map.keys.first())
        assertThat(leaves[0].content).isEqualTo((map[map.keys.first()] as List<*>).first())
    }

    @ParameterizedTest
    @DisplayName("원시 타입의 리스트가 비어있을 경우에는 저장하지 않는다.")
    @MethodSource("primitiveListZeroTestCases")
    fun primitiveListZeroTest(map: Map<String, Any>) {
        val leaves = Leaf.of(CONCEPT, map)

        assertThat(leaves).isEmpty()
    }

    @ParameterizedTest
    @DisplayName("객체(map)의 경우에는 depth 가 하나 더 생긴다.")
    @MethodSource("objectTypeTestCases")
    fun objectTypeTest(map: Map<String, Any>) {
        val roots = Leaf.of(CONCEPT, map)
        val root = roots[0]
        val leaf = root.leaves.first()

        // 구초 확인
        assertThat(roots).hasSize(1)
        assertThat(root.leaves).hasSize(1)

        // root 확인
        assertThat(root.label).isEqualTo(map.keys.first())
        assertThat(root.content).isNull()

        // leaves 확인
        val subMap = (map.values.first() as Map<*, *>)
        assertThat(leaf.label).isEqualTo(subMap.keys.first())
        assertThat(leaf.content).isEqualTo(subMap.values.first())
    }

    @Test
    @DisplayName("DB에서 로드하고 나서의 순서는 map의 순서와 같다")
    fun loadTest() {
        val map = mapOf(
            "depth1-1" to mapOf("depth2" to "depth2 value"),
            "depth1-2" to "depth1-2 value",
            "depth1-3" to mapOf("depth3" to "depth3 value")
        )

        val kanji = Kanji.of("key", map)
        conceptRepository.save(kanji)
        entityManager.clear()

        val concepts = conceptRepository.findAll()
        val concept = concepts.first()
        val leaves = concept.leaves

        assertThat(concepts).hasSize(1)
        assertThat(leaves[0].label).isEqualTo("DEPTH1-1")
        assertThat(leaves[0].content).isNull()
        assertThat(leaves[1].label).isEqualTo("DEPTH2")
        assertThat(leaves[2].label).isEqualTo("DEPTH1-2")
        assertThat(leaves[3].label).isEqualTo("DEPTH1-3")
        assertThat(leaves[4].label).isEqualTo("DEPTH3")
    }

    companion object {
        val CONCEPT = Concept("name", "meaning")

        @JvmStatic
        fun primitiveTestCases() = listOf(
            Arguments.of(mapOf("string" to "value")),
            Arguments.of(mapOf("number" to 123)),
            Arguments.of(mapOf("boolean" to true)),
            Arguments.of(mapOf("double" to 123.45)),
        )

        @JvmStatic
        fun primitiveListOneTestCases() = listOf(
            Arguments.of(mapOf("string" to listOf("value")))
        )

        @JvmStatic
        fun primitiveListZeroTestCases() = listOf(
            Arguments.of(mapOf("string" to emptyList<Any>()))
        )

        @JvmStatic
        fun objectTypeTestCases() = listOf(
            Arguments.of(
                mapOf("object" to
                        mapOf("string" to "value")
                )
            )
        )

    }
}
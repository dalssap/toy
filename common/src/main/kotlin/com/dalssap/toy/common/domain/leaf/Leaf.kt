package com.dalssap.toy.common.domain.leaf

import com.dalssap.toy.common.domain.LearningEntity
import com.dalssap.toy.common.domain.concept.Concept
import com.dalssap.toy.common.function.isOne
import jakarta.persistence.*
import java.time.ZonedDateTime

/**
 * 컨셉
 * json 에서 key가 type 이 되며, value 는 details 이 된다.
 * 다만 value 가 객체({})일 때에는 해당 객체 내의 key가 detail 이 되며,
 * 이를 target_id 로 하는 하위 계층의 Tag 를 생성한다.
 */

@Entity
@Table(name = "leaves")
class Leaf(
    @ManyToOne
    @JoinColumn(name = "concept_id")
    val concept: Concept,

    val label: String,

    val content: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_id")
    val root: Leaf? = null,

    @OneToMany(mappedBy = "root", cascade = [CascadeType.ALL])
    val leaves: MutableList<Leaf> = mutableListOf(),

    learnedAt: ZonedDateTime? = null
): LearningEntity(learnedAt) {
    @Transient
    private var _tidyLeaves: MutableList<Leaf>? = null

    val tidyLeaves: MutableList<Leaf>
        get() = _tidyLeaves ?: mutableListOf<Leaf>().also { _tidyLeaves = it }

    fun iz(key: String) = label.equals(key, ignoreCase = true)

    fun print(depth: Int = 0) {
        val indent = "  ".repeat(depth)
        println("$indent$label : $content")
        tidyLeaves.forEach {
            it.print(depth + 1)
        }
    }

    companion object {
        fun of(concept: Concept, map: Map<String, Any?>, learnedAt: ZonedDateTime? = null): List<Leaf> {

            return map.map { (k, v) -> of(concept,k, v, learnedAt = learnedAt) }.flatten()
        }

        fun of(concept: Concept, key: String, value: Any?, rootLeaf: Leaf? = null, learnedAt: ZonedDateTime? = null): List<Leaf> {

            return when (value) {
                is Map<*, *> -> {
                    if (value.isEmpty()) emptyList<Leaf>()

                    return listOf(
                        Leaf(concept, key, null, rootLeaf).apply {
                            value.map { (subKey, subValue) -> subKey as String
                                leaves.addAll(of(concept, subKey, subValue, this, learnedAt))
                            }
                        }
                    )
            }
                is List<*> -> {
                    if (value.isEmpty()) return emptyList()
                    if (value.isOne()) return listOf(Leaf(concept, key, value.first() as String, rootLeaf, learnedAt = learnedAt))

                    value.map { item ->
                        when (item) {
                            is Map<*, *> -> {
                                val leave = Leaf(concept, key, null, rootLeaf, learnedAt = learnedAt)
                                leave.leaves.addAll(of(concept, key, item, leave, learnedAt = learnedAt))
                                leave
                            }
                            else -> Leaf(concept, key, item.toString(), rootLeaf, learnedAt = learnedAt)
                        }
                    }
                }
                else -> listOf(Leaf(concept, key, value.toString(), rootLeaf, learnedAt = learnedAt))
            }
        }



    }
}
package com.dalssap.toy.common.domain.concept

import com.dalssap.toy.common.domain.LearningEntity
import com.dalssap.toy.common.domain.leaf.Leaf
import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.stream.Stream

@Entity
@Table(name = "concepts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "category")
open class Concept(
    val name: String,

    val meaning: String? = null,

    @OneToMany(mappedBy = "concept", cascade = [CascadeType.ALL])
    val leaves: MutableList<Leaf> = mutableListOf(),

    learnedAt: ZonedDateTime? = null
): LearningEntity(learnedAt) {
    @Transient
    private var _tidyLeaves: MutableList<Leaf>? = null

    val tidyLeaves: MutableList<Leaf>
        get() = _tidyLeaves ?: mutableListOf<Leaf>().also { _tidyLeaves = it }

    protected fun leaf(key: String) = tidyLeaves.firstOrNull { it.iz(key) }

    protected fun leaves(vararg keys: String) = tidyLeaves.filter { leaf -> keys.any { leaf.iz(it) } }

    @PostLoad
    private fun tidyLeaves() {
        val checked = mutableSetOf<Leaf>()

        leaves.filter { it.root == null }
            .forEach { leaf ->
                tidyLeaf(leaf, checked)?.let {
                    tidyLeaves.add(it)
                }
            }

    }

    private fun tidyLeaf(target: Leaf, checked: MutableSet<Leaf>): Leaf? {
        if (checked.contains(target)) {
            return null
        }
        checked.plus(target)

        leaves.filter { it.root == target }
            .forEach { leaf ->
                tidyLeaf(leaf, checked)?.let {
                    target.tidyLeaves.add(it)
                }
            }

        return target
    }

}

fun Stream<Leaf>.filterByLabel(vararg labels: String): Stream<Leaf> {
    return this.filter { leaf -> labels.any { label -> leaf.label.equals(label, true) } }
}

fun Stream<Leaf>.depth(depth: Int = 0): Stream<Leaf> {
    if (depth == 0) return this

    return this.flatMap { leaf ->
        leaf.tidyLeaves.stream().depth(depth - 1)
    }
}

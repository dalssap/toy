package com.dalssap.toy.common.domain.leaf.repository

import com.dalssap.toy.common.domain.leaf.Leaf
import org.springframework.data.jpa.repository.JpaRepository

interface LeafRepository: JpaRepository<Leaf, Long>

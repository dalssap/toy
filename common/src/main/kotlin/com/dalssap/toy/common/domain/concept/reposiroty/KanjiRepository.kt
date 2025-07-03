package com.dalssap.toy.common.domain.concept.reposiroty

import com.dalssap.toy.common.domain.concept.Kanji
import org.springframework.data.jpa.repository.JpaRepository

interface KanjiRepository: JpaRepository<Kanji, Long>

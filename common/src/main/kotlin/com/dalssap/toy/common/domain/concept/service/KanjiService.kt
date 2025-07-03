package com.dalssap.toy.common.domain.concept.service

import com.dalssap.toy.common.domain.common.findByIdOrElseThrow
import com.dalssap.toy.common.domain.concept.Kanji
import com.dalssap.toy.common.domain.concept.reposiroty.KanjiRepository
import org.springframework.stereotype.Service

@Service
@Suppress("UNCHECKED_CAST")
class KanjiService(
    val kanjiRepository: KanjiRepository
) {

    fun load(id: Long) = kanjiRepository.findByIdOrElseThrow(id)

    fun save(data: Map<String, Any?>) {

        data.map { (kanji, value) ->
            Kanji.of(kanji, value as Map<String, Any?>)
        }.let { kanjiRepository.saveAll(it) }
    }
}
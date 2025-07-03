package com.dalssap.toy.common.domain.concept.service

import com.dalssap.toy.common.domain.common.findByIdOrElseThrow
import com.dalssap.toy.common.domain.concept.Common
import com.dalssap.toy.common.domain.concept.Concept
import com.dalssap.toy.common.domain.concept.reposiroty.CommonRepository
import org.springframework.stereotype.Service

@Service
@Suppress("UNCHECKED_CAST")
class CommonService(
    private val commonRepository: CommonRepository
) {
    fun get(id: Long): Concept {
        return commonRepository.findByIdOrElseThrow(id)
    }

    fun save(key: String, value: String): Concept {
        return commonRepository.save(Common(key, value))
    }
}
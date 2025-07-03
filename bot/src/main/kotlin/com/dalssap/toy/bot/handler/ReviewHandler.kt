package com.dalssap.toy.bot.handler

import com.dalssap.toy.bot.common.ButtonAttachedMessageResponse
import com.dalssap.toy.bot.common.CommandHandler
import com.dalssap.toy.bot.common.CommandHandlers
import com.dalssap.toy.bot.common.request.CommandRequest
import com.dalssap.toy.common.domain.concept.service.CommonService
//
//@CommandHandlers(command = "review", aliases = ["r", "리뷰"])
//class ReviewHandler(
//    private val commonService: CommonService
//) {
//
//    @CommandHandler
//    fun review(request: CommandRequest): ButtonAttachedMessageResponse {
//
//        println("message: ${request.message()}")
//        request.options().forEach { key ->
//            println("option: ${request.option(key)}")
//        }
//
//        return ButtonAttachedMessageResponse(
//            """
//                T
//                E
//                S
//                T
//            """.trimIndent(),
//            emptyList()
//        )
//    }
//
//    @CommandHandler("concept", aliases = ["c", "컨셉"])
//    fun addConcept(request: CommandRequest): String {
//        if (!request.hasOption("k") || !request.hasOption("v")) {
//            return "/concept -k key -v value"
//        }
//
//        val concept = commonService.save(request.option("k")!!, request.option("v")!!)
//
//        return "등록 완료 (id: ${concept.id})"
//    }
//
//    @CommandHandler("view")
//    fun viewConcept(request: CommandRequest): String {
//        if (!request.hasOption("id")) {
//            return "/view -k key -v value"
//        }
//        val concept = commonService.get(request.option("id")!!.toLong())
//
//        return "${concept.name}: ${concept.meaning}"
//    }
//}
package com.dalssap.toy.common.domain.concept

import com.dalssap.toy.common.domain.leaf.Leaf
import com.dalssap.toy.common.function.join
import com.dalssap.toy.common.function.uppercaseKeys
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.time.ZonedDateTime

@Entity
@DiscriminatorValue("KANJI")
class Kanji(name: String, meaning: String?, learnedAt: ZonedDateTime? = null) : Concept(name, meaning, learnedAt = learnedAt) {

    companion object {
        const val KUNON = "KUNON"
        const val ON = "ON"
        const val KUN = "KUN"
        const val ON_EXAMPLES = "ON_EXAMPLES"
        const val KUN_EXAMPLES = "KUN_EXAMPLES"
        const val THUMBNAIL = "THUMBNAIL"
        const val DETAIL_URL = "DETAIL_URL"
        const val INDEX = "INDEX"

        fun of(key: String, value: Map<String, Any?>, learnedAt: ZonedDateTime? = null): Kanji {
            val meaning = value.uppercaseKeys()
            val kunon = meaning[KUNON].join(", ")

            return Kanji(key, kunon, learnedAt).apply {
                leaves.addAll(Leaf.of(this, meaning, learnedAt))
            }
        }
    }

    fun kanji() = name

    fun kunons() = meaning

    fun index() = leaf(INDEX)?.content

    fun ons() = leaves(ON).mapNotNull { it.content }

    fun kuns() = leaves(KUN).mapNotNull { it.content }

    fun onExamples() = leaves(ON_EXAMPLES)

    fun kunExamples() = leaves(KUN_EXAMPLES)

    fun thumbnail() = leaf(THUMBNAIL)?.content

    fun imageUrl() = leaf(DETAIL_URL)?.content

    fun tangoLeaves(): List<Leaf> = leaves.stream()
            .filterByLabel(ON_EXAMPLES, KUN_EXAMPLES)
            .depth(2)
            .toList()

    fun tangos() = tangoLeaves()
            .map { it.label }
            .toList()
}

/**
    {
    "日": {
        "KUNON": [
            "해 일",
            "날 일",
            "일본 일"
        ],
        "ON": [
            "にち",
            "じつ"
        ],
        "KUN": [
            "ひ",
            "숫자 + か"
        ],
        "BUSHU": [],
        "ON_EXAMPLES": {
            "にち": {
                "日光": {
                    "YOMIKATA": "にっこう",
                    "MEANING": [
                        "일광"
                    ]
                },
                "毎日": {
                    "YOMIKATA": "まいにち",
                    "MEANING": [
                        "매일"
                    ]
                },
                "来日": {
                    "YOMIKATA": "らいにち",
                    "MEANING": [
                        "일본에 옴"
                    ]
                }
            },
            "じつ": {
                "平日": {
                    "YOMIKATA": "へいじつ",
                    "MEANING": [
                        "평일"
                    ]
                },
                "当日": {
                    "YOMIKATA": "とうじつ",
                    "MEANING": [
                        "당일"
                    ]
                },
                "昨日": {
                    "YOMIKATA": "さくじつ",
                    "MEANING": [
                        "어제"
                    ]
                }
            },
            "ON_EXCEPTIONS": {
                "日本": {
                    "YOMIKATA": "にほん",
                    "MEANING": [
                        "일본"
                    ]
                }
            }
        },
        "KUN_EXAMPLES": {
            "ひ": {
                "日": {
                    "YOMIKATA": "ひ",
                    "MEANING": [
                        "해"
                    ]
                },
                "日の出": {
                    "YOMIKATA": "ひので",
                    "MEANING": [
                        "일출"
                    ]
                },
                "日々": {
                    "YOMIKATA": "ひび",
                    "MEANING": [
                        "나날"
                    ]
                },
                "日にち": {
                    "YOMIKATA": "ひにち",
                    "MEANING": [
                        "며칠(일정, 기간)"
                    ]
                }
            },
            "숫자 + か": {
                "二日": {
                    "YOMIKATA": "ふつか",
                    "MEANING": [
                        "2일"
                    ]
                },
                "八日": {
                    "YOMIKATA": "ようか",
                    "MEANING": [
                        "8일"
                    ]
                }
            },
            "KUN_EXCEPTIONS": {
                "明日": {
                    "YOMIKATA": "あした/あす",
                    "MEANING": [
                        "내일"
                    ]
                },
                "今日": {
                    "YOMIKATA": "きょう/こんにち",
                    "MEANING": [
                        "오늘"
                    ]
                },
                "一日": {
                    "YOMIKATA": "ついたち/いちにち",
                    "MEANING": [
                        "1일"
                    ]
                },
                "昨日": {
                    "YOMIKATA": "きのう/さくじつ",
                    "MEANING": [
                        "어제"
                    ]
                    }
                }
            }
        },
        "INDEX": "0015",
        "DETAIL_IMG": "https://storage.googleapis.com/nokchax-dev.firebasestorage.app/kanji/0015.jpeg",
        "THUMBNAIL": "https://storage.googleapis.com/nokchax-dev.firebasestorage.app/kanji/0015_thumbnail.jpg"
    }
 */
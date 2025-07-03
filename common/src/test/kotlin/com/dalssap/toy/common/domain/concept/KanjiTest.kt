package com.dalssap.toy.common.domain.concept

import com.dalssap.toy.common.domain.common.findByIdOrElseThrow
import com.dalssap.toy.common.domain.concept.reposiroty.KanjiRepository
import com.dalssap.toy.common.function.join
import com.dalssap.toy.common.function.toMap
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@Suppress("UNCHECKED_CAST")
class KanjiTest(
    @Autowired val kanjiRepository: KanjiRepository,
    @Autowired val entityManager: EntityManager
) {

    @Test
    @DisplayName("leaf 생성 테스트")
    fun leafConstructorTest() {
        val id = kanjiRepository.save(KANJI_ENTITY).id
        entityManager.clear()
        val kanji = kanjiRepository.findByIdOrElseThrow(id)

        assertThat(kanji.kanji()).isEqualTo(KANJI)
        assertThat(kanji.meaning).isEqualTo(KUNON)
        assertThat(kanji.kunons()).isEqualTo(KUNON)
        assertThat(kanji.index()).isEqualTo(INDEX)
        assertThat(kanji.thumbnail()).isEqualTo(THUMBNAIL)
        assertThat(kanji.imageUrl()).isEqualTo(DETAIL_URL)
        assertThat(kanji.ons()).containsAll(ON as List<String>)
        assertThat(kanji.kuns()).containsAll(KUN as List<String>)
        assertThat(kanji.tangos()).containsAll(TANGOS)
    }

    companion object {
        val TEST_CASE = """
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
        """.trimIndent()
        val CASE = TEST_CASE.toMap()
        val KANJI = CASE.keys.first()
        val VALUE = CASE.values.first() as Map<String, Any>
        val KANJI_ENTITY = Kanji.of(KANJI, VALUE)
        val KUNON = VALUE[Kanji.KUNON].join(", ")
        val ON = VALUE[Kanji.ON]
        val KUN = VALUE[Kanji.KUN]
        val THUMBNAIL = VALUE[Kanji.THUMBNAIL]
        val DETAIL_URL = VALUE[Kanji.DETAIL_URL]
        val INDEX = VALUE[Kanji.INDEX]
        val ON_EXAMPLES = VALUE[Kanji.ON_EXAMPLES] as Map<String, Any>
        val KUN_EXAMPLES = VALUE[Kanji.KUN_EXAMPLES] as Map<String, Any>
        val ON_TANGOS = ON_EXAMPLES.values.map { it -> (it as Map<String, Any>).keys }.flatten()
        val KUN_TANGOS = KUN_EXAMPLES.values.map { it -> (it as Map<String, Any>).keys }.flatten()
        val TANGOS = ON_TANGOS.plus(KUN_TANGOS)
    }
}
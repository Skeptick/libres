package io.github.skeptick.libres.strings

import io.github.skeptick.libres.strings.PluralForm.*

fun interface PluralRule {
    fun getForm(number: Int): PluralForm
}

enum class PluralForm(internal val formName: String) {
    Zero("zero"),
    One("one"),
    Two("two"),
    Few("few"),
    Many("many"),
    Other("other")
}

object PluralRules {

    private val custom = mutableMapOf<String, PluralRule>()

    private val English = PluralRule { number ->
        when (number) {
            1 -> One
            else -> Other
        }
    }

    private val Russian = PluralRule { number ->
        when (number % 100) {
            11, 12, 13, 14 -> Many
            else -> when (number % 10) {
                1 -> One
                2, 3, 4 -> Few
                else -> Many
            }
        }
    }

    private val Ukrainian = PluralRule { number ->
        when (number % 100) {
            11, 12, 13, 14 -> Many
            else -> when (number % 10) {
                1 -> One
                2, 3, 4 -> Few
                else -> Many
            }
        }
    }

    private val Kazakh = PluralRule { number ->
        when (number) {
            1 -> One
            else -> Other
        }
    }

    private val French = PluralRule {  number ->
        when (number) {
            0, 1 -> One
            else -> Other
        }
    }

    private val Czech = PluralRule { number ->
        when (number) {
            1 -> One
            in (2..4) -> Few
            else -> Other
        }
    }

    private val Indonesian = PluralRule { Other }

    private val Polish = PluralRule { number ->
        when {
            number == 1 -> One
            (number % 10) in 2..4 && (number % 100) !in 12..14 -> Few
            else -> Many
        }
    }

    private val Chinese = PluralRule { Other }

    private val Uzbek = PluralRule { number ->
        when (number) {
            1 -> One
            else -> Other
        }
    }

    private val Japanese = PluralRule { Other }

    private val Korean = PluralRule { Other }

    operator fun set(languageCode: String, value: PluralRule) {
        custom[languageCode] = value
    }

    operator fun get(languageCode: String): PluralRule {
        return when (languageCode) {
            "en" -> English
            "ru" -> Russian
            "uk" -> Ukrainian
            "kk" -> Kazakh
            "fr" -> French
            "cs" -> Czech
            "id" -> Indonesian
            "pl" -> Polish
            "zh" -> Chinese
            "uz" -> Uzbek
            "ja" -> Japanese
            "ko" -> Korean
            else -> custom[languageCode] ?: error("Plural rule for '$languageCode' not provided")
        }
    }

    operator fun plus(value: Pair<String, PluralRule>) {
        custom[value.first] = value.second
    }

    operator fun plusAssign(map: Map<String, PluralRule>) {
        custom += map
    }

}
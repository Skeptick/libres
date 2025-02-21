package io.github.skeptick.libres.strings

import io.github.skeptick.libres.strings.PluralForm.*

public fun interface PluralRule {
    public fun getForm(number: Int): PluralForm
}

public enum class PluralForm(internal val formName: String) {
    Zero("zero"),
    One("one"),
    Two("two"),
    Few("few"),
    Many("many"),
    Other("other")
}

public object PluralRules {

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

    public operator fun set(languageCode: String, value: PluralRule) {
        custom[languageCode] = value
    }

    public operator fun get(languageCode: String): PluralRule {
        return when (languageCode) {
            "en" -> English
            "ru" -> Russian
            "uk" -> Ukrainian
            "kk" -> Kazakh
            "fr" -> French
            else -> custom[languageCode] ?: error("Plural rule for '$languageCode' not provided")
        }
    }

    public operator fun plus(value: Pair<String, PluralRule>) {
        custom[value.first] = value.second
    }

    public operator fun plusAssign(map: Map<String, PluralRule>) {
        custom += map
    }

}
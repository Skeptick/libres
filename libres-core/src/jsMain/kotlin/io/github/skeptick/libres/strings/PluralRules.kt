@file:Suppress("unused")

package io.github.skeptick.libres.strings

private const val zero = "zero"
private const val one = "one"
private const val two = "two"
private const val few = "few"
private const val many = "many"
private const val other = "other"

private fun englishPluralRule(number: Int): String {
    return when (number) {
        1 -> one
        else -> other
    }
}

private fun russianPluralRule(number: Int): String {
    return when (number % 100) {
        11, 12, 13, 14 -> many
        else -> when (number % 10) {
            1 -> one
            2, 3, 4 -> few
            else -> many
        }
    }
}

private fun ukrainianPluralRule(number: Int): String {
    return when (number % 100) {
        11, 12, 13, 14 -> many
        else -> when (number % 10) {
            1 -> one
            2, 3, 4 -> few
            else -> many
        }
    }
}

private fun kazakhPluralRule(number: Int): String {
    return when (number) {
        1 -> one
        else -> other
    }
}

private fun frenchPluralRule(number: Int): String {
    return when (number) {
        0, 1 -> one
        else -> other
    }
}

internal fun getPluralForm(languageCode: String, number: Int): String {
    return when (languageCode) {
        "en" -> englishPluralRule(number)
        "ru" -> russianPluralRule(number)
        "uk" -> ukrainianPluralRule(number)
        "kk" -> kazakhPluralRule(number)
        "fr" -> frenchPluralRule(number)
        else -> error("Plural rule for '$languageCode' not provided")
    }
}
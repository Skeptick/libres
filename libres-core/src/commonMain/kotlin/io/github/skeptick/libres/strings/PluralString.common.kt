@file:Suppress("unused")

package io.github.skeptick.libres.strings

class VoidPluralString(private val forms: PluralForms, private val languageCode: String) {
    fun format(number: Int) = getPluralizedString(forms, languageCode, number)
}

class VoidFormattedPluralString(private val forms: PluralForms, private val languageCode: String) {
    fun format(number: Int, vararg args: String) = formatString(getPluralizedString(forms, languageCode, number), args)
}

expect fun getPluralizedString(forms: PluralForms, languageCode: String, number: Int): String
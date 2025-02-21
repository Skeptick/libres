@file:Suppress("unused")

package io.github.skeptick.libres.strings

public class VoidPluralString(private val forms: PluralForms, private val languageCode: String) {
    public fun format(number: Int): String = getPluralizedString(forms, languageCode, number)
}

public class VoidFormattedPluralString(private val forms: PluralForms, private val languageCode: String) {
    public fun format(number: Int, vararg args: String): String = formatString(getPluralizedString(forms, languageCode, number), args)
}

public expect fun getPluralizedString(forms: PluralForms, languageCode: String, number: Int): String
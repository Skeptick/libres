package io.github.skeptick.libres.strings

import android.icu.text.PluralRules
import android.os.Build
import libcore.icu.NativePluralRules
import java.util.Locale

actual fun getPluralizedString(forms: PluralForms, languageCode: String, number: Int): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val rules = PluralRules.forLocale(Locale(languageCode))
        val form = rules.select(number.toDouble())
        forms[form] ?: error("Plural form '$form' not provided")
    } else {
        val rules = NativePluralRules.forLocale(Locale(languageCode))
        val form = rules.quantityForInt(number).stringForQuantityCode()
        forms[form] ?: error("Plural form '$form' not provided")
    }
}

private fun Int.stringForQuantityCode(): String =
    when (this) {
        NativePluralRules.ZERO -> "zero"
        NativePluralRules.ONE -> "one"
        NativePluralRules.TWO -> "two"
        NativePluralRules.FEW -> "few"
        NativePluralRules.MANY -> "many"
        else -> "other"
    }
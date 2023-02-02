package io.github.skeptick.libres.strings

import com.ibm.icu.text.PluralRules
import java.util.*

actual fun getPlatformLanguageCode(): String {
    return Locale.getDefault().language
}

actual fun getPluralizedString(forms: PluralForms, languageCode: String, number: Int): String {
    val rules = PluralRules.forLocale(Locale(languageCode))
    val form = rules.select(number.toDouble())
    return forms[form] ?: error("Plural form '$form' not provided")
}
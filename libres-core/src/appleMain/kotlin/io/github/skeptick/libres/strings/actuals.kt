package io.github.skeptick.libres.strings

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getPlatformLanguageCode(): String {
    return NSLocale.currentLocale.languageCode
}

actual fun getPluralizedString(forms: PluralForms, languageCode: String, number: Int): String {
    val form = getPluralForm(languageCode, number)
    return forms[form] ?: error("Plural form '$form' not provided")
}
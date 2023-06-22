package io.github.skeptick.libres.strings

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.preferredLanguages

actual fun getPlatformLanguageCode(): String {

    return NSLocale.preferredLanguages
        .firstOrNull()
        ?.toString()
        ?.split("-")
        ?.firstOrNull()
        ?: NSLocale.currentLocale().languageCode()
}

actual fun getPluralizedString(forms: PluralForms, languageCode: String, number: Int): String {
    val formName = PluralRules[languageCode].getForm(number).formName
    return forms[formName] ?: error("Plural form '$formName' not provided")
}
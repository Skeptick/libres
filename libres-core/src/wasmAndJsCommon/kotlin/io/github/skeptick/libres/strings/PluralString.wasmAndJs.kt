package io.github.skeptick.libres.strings

actual fun getPluralizedString(forms: PluralForms, languageCode: String, number: Int): String {
    val formName = PluralRules[languageCode].getForm(number).formName
    return forms[formName] ?: error("Plural form '$formName' not provided")
}
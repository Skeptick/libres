package io.github.skeptick.libres.strings

import io.github.skeptick.libres.plurals.IntPluralRules
import io.github.skeptick.libres.plurals.PluralForm

public class PluralForms(
    public val zero: String? = null,
    public val one: String? = null,
    public val two: String? = null,
    public val few: String? = null,
    public val many: String? = null,
    public val other: String? = null
)

public fun getPluralizedString(forms: PluralForms, languageCode: String, number: Int): String {
    val form = IntPluralRules.getPluralForm(languageCode, number)
    val result = when (form) {
        PluralForm.Zero -> forms.zero
        PluralForm.One -> forms.one
        PluralForm.Two -> forms.two
        PluralForm.Few -> forms.few
        PluralForm.Many -> forms.many
        PluralForm.Other -> forms.other
    }
    return result ?: forms.other ?: error("Plural form '$form' not provided")
}
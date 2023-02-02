package io.github.skeptick.libres.strings

class PluralForms(
    val zero: String? = null,
    val one: String? = null,
    val two: String? = null,
    val few: String? = null,
    val many: String? = null,
    val other: String? = null
)

internal operator fun PluralForms.get(key: String) =
    when (key) {
        "zero" -> zero
        "one" -> one
        "two" -> two
        "few" -> few
        "many" -> many
        else -> other
    }
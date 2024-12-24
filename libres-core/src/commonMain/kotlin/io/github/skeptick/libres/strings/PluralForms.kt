package io.github.skeptick.libres.strings

public class PluralForms(
    public val zero: String? = null,
    public val one: String? = null,
    public val two: String? = null,
    public val few: String? = null,
    public val many: String? = null,
    public val other: String? = null
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
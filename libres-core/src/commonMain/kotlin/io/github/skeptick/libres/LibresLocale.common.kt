package io.github.skeptick.libres

/**
 * A locale represented by BCP 47 language, script, and region subtags.
 *
 * @property[language] Language subtag, such as `ru` or `en`.
 * @property[script] Optional script subtag, such as `Latn` or `Cyrl`.
 * @property[region] Optional region subtag, such as `RU` or `US`.
 */
public data class LibresLocale(
    val language: String,
    val script: String?,
    val region: String?
)

/**
 * Returns the platform's preferred locale, or `null` when it cannot be determined.
 */
public expect fun getPlatformDefaultLibresLocale(): LibresLocale?
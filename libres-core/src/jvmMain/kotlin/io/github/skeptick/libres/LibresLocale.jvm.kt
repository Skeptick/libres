package io.github.skeptick.libres

import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.text.ifEmpty

private val localeCache = ConcurrentHashMap<Locale, LibresLocale>()

private fun getPlatformDefaultLocale(): Locale {
    return Locale.getDefault()
}

public actual fun getPlatformDefaultLibresLocale(): LibresLocale? {
    return getPlatformDefaultLocale().asLibresLocale()
}

public fun Locale.asLibresLocale(): LibresLocale {
    return localeCache.getOrPut(this) {
        LibresLocale(
            language = language,
            script = script.ifEmpty { null },
            region = country.ifEmpty { null },
        )
    }
}
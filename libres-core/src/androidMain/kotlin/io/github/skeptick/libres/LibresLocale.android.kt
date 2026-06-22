package io.github.skeptick.libres

import android.os.Build
import android.os.LocaleList
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

private val localeCache = ConcurrentHashMap<Locale, LibresLocale>()

private fun getPlatformDefaultLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= 24) LocaleList.getDefault()[0] ?: Locale.getDefault() else Locale.getDefault()
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
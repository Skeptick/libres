package io.github.skeptick.libres

import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.preferredLanguages
import platform.Foundation.scriptCode
import kotlin.concurrent.Volatile

@Volatile
private var localeCache: Map<NSLocale, LibresLocale> = emptyMap()

// A changed app language is applied on the next launch, so resolve it only once per process.
private val platformDefaultLibresLocale: LibresLocale by lazy(LazyThreadSafetyMode.PUBLICATION) {
    getPlatformDefaultLocale().asLibresLocale()
}

private fun getPlatformDefaultLocale(): NSLocale {
    val preferredLanguage = NSBundle.mainBundle.preferredLocalizations.firstOrNull() ?: NSLocale.preferredLanguages.firstOrNull()
    return preferredLanguage?.let { NSLocale(it as String) } ?: NSLocale.currentLocale
}

public actual fun getPlatformDefaultLibresLocale(): LibresLocale? {
    return platformDefaultLibresLocale
}

public fun NSLocale.asLibresLocale(): LibresLocale {
    return localeCache[this] ?: LibresLocale(
        language = languageCode,
        script = scriptCode?.ifEmpty { null },
        region = countryCode?.ifEmpty { null },
    ).also { libresLocale ->
        localeCache = localeCache + (this to libresLocale)
    }
}
package io.github.skeptick.libres

private external interface GlobalScope {
    val navigator: NavigatorLike?
}

private external interface NavigatorLike {
    val language: String?
    val languages: Array<String>?
}

@JsName("globalThis")
private external val globalThis: GlobalScope

private external object Intl {
    class Locale(locale: String) {
        val language: String
        val script: String?
        val region: String?
    }
}

// Resolve the preferred language once. Runtime changes are expected to reload the page or restart the application.
private val platformDefaultLibresLocale: LibresLocale? by lazy {
    getPlatformDefaultLocale()?.asLibresLocale()
}

private fun getPlatformDefaultLocale(): Intl.Locale? {
    val navigator = globalThis.navigator ?: return null
    val language = navigator.languages?.firstOrNull() ?: navigator.language ?: return null
    return Intl.Locale(language)
}

public actual fun getPlatformDefaultLibresLocale(): LibresLocale? {
    return platformDefaultLibresLocale
}

private fun Intl.Locale.asLibresLocale(): LibresLocale {
    return LibresLocale(
        language = language,
        script = script,
        region = region
    )
}
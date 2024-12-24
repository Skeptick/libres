package io.github.skeptick.libres.strings

import kotlinx.browser.window

public actual fun getPlatformLanguageCode(): String {
    return if (window.navigator.languages.isNotEmpty()) {
        window.navigator.languages[0].languageCode
    } else {
        window.navigator.language.languageCode
    }
}

private inline val String.languageCode get() = if (length == 2) this else take(2)
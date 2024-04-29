package io.github.skeptick.libres.strings

import kotlinx.browser.window

actual fun getPlatformLanguageCode(): String {
    return if (window.navigator.languages.isNotEmpty()) {
        window.navigator.languages[0].languageCode
    } else {
        window.navigator.language.languageCode
    }
}
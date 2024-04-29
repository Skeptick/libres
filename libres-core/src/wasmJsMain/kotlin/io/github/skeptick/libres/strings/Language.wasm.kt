package io.github.skeptick.libres.strings

import kotlinx.browser.window

actual fun getPlatformLanguageCode(): String {
    return if (window.navigator.languages.length > 0) {
        window.navigator.languages[0].toString().languageCode
    } else {
        window.navigator.language.languageCode
    }
}
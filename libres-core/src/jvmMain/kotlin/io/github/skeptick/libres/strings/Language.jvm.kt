package io.github.skeptick.libres.strings

import java.util.Locale

public actual fun getPlatformLanguageCode(): String {
    return Locale.getDefault().language
}
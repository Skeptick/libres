package io.github.skeptick.libres.strings

internal inline val String.languageCode get() = if (length == 2) this else take(2)

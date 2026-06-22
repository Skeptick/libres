package io.github.skeptick.libres

import kotlin.concurrent.Volatile

public object LibresSettings {

    @Volatile
    private var localeProvider: (() -> LibresLocale)? = null

    public val currentLocale: LibresLocale?
        get() = localeProvider?.invoke() ?: getPlatformDefaultLibresLocale()

    public fun setLocaleProvider(provider: () -> LibresLocale) {
        this.localeProvider = provider
    }

}
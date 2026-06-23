package io.github.skeptick.libres

@InternalLibresApi
public class LibresLocalizations<T>(private val locales: Map<LibresLocale, T>) {

    public constructor(vararg locales: Pair<LibresLocale, T>) : this(mapOf(*locales))

    private val cache: MutableMap<LibresLocale, List<T>> = mutableMapOf()

    public val current: List<T>?
        get() = LibresSettings.currentLocale?.let(::get)

    public operator fun get(target: LibresLocale): List<T> {
        return cache.getOrPut(target) { locales.rankBuckets(target).flatten() }
    }

}

private fun <V> Map<LibresLocale, V>.rankBuckets(target: LibresLocale): List<List<V>> {
    return List(5) { mutableListOf<V>() }.also { buckets ->
        forEach { (locale, value) ->
            if (locale.language == target.language) {
                buckets[locale.matchRank(target)] += value
            }
        }
    }
}

private fun LibresLocale.matchRank(target: LibresLocale): Int =
    when {
        script == target.script && region == target.region -> 0
        script == target.script && script != null -> 1
        region == target.region && region != null && script == null -> 2
        script == null && region == null -> 3
        else -> 4
    }
package io.github.skeptick.libres.plugin.common.extensions

internal fun <T> List<T>.findDuplicates(): Set<T> {
    val exist = mutableSetOf<T>()
    val duplicates = mutableSetOf<T>()
    for (item in this) if (item in exist) duplicates += item else exist += item
    return duplicates
}
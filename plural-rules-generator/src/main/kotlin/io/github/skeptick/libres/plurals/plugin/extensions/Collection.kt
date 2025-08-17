package io.github.skeptick.libres.plurals.plugin.extensions

internal fun <T> Iterable<T>.splitBy(condition: (T) -> Boolean): List<List<T>> =
    mutableListOf(mutableListOf<T>()).also { result ->
        for (item in this) {
            if (condition(item)) result.add(mutableListOf()) else result.last().add(item)
        }
    }
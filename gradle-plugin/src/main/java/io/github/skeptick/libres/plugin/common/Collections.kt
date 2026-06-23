package io.github.skeptick.libres.plugin.common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Suppress("KotlinConstantConditions")
@OptIn(ExperimentalContracts::class)
internal inline fun <reified K, reified V> Map<*, *>.allAre(): Boolean {
    contract { returns(true) implies (this@allAre is Map<K, V>) }
    return all { it.key is K && it.value is V }
}
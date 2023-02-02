package io.github.skeptick.libres.plugin.strings.models

import io.github.skeptick.libres.plugin.strings.extractInterpolationParametersNames

data class PluralsResource(
    override val name: String,
    val items: List<Item>
) : TextResource {

    override val parameters: Set<String> by lazy(LazyThreadSafetyMode.NONE) {
        items.flatMap { it.value.extractInterpolationParametersNames() }.toSet()
    }

    data class Item(
        val quantity: Quantity,
        val value: String
    )

    enum class Quantity(val serialName: String) {
        Zero("zero"),
        One("one"),
        Two("two"),
        Few("few"),
        Many("many"),
        Other("other")
    }

    companion object {
        val quantityBySerialName = Quantity.values().associateBy(Quantity::serialName)
    }

}
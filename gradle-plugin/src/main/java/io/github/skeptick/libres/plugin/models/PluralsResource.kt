package io.github.skeptick.libres.plugin.models

import io.github.skeptick.libres.plugin.common.extractInterpolationParameters

internal class PluralsResource(
    override val name: String,
    baseItems: List<Item>,
    localizedItems: Map<LocaleTag, List<Item>>
) : TextResource {

    override val parameters: Set<String> = baseItems.flatMap { it.value.extractInterpolationParameters() }.toSet()

    val localizedItems: Map<LocaleTag, List<Item>> = localizedItems.mapValues { (locale, items) ->
        items.map { item ->
            item.copy(
                value = replaceParametersToJavaSpecifiers(item.value, locale)
            )
        }
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
        val quantityBySerialName = Quantity.entries.associateBy(Quantity::serialName)
    }

}
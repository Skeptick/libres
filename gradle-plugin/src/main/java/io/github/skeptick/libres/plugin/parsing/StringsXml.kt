package io.github.skeptick.libres.plugin.parsing

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlPolyChildren
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("resources")
internal data class StringsXml(
    val items: List<StringsXmlItem> = emptyList(),
)

@Serializable
internal sealed interface StringsXmlItem {

    val name: String

    @Serializable
    @SerialName("string")
    @XmlSerialName("string")
    data class StringItem(
        @XmlElement(false) override val name: String,
        @XmlValue val value: String = "",
    ) : StringsXmlItem

    @Serializable
    @SerialName("plurals")
    @XmlSerialName("plurals")
    data class PluralsItem(
        @XmlElement(false) override val name: String,
        @XmlPolyChildren(["item"]) val items: List<Item> = emptyList(),
    ) : StringsXmlItem {

        @Serializable
        @XmlSerialName("item")
        data class Item(
            @XmlElement(false) val quantity: String,
            @XmlValue val value: String = "",
        )

    }

}
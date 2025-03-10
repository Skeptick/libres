package io.github.skeptick.libres.plugin.images.models

import com.fasterxml.jackson.annotation.JsonProperty

internal data class ImageSetContents(
    val images: List<Image>,
    val info: Info = Info(),
    val properties: Properties
) {

    @Suppress("EnumEntryName")
    enum class ImageScale {
        @JsonProperty("1x") x1,
        @JsonProperty("2x") x2,
        @JsonProperty("3x") x3
    }

    enum class VectorRenderingType {
        @JsonProperty("original") Original,
        @JsonProperty("template") Template
    }

    data class Image(
        val filename: String,
        val scale: ImageScale? = null,
        val idiom: String = "universal",
        val appearances: List<Appearance>? = null,
    )

    sealed interface Appearance {
        val appearance: String
        val value: String

        sealed interface Luminosity : Appearance {
            override val appearance: String
                get() = "luminosity"

            object Dark : Luminosity {
                override val value: String = "dark"
            }
        }
    }

    data class Info(
        val author: String = "xcode",
        val version: Int = 1
    )

    data class Properties(
        @JsonProperty("preserves-vector-representation") val preserveVectorRepresentation: Boolean?,
        @JsonProperty("template-rendering-intent") val templateRenderingIntent: VectorRenderingType
    )
}

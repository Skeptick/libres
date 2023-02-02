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
        val idiom: String = "universal"
    )

    data class Info(
        val author: String = "xcode",
        val version: Int = 1
    )

    data class Properties(
        @JsonProperty("preserves-vector-representation") val preserveVectorRepresentation: Boolean?,
        @JsonProperty("template-rendering-intent") val templateRenderingIntent: VectorRenderingType
    )

}

internal fun ImageProps.toImageSetContents() =
    ImageSetContents(
        images = when (targetSize) {
            null -> listOf(
                ImageSetContents.Image(filename = "$name.$extension")
            )
            else -> ImageSetContents.ImageScale.values().map {
                ImageSetContents.Image(filename = "${this.name}_${it.name}.$extension", scale = it)
            }
        },
        properties = ImageSetContents.Properties(
            preserveVectorRepresentation = if (isVector) true else null,
            templateRenderingIntent = when (isNonRepaintable) {
                true -> ImageSetContents.VectorRenderingType.Original
                false -> ImageSetContents.VectorRenderingType.Template
            }
        )
    )
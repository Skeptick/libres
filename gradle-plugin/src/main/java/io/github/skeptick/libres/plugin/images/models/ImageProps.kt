package io.github.skeptick.libres.plugin.images.models

import java.io.File

internal class ImageProps(val file: File) {

    val name: String
    val extension: String
    val targetSize: Int?
    val isNonRepaintable: Boolean

    init {
        val nameWithoutExtension = file.nameWithoutExtension
        val parameters = ParametersRegex.findAll(nameWithoutExtension).toList()
        this.name = nameWithoutExtension.substringBefore("_(").lowercase()
        this.extension = file.extension.lowercase()
        this.targetSize = if (extension != "svg") parameters.firstNotNullOfOrNull { it.groupValues[1].toIntOrNull() } else null
        this.isNonRepaintable = parameters.any { it.groupValues[1].startsWith("orig") }
    }

    companion object {
        private val ParametersRegex = Regex("_\\((.*?)\\)")
    }

}

internal val ImageProps.isVector: Boolean get() = extension == "svg"
package io.github.skeptick.libres.plugin.images.processing

import com.android.ide.common.vectordrawable.Svg2Vector
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.skeptick.libres.plugin.KotlinPlatform
import io.github.skeptick.libres.plugin.images.models.ImageProps
import io.github.skeptick.libres.plugin.images.models.ImageScale
import io.github.skeptick.libres.plugin.images.models.ImageSet
import io.github.skeptick.libres.plugin.images.models.ImageSetContents
import io.github.skeptick.libres.plugin.images.models.ImageSetContents.Appearance
import io.github.skeptick.libres.plugin.images.models.androidName
import io.github.skeptick.libres.plugin.images.models.isVector
import io.github.skeptick.libres.plugin.images.models.times
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.global.opencv_imgproc
import org.bytedeco.opencv.opencv_core.Mat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

private val jsonWriter = jacksonObjectMapper().let {
    it.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    it.writer(DefaultPrettyPrinter().apply {
        indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
    })
}

internal fun ImageProps.saveImage(directories: Map<KotlinPlatform, File>) {
    if (targetSize == null) {
        saveOriginal(directories)
    } else {
        val src = opencv_imgcodecs.imread(file.absolutePath, opencv_imgcodecs.IMREAD_UNCHANGED)
        for (scale in ImageScale.values()) {
            if (!directories.any { it.key in scale.supportedPlatforms }) continue
            val newSideSize = targetSize * scale
            when {
                src.size() < newSideSize -> error("Image '$name' has too low resolution")
                src.size() > newSideSize -> resizeAndSave(src, scale, targetSize, directories)
                else -> saveOriginal(scale, directories)
            }
        }
    }
}

internal fun ImageProps.removeImage(directories: Map<KotlinPlatform, File>) {
    for ((platform, directory) in directories) {
        when {
            platform == KotlinPlatform.Common -> continue
            platform == KotlinPlatform.Apple -> File(directory, "$name.imageset").deleteRecursively()
            platform == KotlinPlatform.Android && targetSize != null -> ImageScale.values().forEach { scale ->
                targetFile(directory, platform, scale).delete()
            }
            else -> targetFile(directory, platform).delete()
        }
    }
}

private fun ImageProps.saveOriginal(directories: Map<KotlinPlatform, File>) {
    for ((platform, directory) in directories) {
        when {
            platform == KotlinPlatform.Common -> continue
            platform == KotlinPlatform.Android && isVector -> {
                val targetFile = targetFile(directory, platform).apply {
                    parentFile.mkdirs()
                }

                val output = FileOutputStream(targetFile)
                parseSvgToXml(file, output)
            }
            else -> {
                val targetFile = targetFile(directory, platform).apply {
                    parentFile.mkdirs()
                }

                file.copyTo(targetFile, overwrite = true)
            }
        }
    }
}

private fun ImageProps.saveOriginal(scale: ImageScale, directories: Map<KotlinPlatform, File>) {
    for ((platform, directory) in directories) {
        if (platform in scale.supportedPlatforms) {
            val targetFile = targetFile(directory, platform, scale).apply {
                parentFile.mkdirs()
            }

            file.copyTo(targetFile, overwrite = true)
        }
    }
}

private fun ImageProps.resizeAndSave(src: Mat, scale: ImageScale, size: Int, directories: Map<KotlinPlatform, File>) {
    val destinationImage = Mat()
    val newSize = calculateSize(src.size(), size * scale)
    opencv_imgproc.resize(src, destinationImage, newSize)

    for ((platform, directory) in directories) {
        if (platform in scale.supportedPlatforms) {
            val targetFile = targetFile(directory, platform, scale).apply {
                parentFile.mkdirs()
            }

            opencv_imgcodecs.imwrite(targetFile.absolutePath, destinationImage)
        }
    }
}

internal fun ImageSet.saveImageSet(directories: Map<KotlinPlatform, File>) {
    for ((platform, directory) in directories) {
        when (platform) {
            KotlinPlatform.Apple -> saveImageSetContents(directory)
            KotlinPlatform.Android, KotlinPlatform.Common, KotlinPlatform.Jvm, KotlinPlatform.Js -> Unit
        }
    }
}

private fun ImageSet.saveImageSetContents(directory: File) {
    val text = jsonWriter.writeValueAsString(toImageSetContents(directory))
    val file = File(directory, "$name.imageset/Contents.json")
    file.parentFile.mkdirs()
    file.writeText(text)
}

private fun ImageSet.toImageSetContents(directory: File) =
    ImageSetContents(
        images = images.map { image ->
            val appearances = image.appearances()
            when (image.targetSize) {
                null -> listOf(
                    ImageSetContents.Image(
                        filename = image.targetFile(directory, KotlinPlatform.Apple).name,
                        appearances = appearances,
                    )
                )

                else -> ImageSetContents.ImageScale.values().map { scale ->
                    ImageSetContents.Image(
                        filename = image.targetFile(directory, KotlinPlatform.Apple, scale.toImageScale()).name,
                        scale = scale,
                        appearances = appearances,
                    )
                }
            }
        }.flatten(),
        properties = ImageSetContents.Properties(
            preserveVectorRepresentation = if (isVector) true else null,
            templateRenderingIntent = when (isTintable) {
                true -> ImageSetContents.VectorRenderingType.Template
                false -> ImageSetContents.VectorRenderingType.Original
            }
        )
    )

private fun ImageSetContents.ImageScale.toImageScale() =
    when (this) {
        ImageSetContents.ImageScale.x1 -> ImageScale.x1
        ImageSetContents.ImageScale.x2 -> ImageScale.x2
        ImageSetContents.ImageScale.x3 -> ImageScale.x3
    }

private fun ImageProps.appearances(): List<Appearance>? =
    listOfNotNull(
        if (isNightMode) Appearance.Luminosity.Dark else null,
    ).takeIf(List<*>::isNotEmpty)

private fun ImageProps.targetFile(
    directory: File,
    platform: KotlinPlatform,
    scale: ImageScale? = null,
) = File(directory, targetFilePath(platform, scale))

private fun ImageProps.targetFilePath(platform: KotlinPlatform, scale: ImageScale? = null): String =
    when (platform) {
        KotlinPlatform.Android -> androidTargetFilePath(scale)
        KotlinPlatform.Apple -> appleTargetFilePath(scale)
        KotlinPlatform.Jvm, KotlinPlatform.Js -> "/$name.$extension"
        KotlinPlatform.Common -> error("Can not generate targetFilePath for platform '$platform'.")
    }

private fun ImageProps.androidTargetFilePath(scale: ImageScale?): String {
    val folderName = listOfNotNull(
        "drawable",
        if (isNightMode) "night" else null,
        scale?.androidName ?: "nodpi",
    ).joinToString("-")
    val fileName = name
    val extension = if (isVector) "xml" else extension

    return "/$folderName/$fileName.$extension"
}

private fun ImageProps.appleTargetFilePath(scale: ImageScale?): String {
    val folderName = "$name.imageset"
    val fileName = listOfNotNull(
        name,
        scale?.name,
        if (isNightMode) "night" else null,
    ).joinToString("_")

    return "/$folderName/$fileName.$extension"
}

/**
 * Workaround for Svg2Vector::parseSvgToXml
 */
private fun parseSvgToXml(inputSvg: File, output: OutputStream) {
    val method = Svg2Vector::class.java.declaredMethods.first { it.name == "parseSvgToXml" }
    when (method.parameterTypes[0]) {
        File::class.java -> method(null, inputSvg, output)
        else -> method(null, inputSvg.toPath(), output)
    }
}

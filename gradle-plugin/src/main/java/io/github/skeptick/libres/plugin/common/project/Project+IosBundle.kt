package io.github.skeptick.libres.plugin.common.project

import org.gradle.api.Project
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.pathString

internal const val iosImagesDirectory = "generated/libres/ios/resources/images"

// {
//     'LibresCommon' => ['common/build/generated/libres/ios/resources/LibresCommon.xcassets'],
//     'LibresFeature' => ['feature/build/generated/libres/ios/resources/LibresFeature.xcassets'],
// }
internal fun List<Project>.appleImageBundles(rootProject: Project): String =
    joinToString(separator = ",\n", prefix = "{\n", postfix = "\n    }") {
        val bundleName = it.appleBundleName
        "        '${bundleName}' => ['${rootProject.buildDir.relativeTo(rootProject.projectDir).path}/$iosImagesDirectory/$bundleName.xcassets']"
    }

internal fun List<Project>.createSymLinks(rootProject: Project) {
    val assetsDir = Path(rootProject.buildDir.absolutePath, iosImagesDirectory)
    Files.createDirectories(assetsDir)

    forEach { project ->
        val bundleName = project.appleBundleName
        val target = Path(project.buildDir.absolutePath, iosImagesDirectory, "$bundleName.xcassets")
        val link = Path(assetsDir.pathString, "$bundleName.xcassets")

        if (!Files.exists(link)) {
            Files.createDirectories(target)
            Files.createSymbolicLink(link, target)
        }
    }
}

internal val Project.appleBundleName: String
    get() = "Libres" + path.split(":", "-", "_").joinToString("") { partOfName ->
        partOfName.replaceFirstChar { it.titlecase() }
    }
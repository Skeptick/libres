package io.github.skeptick.libres.plugin.common.project

import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

internal const val appleImagesDirectory = "generated/libres/apple/resources/images"

// {
//     'LibresCommon' => ['common/build/generated/libres/apple/resources/LibresCommon.xcassets'],
//     'LibresFeature' => ['feature/build/generated/libres/apple/resources/LibresFeature.xcassets'],
// }
internal fun List<Project>.appleImageBundles(rootProject: Project): String =
    joinToString(separator = ",\n", prefix = "{\n", postfix = "\n    }") {
        val bundleName = it.appleBundleName
        "        '${bundleName}' => ['${rootProject.buildDir.relativeTo(rootProject.projectDir).path}/$appleImagesDirectory/$bundleName.xcassets']"
    }

internal fun createAssetsSymLinks(rootProject: Project, exportedProjects: List<Project>) {
    val rootImagesDir = Path(rootProject.buildDir.absolutePath, appleImagesDirectory)
    val rootBundleName = rootProject.appleBundleName
    val rootAssetsDir = Path(rootProject.buildDir.absolutePath, appleImagesDirectory, "$rootBundleName.xcassets")
    Files.createDirectories(rootAssetsDir)
    exportedProjects.forEach { rootImagesDir.addSymlinkToAssets(it) }
}

internal val Project.appleBundleName: String
    get() = "Libres" + path.split(":", "-", "_").joinToString("") { partOfName ->
        partOfName.replaceFirstChar { it.titlecase() }
    }

private fun Path.addSymlinkToAssets(project: Project) {
    val bundleName = project.appleBundleName
    val target = Path(project.buildDir.absolutePath, appleImagesDirectory, "$bundleName.xcassets")
    val link = Path(pathString, "$bundleName.xcassets")
    Files.createDirectories(target)
    if (!Files.exists(link)) Files.createSymbolicLink(link, target)
}
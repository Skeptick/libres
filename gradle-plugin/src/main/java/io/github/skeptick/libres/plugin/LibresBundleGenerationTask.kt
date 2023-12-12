package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.common.extensions.deleteFilesInDirectory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

@CacheableTask
abstract class LibresBundleGenerationTask : DefaultTask() {

    @get:Input
    internal abstract var bundleName: String

    @get:Input
    internal var minimumDeploymentTarget: String = "9.0"

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    @get:Optional
    internal abstract var directoryToCompile: File?

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    internal abstract var filesToCopy: List<File>

    @get:OutputDirectory
    @get:Optional
    internal abstract var outputDirectory: File?

    @TaskAction
    fun apply() {
        val directoryToCompile = directoryToCompile ?: return
        val outputDirectory = outputDirectory ?: return

        val bundleDirectory = File(outputDirectory, "$bundleName.bundle")
        bundleDirectory.mkdirs()
        bundleDirectory.deleteFilesInDirectory()

        ProcessBuilder(
            "xcrun", "actool", directoryToCompile.absolutePath,
            "--compile", bundleDirectory.absolutePath,
            "--output-format", "human-readable-text",
            "--notices", "--warnings",
            "--platform", "iphoneos",
            "--minimum-deployment-target", minimumDeploymentTarget,
            "--target-device", "iphone",
            "--compress-pngs"
        ).start().let { process ->
            if (process.waitFor() != 0) {
                val message1 = process.inputStream.bufferedReader().readText()
                val message2 = process.errorStream.bufferedReader().readText()
                val errorMessage = listOf(message1, message2).joinToString(".")
                logger.error("Error when compiling iOS resources: $errorMessage")
            }
        }

        val plist = File(bundleDirectory, "Info.plist")
        plist.writeText(InfoPlist(bundleName))
    }

    private companion object {

        @Suppress("FunctionName")
        fun InfoPlist(bundleName: String) = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
            <plist version="1.0">
            <dict>
              <key>CFBundleIdentifier</key>
              <string>$bundleName</string>
              <key>CFBundlePackageType</key>
              <string>BNDL</string>
            </dict>
            </plist>
        """.trimIndent()

    }

}
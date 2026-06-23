package io.github.skeptick.libres.plugin.common

import com.squareup.kotlinpoet.FileSpec
import org.gradle.api.file.Directory
import java.io.File

internal fun FileSpec.saveTo(directory: Directory) {
    directory.asFile.let { file ->
        if (!file.exists()) file.mkdirs()
        File(file, "$name.${if (isScript) "kts" else "kt"}").writer().use(::writeTo)
    }
}
package io.github.skeptick.libres.plurals.plugin

import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class PluralRulesGeneratorPluginExtension @Inject constructor(objects: ObjectFactory) {

    internal val rulesFileProp = objects.fileProperty()

    internal val packageNameProp = objects.property(String::class.java)

    internal val sourceDirectoryProp = objects.directoryProperty()

    internal val testsDirectoryProp = objects.directoryProperty()

    var rulesFile: RegularFile?
        get() = rulesFileProp.orNull
        set(value) = rulesFileProp.set(value)

    var packageName: String?
        get() = packageNameProp.orNull
        set(value) = packageNameProp.set(value)

    var sourceDirectory: Directory?
        get() = sourceDirectoryProp.orNull
        set(value) = sourceDirectoryProp.set(value)

    var testsDirectory: Directory?
        get() = testsDirectoryProp.orNull
        set(value) = testsDirectoryProp.set(value)

    internal fun finalizeValuesOnRead() {
        rulesFileProp.finalizeValueOnRead()
        packageNameProp.finalizeValueOnRead()
        sourceDirectoryProp.finalizeValueOnRead()
        testsDirectoryProp.finalizeValueOnRead()
    }

}
package io.github.skeptick.libres.plurals.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluralRulesGeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extensions = target.extensions.create("pluralRulesGenerator", PluralRulesGeneratorPluginExtension::class.java)
        target.tasks.register("generatePluralRules", GeneratePluralRulesTask::class.java) { task ->
            task.rulesFile.set(extensions.rulesFileProp)
            task.packageName.set(extensions.packageNameProp)
            task.sourceDirectory.set(extensions.sourceDirectoryProp)
            task.testsDirectory.set(extensions.testsDirectoryProp)
        }
        extensions.finalizeValuesOnRead()
    }

}
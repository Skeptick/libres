package io.github.skeptick.libres.plurals.plugin

import io.github.skeptick.libres.plurals.plugin.generator.Dnf
import io.github.skeptick.libres.plurals.plugin.generator.IntPluralRulesFile
import io.github.skeptick.libres.plurals.plugin.generator.IntPluralRulesTestsFile
import io.github.skeptick.libres.plurals.plugin.generator.PluralForm
import io.github.skeptick.libres.plurals.plugin.generator.Plurals
import io.github.skeptick.libres.plurals.plugin.generator.parseDnf
import io.github.skeptick.libres.plurals.plugin.generator.parseIntSamples
import io.github.skeptick.libres.plurals.plugin.generator.parseJson
import io.github.skeptick.libres.plurals.plugin.generator.simplifyForIntegers
import io.github.skeptick.libres.plurals.plugin.generator.tokenize
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
abstract class GeneratePluralRulesTask : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    internal abstract val rulesFile: RegularFileProperty

    @get:Input
    internal abstract val packageName: Property<String>

    @get:OutputDirectory
    internal abstract val sourceDirectory: DirectoryProperty

    @get:OutputDirectory
    internal abstract val testsDirectory: DirectoryProperty

    @TaskAction
    fun apply() {
        val rulesFile = rulesFile.orNull?.asFile?.takeIf(File::exists) ?: error("Plural rules file is not specified")
        val packageName = packageName.orNull ?: error("Package name is not specified")
        val sourceDirectory = sourceDirectory.orNull ?: error("Source directory is not specified")
        val testsDirectory = testsDirectory.orNull ?: error("Tests directory is not specified")

        val plurals = parseJson(rulesFile.readText())
        val intRules = getIntRules(plurals)
        val intRulesTests = getIntRulesTests(plurals)
        IntPluralRulesFile(packageName, intRules).writeTo(sourceDirectory.asFile)
        IntPluralRulesTestsFile(packageName, intRulesTests).writeTo(testsDirectory.asFile)
    }

    private fun getIntRules(plurals: Plurals): Map<String, Map<PluralForm, Dnf>> {
        val rulesTokens = tokenize(plurals)
        val rulesDnf = rulesTokens.mapValues { (_, tokensByForm) ->
            tokensByForm.mapValues { (_, tokens) ->
                if (tokens.isEmpty()) emptyList() else parseDnf(tokens).let(Dnf::simplifyForIntegers)
            }
        }
        return rulesDnf.filterKeys { it.all(Char::isLetter) }
    }

    private fun getIntRulesTests(plurals: Plurals): Map<String, Map<PluralForm, List<IntRange>>> {
        val samples = parseIntSamples(plurals)
        return samples.filterKeys { it.all(Char::isLetter) }
    }

}
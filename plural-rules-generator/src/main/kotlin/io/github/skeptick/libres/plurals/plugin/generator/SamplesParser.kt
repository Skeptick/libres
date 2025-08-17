package io.github.skeptick.libres.plurals.plugin.generator

private val IntLiteralRegex = Regex("^\\d+$")

private val IntRangeRegex = Regex("^\\d+~\\d+$")

internal fun parseIntSamples(plurals: Plurals): Map<String, Map<PluralForm, List<IntRange>>> {
    return plurals.supplemental.cardinal.mapValues { (_, rules) ->
        buildMap {
            if (rules.zero != null) put(PluralForm.Zero, parseRuleIntSamples(rules.zero))
            if (rules.one != null) put(PluralForm.One, parseRuleIntSamples(rules.one))
            if (rules.two != null) put(PluralForm.Two, parseRuleIntSamples(rules.two))
            if (rules.few != null) put(PluralForm.Few, parseRuleIntSamples(rules.few))
            if (rules.many != null) put(PluralForm.Many, parseRuleIntSamples(rules.many))
            if (rules.other != null) put(PluralForm.Other, parseRuleIntSamples(rules.other))
        }
    }
}

private fun parseRuleIntSamples(rule: String): List<IntRange> {
    val numbers = rule.substringAfter("@integer", missingDelimiterValue = "").substringBefore("@decimal")
    return numbers.split(",").map(String::trim).mapNotNull {
        when {
            IntLiteralRegex.matches(it) -> it.toInt()..it.toInt()
            IntRangeRegex.matches(it) -> it.toIntRange()
            else -> return@mapNotNull null
        }
    }
}

private fun String.toIntRange(): IntRange =
    substringBefore("~").toInt()..substringAfter("~").toInt()
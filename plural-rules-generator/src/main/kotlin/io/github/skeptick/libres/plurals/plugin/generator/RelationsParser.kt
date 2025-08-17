package io.github.skeptick.libres.plurals.plugin.generator

import io.github.skeptick.libres.plurals.plugin.extensions.splitBy

internal typealias Dnf = List<Conjunction>

internal typealias Conjunction = List<Relation>

internal data class Relation(
    val varName: Char,
    val mod: Int?,
    val negated: Boolean,
    val ranges: List<IntRange>
)

internal fun parseDnf(tokens: List<Token>): Dnf {
    val orGroups = tokens.splitBy { it == Token.Or }
    return orGroups.map { andGroup ->
        val andGroups = andGroup.splitBy { it == Token.And }
        andGroups.map(::parseRelation)
    }
}

private fun parseRelation(tokens: List<Token>): Relation {
    requireRelation(tokens)

    val condition = tokens.first { it is ConditionToken }
    val (left, right) = tokens.splitBy { it == condition }

    return Relation(
        varName = (left.first() as Token.Identifier).char,
        mod = left.findMod(),
        negated = condition == Token.Neq,
        ranges = (right.single() as IntToken).toRanges()
    )
}

// Identifier [ Mod IntLiteral ] (ConditionToken) (IntToken)
private fun requireRelation(tokens: List<Token>) {
    if (tokens.any { it == Token.Mod }) {
        require(tokens.size == 5) { "Invalid relation size: $tokens" }
        tokens.requireToken<Token.Identifier>(0)
        tokens.requireToken<Token.Mod>(1)
        tokens.requireToken<Token.IntLiteral>(2)
        tokens.requireToken<ConditionToken>(3)
        tokens.requireToken<IntToken>(4)
    } else {
        require(tokens.size == 3) { "Invalid relation size: $tokens" }
        tokens.requireToken<Token.Identifier>(0)
        tokens.requireToken<ConditionToken>(1)
        tokens.requireToken<IntToken>(2)
    }
}

private inline fun <reified T> List<Token>.requireToken(position: Int) {
    require(this[position] is T) {
        "Invalid token at position $position. Expected: ${T::class.simpleName}. Tokens: $this"
    }
}

private fun List<Token>.findMod(): Int? =
    if (size == 3 && this[1] == Token.Mod) (this[2] as Token.IntLiteral).value else null

private fun IntToken.toRanges(): List<IntRange> =
    when (this) {
        is Token.IntLiteral -> listOf((value..value))
        is Token.IntRange -> listOf(value)
        is Token.IntTokenList -> tokens.flatMap { it.toRanges() }
    }
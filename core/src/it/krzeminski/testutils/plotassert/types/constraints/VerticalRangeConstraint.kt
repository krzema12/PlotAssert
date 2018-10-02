package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.computeValueBounds
import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn

data class VerticalRangeConstraint(
        val minY: Float,
        val maxY: Float) : YValueConstraint()

object VerticalRangeConstraintBuilder : ConstraintBuilder()
{
    override fun columnMatchesThisConstraintType(column: VisualisationColumn): Boolean {
        val onlyLegalCharacters = column.characters.groupBy { it }.keys == setOf(' ', 'I')
        val noGapsBetweenLetters =
            column.characters
                    .mapIndexed { index, character -> Pair(index, character) }
                    .filter { it.second == 'I' }
                    .map { it.first }
                    .zipWithNext { a, b -> b - a }
                    .all { it == 1 }

        return onlyLegalCharacters && noGapsBetweenLetters
    }

    override fun buildConstraintFromColumn(
            column: VisualisationColumn, yAxisMarkers: List<AxisMarker>): YValueConstraint
    {
        val indexOfFirstCharacter = column.characters.indexOfFirst { it == 'I' }
        val indexOfLastCharacter = column.characters.indexOfLast { it == 'I' }
        val firstCharacterValueBounds = computeValueBounds(yAxisMarkers, indexOfFirstCharacter)
        val lastCharacterValueBounds = computeValueBounds(yAxisMarkers, indexOfLastCharacter)

        return VerticalRangeConstraint(
                minY = lastCharacterValueBounds.lowerBound, maxY = firstCharacterValueBounds.upperBound)
    }
}
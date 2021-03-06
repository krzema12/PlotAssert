package it.krzeminski.visassert

import it.krzeminski.visassert.types.AxisMarker
import it.krzeminski.visassert.types.VisualisationColumn
import it.krzeminski.visassert.types.constraints.Constraint
import it.krzeminski.visassert.types.constraints.ConstraintBuilder
import it.krzeminski.visassert.types.constraints.getAvailableConstraintBuilders

/**
 * Returns null when the given column doesn't specify any constraint.
 */
fun mapVisualisationColumnToConstraint(
    xValues: List<Float>,
    column: VisualisationColumn,
    yAxisMarkers: List<AxisMarker>,
    getAvailableConstraintBuilders:
        () -> List<ConstraintBuilder> = ::getAvailableConstraintBuilders
): Constraint? {
    if (onlySpaces(column)) {
        return null
    }
    val buildersAcceptingThisColumn = getAvailableConstraintBuilders().filter { builder ->
        builder.columnMatchesThisConstraintType(column)
    }
    require(buildersAcceptingThisColumn.size == 1) {
        if (buildersAcceptingThisColumn.size > 1) {
            "Ambiguous constraint; more than 1 constraint type matches: $buildersAcceptingThisColumn"
        } else {
            "No constraints match this visualisation column: ${column.characters}"
        }
    }
    val builderToUse = buildersAcceptingThisColumn.first()
    return builderToUse.buildConstraintFromColumn(xValues, column, yAxisMarkers)
}

private fun onlySpaces(column: VisualisationColumn) =
    column.characters.groupBy { it }.keys == setOf(' ')

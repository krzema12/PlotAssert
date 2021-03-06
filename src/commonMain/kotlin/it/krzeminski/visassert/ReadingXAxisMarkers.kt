package it.krzeminski.visassert

import it.krzeminski.visassert.types.AxisMarker
import it.krzeminski.visassert.types.RawVisualisation
import it.krzeminski.visassert.types.RawXAxis

/**
 * Returns X axis markers in the order they were given, from left to right.
 */
fun readXAxisMarkers(rawVisualisation: RawVisualisation): List<AxisMarker> {
    validate(rawVisualisation)

    val markerIndices = rawVisualisation.xAxis.markers
        .mapIndexed { index, character -> Pair(index, character) }
        .filter { pair -> pair.second == markerCharacter }
        .map { pair -> pair.first }
    return rawVisualisation.xAxis.values
        .zip(markerIndices)
        .map { pair ->
            AxisMarker(
                value = pair.first,
                characterIndex = pair.second
            )
        }
}

private const val markerCharacter = '|'
private const val noMarkerCharacter = ' '
private val allowedCharacters = setOf(markerCharacter, noMarkerCharacter)

private fun validate(rawVisualisation: RawVisualisation) {
    validateIfLegalCharactersUsed(rawVisualisation.xAxis.markers)
    validateIfNumberOfMarkersMatchesNumberOfValues(rawVisualisation.xAxis)
    validateIfAtLeastTwoMarkers(rawVisualisation.xAxis.values)
    validateIfFirstAndLastColumnHaveMarkers(rawVisualisation.xAxis.markers)
    validateIfMarkerValuesIncreaseMonotonically(rawVisualisation.xAxis.values)
}

private fun validateIfLegalCharactersUsed(markers: String) =
    markers.forEach { character ->
        require(character in allowedCharacters) {
            "Illegal characters given in X axis markers string, " +
                "only (${allowedCharacters.joinToString(", ") { "'$it'" }}) are allowed!"
        }
    }

private fun validateIfNumberOfMarkersMatchesNumberOfValues(xAxis: RawXAxis) {
    val numberOfValues = xAxis.values.count()
    val numberOfMarkers = xAxis.markers.filter { it == markerCharacter }.count()
    require(numberOfValues == numberOfMarkers) {
        "X axis definition mismatch: found $numberOfMarkers marker(s) but $numberOfValues value(s)!"
    }
}

private fun validateIfAtLeastTwoMarkers(values: List<Float>) {
    val numberOfMarkers = values.count()
    require(values.count() >= 2) { "$numberOfMarkers X axis marker(s) found, and there should be at least two!" }
}

private fun validateIfFirstAndLastColumnHaveMarkers(markers: String) {
    require(markers.first() == markerCharacter && markers.last() == markerCharacter) {
        "X axis should have markers for first and last column!"
    }
}

private fun validateIfMarkerValuesIncreaseMonotonically(visualisationRows: List<Float>) {
    visualisationRows
        .zipWithNext { a, b -> Pair(a, b) }
        .forEach { pair ->
            require(pair.second - pair.first > 0.0f) {
                "Given X axis markers should have ascending values " +
                    "(found: ${pair.first}, ${pair.second})!"
            }
        }
}

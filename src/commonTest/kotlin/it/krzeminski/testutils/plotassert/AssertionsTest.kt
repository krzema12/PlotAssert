package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.exceptions.FailedConstraintException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/* ktlint-disable no-multi-spaces paren-spacing */

class AssertionsTest {
    @Test
    fun `assertFunctionConformsTo() when assertions are fulfilled`() {
        assertFunctionConformsTo(
                functionUnderTest = { 1.0f },
                visualisation = {
                    row(1.0f,   "XXXXX")
                    row(0.0f,   "     ")
                    xAxis {
                        markers("|   |")
                        values(1.0f, 2.0f)
                    }
                }
        )
    }

    @Test
    fun `assertFunctionConformsTo() when one assertion fails`() {
        assertFailsWith<FailedConstraintException> {
            assertFunctionConformsTo(
                    functionUnderTest = { 1.0f },
                    visualisation = {
                        row(1.0f,   "X XXX")
                        row(0.0f,   " X   ")
                        xAxis {
                            markers("|   |")
                            values(1.0f, 2.0f)
                        }
                    }
            )
        }.let { e ->
            assertTrue(e.message in setOf("For x=1.25: 1.0 is not equal to 0.0!", "For x=1.25: 1 is not equal to 0!"))
        }
    }
}

/* ktlint-disable no-multi-spaces paren-spacing */

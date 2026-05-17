package com.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpecScenariosTest {

    private static final int[][] SPEC_PUZZLE = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    private static final int[][] SPEC_SOLUTION = {
        {5, 3, 4, 6, 7, 8, 9, 1, 2},
        {6, 7, 2, 1, 9, 5, 3, 4, 8},
        {1, 9, 8, 3, 4, 2, 5, 6, 7},
        {8, 5, 9, 7, 6, 1, 4, 2, 3},
        {4, 2, 6, 8, 5, 3, 7, 9, 1},
        {7, 1, 3, 9, 2, 4, 8, 5, 6},
        {9, 6, 1, 5, 3, 7, 2, 8, 4},
        {2, 8, 7, 4, 1, 9, 6, 3, 5},
        {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    private SudokuGrid grid;
    private SudokuValidator validator;
    private CommandParser parser;

    @BeforeEach
    void setUp() {
        grid      = new SudokuGrid(copy(SPEC_PUZZLE), SPEC_SOLUTION);
        validator = new SudokuValidator();
        parser    = new CommandParser();
    }

    // -------------------------------------------------------------------
    // Scenario 1 – Success path
    // "A3 4" accepted, check passes, hint reveals correct value, completion detected.
    // -------------------------------------------------------------------

    @Test
    void scenario1_parseA3_4returnsPlaceCommand() {
        Command cmd = parser.parse("A3 4");
        assertEquals(Command.Type.PLACE, cmd.getType());
        assertEquals(0, cmd.getRow());
        assertEquals(2, cmd.getCol());
        assertEquals(4, cmd.getValue());
    }

    @Test
    void scenario1_placeA3_4isAccepted() {
        assertTrue(grid.placeValue(0, 2, 4));
        assertEquals(4, grid.getValue(0, 2));
    }

    @Test
    void scenario1_checkAfterValidPlaceReturnsNoViolations() {
        grid.placeValue(0, 2, 4);
        assertTrue(validator.validate(grid.snapshot()).isEmpty());
    }

    @Test
    void scenario1_hintReturnsCorrectSolutionValueForFirstEmptyCell() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid.getValue(r, c) == 0) {
                    assertEquals(SPEC_SOLUTION[r][c], grid.getSolutionValue(r, c));
                    return;
                }
            }
        }
        fail("No empty cell found");
    }

    @Test
    void scenario1_gridRendersA3CorrectlyAfterPlace() {
        grid.placeValue(0, 2, 4);
        assertTrue(new GridPrinter().render(grid).contains("A 5 3 4"));
    }

    @Test
    void scenario1_puzzleIsCompleteWhenAllCellsMatchSolution() {
        assertTrue(new SudokuGrid(SPEC_SOLUTION, SPEC_SOLUTION).isComplete());
    }

    @Test
    void scenario1_puzzleIsNotCompleteWithEmptyCells() {
        assertFalse(grid.isComplete());
    }

    // -------------------------------------------------------------------
    // Scenario 2 – Invalid move: attempt to overwrite a pre-filled cell
    // Spec: "A1 6" → "Invalid move. A1 is pre-filled."
    // -------------------------------------------------------------------

    @Test
    void scenario2_parseA1_6returnsPlaceCommand() {
        Command cmd = parser.parse("A1 6");
        assertEquals(Command.Type.PLACE, cmd.getType());
        assertEquals(0, cmd.getRow());
        assertEquals(0, cmd.getCol());
    }

    @Test
    void scenario2_A1isLocked() {
        assertTrue(grid.isLocked(0, 0));
    }

    @Test
    void scenario2_placeOnLockedCellReturnsFalse() {
        assertFalse(grid.placeValue(0, 0, 6));
    }

    @Test
    void scenario2_lockedCellValueUnchangedAfterRejection() {
        grid.placeValue(0, 0, 6);
        assertEquals(5, grid.getValue(0, 0));
    }

    @Test
    void scenario2_gridStillShowsPreFilledValuesAfterRejection() {
        assertTrue(new GridPrinter().render(grid).contains("A 5 3"));
    }

    // -------------------------------------------------------------------
    // Scenario 3 – Row violation
    // Spec: "A3 3" accepted, then check → "Number 3 already exists in Row A."
    // -------------------------------------------------------------------

    @Test
    void scenario3_parseA3_3returnsPlaceCommand() {
        Command cmd = parser.parse("A3 3");
        assertEquals(Command.Type.PLACE, cmd.getType());
    }

    @Test
    void scenario3_placeIsAcceptedWithoutImmediateValidation() {
        assertTrue(grid.placeValue(0, 2, 3));
        assertEquals(3, grid.getValue(0, 2));
    }

    @Test
    void scenario3_checkReturnsRowViolation() {
        grid.placeValue(0, 2, 3);
        List<String> violations = validator.validate(grid.snapshot());
        assertFalse(violations.isEmpty());
        assertEquals("Number 3 already exists in Row A.", violations.get(0));
    }

    // -------------------------------------------------------------------
    // Scenario 4 – Column violation
    // Spec: "C1 5" accepted, then check → "Number 5 already exists in Column 1."
    // Note: C1=5 also violates the top-left 3x3 box (A1 is already 5), but the
    // validator reports column violations before subgrid violations, matching the spec.
    // -------------------------------------------------------------------

    @Test
    void scenario4_parseC1_5returnsPlaceCommand() {
        Command cmd = parser.parse("C1 5");
        assertEquals(Command.Type.PLACE, cmd.getType());
        assertEquals(2, cmd.getRow());
        assertEquals(0, cmd.getCol());
    }

    @Test
    void scenario4_placeIsAccepted() {
        assertTrue(grid.placeValue(2, 0, 5));
        assertEquals(5, grid.getValue(2, 0));
    }

    @Test
    void scenario4_checkReturnsColumnViolation() {
        grid.placeValue(2, 0, 5);
        List<String> violations = validator.validate(grid.snapshot());
        assertFalse(violations.isEmpty());
        assertEquals("Number 5 already exists in Column 1.", violations.get(0));
    }

    // -------------------------------------------------------------------
    // Scenario 5 – Sub-grid violation
    // Spec example uses "B3 8", but the spec puzzle already has 8 in column 3
    // at C3, making B3=8 a column violation as well. The validator reports the
    // column violation first (rows → columns → subgrids), so B3=8 produces
    // "Column 3" not "subgrid" — a contradiction in the spec's example choice.
    //
    // The tests below verify:
    //   (a) B3=8 does produce a violation (spec intent is met)
    //   (b) A pure subgrid violation (B2=8) produces the correct spec message
    // -------------------------------------------------------------------

    @Test
    void scenario5_B3_8parseIsPlaceCommand() {
        Command cmd = parser.parse("B3 8");
        assertEquals(Command.Type.PLACE, cmd.getType());
        assertEquals(1, cmd.getRow());
        assertEquals(2, cmd.getCol());
    }

    @Test
    void scenario5_B3_8placeIsAccepted() {
        assertTrue(grid.placeValue(1, 2, 8));
        assertEquals(8, grid.getValue(1, 2));
    }

    @Test
    void scenario5_B3_8checkDetectsAViolation() {
        grid.placeValue(1, 2, 8);
        assertFalse(validator.validate(grid.snapshot()).isEmpty());
    }

    @Test
    void scenario5_pureSubgridViolationProducesCorrectMessage() {
        // B2=8: row B has no 8, col 2 (1-indexed) has no 8, but the top-left
        // box already contains 8 at C3 — a pure subgrid violation.
        grid.placeValue(1, 1, 8);
        List<String> violations = validator.validate(grid.snapshot());
        assertFalse(violations.isEmpty());
        assertEquals("Number 8 already exists in the same 3\u00d73 subgrid.", violations.get(0));
    }

    // -------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------

    private static int[][] copy(int[][] src) {
        int[][] result = new int[src.length][];
        for (int i = 0; i < src.length; i++) result[i] = src[i].clone();
        return result;
    }
}

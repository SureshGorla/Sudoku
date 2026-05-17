package com.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SudokuValidatorTest {

    private SudokuValidator validator;

    private static final int[][] VALID_PARTIAL = {
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

    private static final int[][] COMPLETE_VALID = {
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

    @BeforeEach
    void setUp() {
        validator = new SudokuValidator();
    }

    @Test
    void validPartialGridHasNoViolations() {
        assertTrue(validator.validate(VALID_PARTIAL).isEmpty());
    }

    @Test
    void completeValidGridHasNoViolations() {
        assertTrue(validator.validate(COMPLETE_VALID).isEmpty());
    }

    @Test
    void detectsDuplicateInRow() {
        int[][] grid = copy(VALID_PARTIAL);
        grid[0][2] = 3;

        List<String> violations = validator.validate(grid);
        assertFalse(violations.isEmpty());
        assertTrue(violations.get(0).contains("Row A"));
        assertTrue(violations.get(0).contains("3"));
    }

    @Test
    void detectsDuplicateInColumn() {
        int[][] grid = copy(VALID_PARTIAL);
        grid[2][0] = 5;

        List<String> violations = validator.validate(grid);
        assertFalse(violations.isEmpty());
        assertTrue(violations.get(0).contains("Column 1"));
        assertTrue(violations.get(0).contains("5"));
    }

    @Test
    void detectsDuplicateInSubgrid() {
        int[][] grid = copy(VALID_PARTIAL);
        // (2,1) has no row or column duplicate with 5; only the top-left box already contains 5 at (0,0)
        grid[2][1] = 5;

        List<String> violations = validator.validate(grid);
        assertFalse(violations.isEmpty());
        assertTrue(violations.get(0).contains("subgrid"));
    }

    @Test
    void detectsSubgridViolationExample() {
        int[][] grid = copy(VALID_PARTIAL);
        grid[1][1] = 8;

        assertFalse(validator.validate(grid).isEmpty());
    }

    @Test
    void rowViolationMessageIncludesRowLabel() {
        int[][] grid = copy(VALID_PARTIAL);
        grid[4][1] = 4;

        String msg = validator.validate(grid).get(0);
        assertTrue(msg.contains("Row E"), "Expected 'Row E' in: " + msg);
    }

    @Test
    void columnViolationMessageIncludesColumnNumber() {
        int[][] grid = copy(VALID_PARTIAL);
        grid[2][4] = 7;

        String msg = validator.validate(grid).get(0);
        assertTrue(msg.contains("Column"), "Expected 'Column' in: " + msg);
    }

    private static int[][] copy(int[][] src) {
        int[][] result = new int[src.length][];
        for (int i = 0; i < src.length; i++) result[i] = src[i].clone();
        return result;
    }
}

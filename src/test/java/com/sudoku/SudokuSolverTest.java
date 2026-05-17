package com.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SudokuSolverTest {

    private SudokuSolver solver;

    private static final int[][] CLASSIC_PUZZLE = {
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

    private static final int[][] CLASSIC_SOLUTION = {
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
        solver = new SudokuSolver();
    }

    @Test
    void solvesFillsAllCells() {
        int[][] grid = copy(CLASSIC_PUZZLE);
        assertTrue(solver.solve(grid));
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                assertNotEquals(0, grid[r][c], "Cell [" + r + "][" + c + "] should not be empty");
            }
        }
    }

    @Test
    void solveProducesCorrectSolution() {
        int[][] grid = copy(CLASSIC_PUZZLE);
        solver.solve(grid);
        for (int r = 0; r < 9; r++) {
            assertArrayEquals(CLASSIC_SOLUTION[r], grid[r], "Row " + r + " differs");
        }
    }

    @Test
    void solveReturnsFalseForUnsolvablePuzzle() {
        int[][] impossible = new int[9][9];
        impossible[0][0] = 1;
        impossible[0][1] = 1;
        assertFalse(solver.solve(impossible));
    }

    @Test
    void solveLeavesAlreadyFilledCellsUntouched() {
        int[][] grid = copy(CLASSIC_PUZZLE);
        solver.solve(grid);
        assertEquals(5, grid[0][0]);
        assertEquals(3, grid[0][1]);
    }

    @Test
    void solveRandomizedProducesValidCompletedGrid() {
        int[][] grid = new int[9][9];
        assertTrue(solver.solveRandomized(grid, new Random(42)));

        SudokuValidator v = new SudokuValidator();
        assertTrue(v.validate(grid).isEmpty());

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                assertNotEquals(0, grid[r][c]);
            }
        }
    }

    @Test
    void differentSeedsProduceDifferentGrids() {
        int[][] grid1 = new int[9][9];
        int[][] grid2 = new int[9][9];
        solver.solveRandomized(grid1, new Random(1L));
        solver.solveRandomized(grid2, new Random(2L));

        boolean differ = false;
        outer:
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid1[r][c] != grid2[r][c]) { differ = true; break outer; }
            }
        }
        assertTrue(differ);
    }

    @Test
    void classicPuzzleHasUniqueSolution() {
        assertEquals(1, solver.countSolutions(copy(CLASSIC_PUZZLE), 2));
    }

    @Test
    void emptyGridHasMoreThanOneSolution() {
        assertEquals(2, solver.countSolutions(new int[9][9], 2));
    }

    @Test
    void countSolutionsStopsAtLimit() {
        assertEquals(1, solver.countSolutions(new int[9][9], 1));
    }

    private static int[][] copy(int[][] src) {
        int[][] result = new int[src.length][];
        for (int i = 0; i < src.length; i++) result[i] = src[i].clone();
        return result;
    }
}

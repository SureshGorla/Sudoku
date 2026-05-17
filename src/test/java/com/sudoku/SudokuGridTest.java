package com.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuGridTest {

    private static final int[][] PUZZLE = new int[9][9];
    private static final int[][] SOLUTION = {
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

    static {
        PUZZLE[0][0] = 5;
    }

    private SudokuGrid grid;

    @BeforeEach
    void setUp() {
        grid = new SudokuGrid(PUZZLE, SOLUTION);
    }

    @Test
    void preFilledCellIsLocked() {
        assertTrue(grid.isLocked(0, 0));
    }

    @Test
    void emptyCellIsNotLocked() {
        assertFalse(grid.isLocked(0, 1));
    }

    @Test
    void placeValueSucceedsOnEmptyCell() {
        assertTrue(grid.placeValue(1, 1, 7));
        assertEquals(7, grid.getValue(1, 1));
    }

    @Test
    void placeValueFailsOnLockedCell() {
        assertFalse(grid.placeValue(0, 0, 9));
        assertEquals(5, grid.getValue(0, 0));
    }

    @Test
    void clearValueSucceedsOnUserCell() {
        grid.placeValue(1, 1, 7);
        assertTrue(grid.clearValue(1, 1));
        assertEquals(0, grid.getValue(1, 1));
    }

    @Test
    void clearValueFailsOnLockedCell() {
        assertFalse(grid.clearValue(0, 0));
        assertEquals(5, grid.getValue(0, 0));
    }

    @Test
    void solutionValueIsAvailableForAnyCell() {
        assertEquals(5, grid.getSolutionValue(0, 0));
        assertEquals(3, grid.getSolutionValue(0, 1));
    }

    @Test
    void snapshotReturnsCopy() {
        int[][] snap = grid.snapshot();
        snap[0][0] = 99;
        assertEquals(5, grid.getValue(0, 0));
    }

    @Test
    void incompletePuzzleIsNotComplete() {
        assertFalse(grid.isComplete());
    }

    @Test
    void puzzleIsCompleteWhenAllCellsMatchSolution() {
        SudokuGrid full = new SudokuGrid(SOLUTION, SOLUTION);
        assertTrue(full.isComplete());
    }

    @Test
    void puzzleIsNotCompleteWhenWrongValueEntered() {
        int[][] partial = copyOf(SOLUTION);
        partial[0][1] = 0;
        SudokuGrid g = new SudokuGrid(partial, SOLUTION);
        g.placeValue(0, 1, 9);
        assertFalse(g.isComplete());
    }

    private static int[][] copyOf(int[][] src) {
        int[][] copy = new int[src.length][];
        for (int i = 0; i < src.length; i++) copy[i] = src[i].clone();
        return copy;
    }
}

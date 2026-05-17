package com.sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuGeneratorTest {

    private static final long SEED = 42L;

    @Test
    void generateReturnsPuzzleAndSolution() {
        int[][][] result = new SudokuGenerator(SEED).generate();
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(9, result[0].length);
        assertEquals(9, result[1].length);
    }

    @Test
    void puzzleHasAtLeastTargetClues() {
        int[][] puzzle = new SudokuGenerator(SEED).generate()[0];
        int clues = 0;
        for (int[] row : puzzle) for (int v : row) if (v != 0) clues++;
        assertTrue(clues >= SudokuGenerator.TARGET_CLUES);
        assertTrue(clues <= 45);
    }

    @Test
    void solutionIsFullyFilledAndValid() {
        int[][] solution = new SudokuGenerator(SEED).generate()[1];
        for (int[] row : solution) for (int v : row) assertNotEquals(0, v);
        assertTrue(new SudokuValidator().validate(solution).isEmpty());
    }

    @Test
    void puzzleCellsMatchSolutionWherePresent() {
        int[][][] result = new SudokuGenerator(SEED).generate();
        int[][] puzzle   = result[0];
        int[][] solution = result[1];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (puzzle[r][c] != 0) {
                    assertEquals(solution[r][c], puzzle[r][c]);
                }
            }
        }
    }

    @Test
    void puzzleHasUniqueSolution() {
        int[][] puzzle = new SudokuGenerator(SEED).generate()[0];
        int[][] copy = new int[9][];
        for (int i = 0; i < 9; i++) copy[i] = puzzle[i].clone();
        assertEquals(1, new SudokuSolver().countSolutions(copy, 2));
    }

    @Test
    void differentSeedsProduceDifferentPuzzles() {
        int[][] p1 = new SudokuGenerator(1L).generate()[0];
        int[][] p2 = new SudokuGenerator(2L).generate()[0];
        boolean differ = false;
        outer:
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (p1[r][c] != p2[r][c]) { differ = true; break outer; }
            }
        }
        assertTrue(differ);
    }
}

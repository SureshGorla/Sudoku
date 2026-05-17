package com.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    public static final int TARGET_CLUES = 30;

    private final Random random;
    private final SudokuSolver solver;

    public SudokuGenerator() {
        this(System.currentTimeMillis());
    }

    public SudokuGenerator(long seed) {
        this.random = new Random(seed);
        this.solver = new SudokuSolver();
    }

    public int[][][] generate() {
        int[][] solution = buildSolution();
        int[][] puzzle   = digHoles(solution);
        return new int[][][]{puzzle, solution};
    }

    private int[][] buildSolution() {
        int[][] grid = new int[9][9];
        solver.solveRandomized(grid, random);
        return grid;
    }

    private int[][] digHoles(int[][] solution) {
        int[][] puzzle = deepCopy(solution);
        List<int[]> cells = allCells();
        Collections.shuffle(cells, random);
        int filledCount = 81;
        for (int[] cell : cells) {
            if (filledCount <= TARGET_CLUES) break;

            int row = cell[0], col = cell[1];
            int backup = puzzle[row][col];
            puzzle[row][col] = 0;

            if (hasUniqueSolution(puzzle)) {
                filledCount--;
            } else {
                puzzle[row][col] = backup;
            }
        }
        return puzzle;
    }

    private boolean hasUniqueSolution(int[][] puzzle) {
        int[][] copy = deepCopy(puzzle);
        return solver.countSolutions(copy, 2) == 1;
    }

    private List<int[]> allCells() {
        List<int[]> cells = new ArrayList<>(81);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells.add(new int[]{r, c});
            }
        }
        return cells;
    }

    private static int[][] deepCopy(int[][] src) {
        return Arrays.stream(src).map(int[]::clone).toArray(int[][]::new);
    }
}

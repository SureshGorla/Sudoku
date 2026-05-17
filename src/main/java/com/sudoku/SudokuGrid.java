package com.sudoku;

import java.util.Arrays;

public class SudokuGrid {

    private static final int SIZE = 9;

    private final int[][] grid;
    private final boolean[][] locked;
    private final int[][] solution;

    public SudokuGrid(int[][] puzzle, int[][] solution) {
        this.grid     = deepCopy(puzzle);
        this.solution = deepCopy(solution);
        this.locked   = new boolean[SIZE][SIZE];

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                this.locked[r][c] = (puzzle[r][c] != 0);
            }
        }
    }

    public int getValue(int row, int col) {
        return grid[row][col];
    }

    public boolean isLocked(int row, int col) {
        return locked[row][col];
    }

    public int getSolutionValue(int row, int col) {
        return solution[row][col];
    }

    public int[][] snapshot() {
        return deepCopy(grid);
    }

    public boolean isComplete() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] != solution[r][c]) return false;
            }
        }
        return true;
    }

    public boolean placeValue(int row, int col, int value) {
        if (locked[row][col]) return false;
        grid[row][col] = value;
        return true;
    }

    public boolean clearValue(int row, int col) {
        if (locked[row][col]) return false;
        grid[row][col] = 0;
        return true;
    }

    private static int[][] deepCopy(int[][] source) {
        return Arrays.stream(source).map(int[]::clone).toArray(int[][]::new);
    }
}

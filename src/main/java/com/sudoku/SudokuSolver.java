package com.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuSolver {

    public boolean solve(int[][] grid) {
        return backtrack(grid, null);
    }

    public boolean solveRandomized(int[][] grid, Random random) {
        return backtrack(grid, random);
    }

    public int countSolutions(int[][] grid, int limit) {
        int[] count = {0};
        countHelper(grid, limit, count);
        return count[0];
    }

    private boolean backtrack(int[][] grid, Random random) {
        int[] cell = findFirstEmpty(grid);
        if (cell == null) return true;

        int row = cell[0], col = cell[1];
        List<Integer> candidates = buildCandidates(grid, row, col);
        if (random != null) Collections.shuffle(candidates, random);

        for (int num : candidates) {
            grid[row][col] = num;
            if (backtrack(grid, random)) return true;
            grid[row][col] = 0;
        }
        return false;
    }

    private void countHelper(int[][] grid, int limit, int[] count) {
        if (count[0] >= limit) return;

        int[] cell = findFirstEmpty(grid);
        if (cell == null) {
            count[0]++;
            return;
        }

        int row = cell[0], col = cell[1];
        for (int num : buildCandidates(grid, row, col)) {
            grid[row][col] = num;
            countHelper(grid, limit, count);
            grid[row][col] = 0;
            if (count[0] >= limit) return;
        }
    }

    private int[] findFirstEmpty(int[][] grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] == 0) return new int[]{r, c};
            }
        }
        return null;
    }

    private List<Integer> buildCandidates(int[][] grid, int row, int col) {
        boolean[] used = new boolean[10];
        for (int c = 0; c < 9; c++) used[grid[row][c]] = true;
        for (int r = 0; r < 9; r++) used[grid[r][col]] = true;
        int br = (row / 3) * 3, bc = (col / 3) * 3;
        for (int r = br; r < br + 3; r++) {
            for (int c = bc; c < bc + 3; c++) {
                used[grid[r][c]] = true;
            }
        }
        List<Integer> candidates = new ArrayList<>();
        for (int n = 1; n <= 9; n++) {
            if (!used[n]) candidates.add(n);
        }
        return candidates;
    }
}

package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuValidator {

    public List<String> validate(int[][] grid) {
        List<String> violations = new ArrayList<>();

        checkRows(grid, violations);
        if (!violations.isEmpty()) return violations;

        checkColumns(grid, violations);
        if (!violations.isEmpty()) return violations;

        checkSubgrids(grid, violations);
        return violations;
    }

    private void checkRows(int[][] grid, List<String> out) {
        for (int row = 0; row < 9; row++) {
            int dup = firstDuplicate(grid[row]);
            if (dup != -1) {
                out.add("Number " + dup + " already exists in Row " + rowLabel(row) + ".");
                return;
            }
        }
    }

    private void checkColumns(int[][] grid, List<String> out) {
        for (int col = 0; col < 9; col++) {
            int[] column = extractColumn(grid, col);
            int dup = firstDuplicate(column);
            if (dup != -1) {
                out.add("Number " + dup + " already exists in Column " + (col + 1) + ".");
                return;
            }
        }
    }

    private void checkSubgrids(int[][] grid, List<String> out) {
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                int[] cells = extractSubgrid(grid, boxRow, boxCol);
                int dup = firstDuplicate(cells);
                if (dup != -1) {
                    out.add("Number " + dup + " already exists in the same 3\u00d73 subgrid.");
                    return;
                }
            }
        }
    }

    private int[] extractColumn(int[][] grid, int col) {
        int[] column = new int[9];
        for (int r = 0; r < 9; r++) column[r] = grid[r][col];
        return column;
    }

    private int[] extractSubgrid(int[][] grid, int boxRow, int boxCol) {
        int[] cells = new int[9];
        int idx = 0;
        for (int r = boxRow * 3; r < boxRow * 3 + 3; r++) {
            for (int c = boxCol * 3; c < boxCol * 3 + 3; c++) {
                cells[idx++] = grid[r][c];
            }
        }
        return cells;
    }

    private int firstDuplicate(int[] values) {
        boolean[] seen = new boolean[10];
        for (int v : values) {
            if (v == 0) continue;
            if (seen[v]) return v;
            seen[v] = true;
        }
        return -1;
    }

    private char rowLabel(int row) {
        return (char) ('A' + row);
    }
}

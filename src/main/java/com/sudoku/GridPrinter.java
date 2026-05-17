package com.sudoku;

public class GridPrinter {

    public String render(SudokuGrid grid) {
        StringBuilder sb = new StringBuilder();
        sb.append("    1 2 3 4 5 6 7 8 9\n");
        for (int row = 0; row < 9; row++) {
            sb.append("  ").append((char) ('A' + row)).append(' ');
            for (int col = 0; col < 9; col++) {
                int value = grid.getValue(row, col);
                sb.append(value == 0 ? "_" : value);
                if (col < 8) sb.append(' ');
            }
            sb.append('\n');
        }

        return sb.toString();
    }
}

package com.sudoku;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SudokuGame {

    private final CommandParser parser     = new CommandParser();
    private final SudokuValidator validator = new SudokuValidator();
    private final SudokuGenerator generator = new SudokuGenerator(8);
    private final GridPrinter printer      = new GridPrinter();

    public void run() {
        System.out.println("Welcome to Sudoku!\n");
        try (Scanner scanner = new Scanner(System.in)) {
            boolean keepPlaying = true;
            while (keepPlaying) {
                keepPlaying = playRound(scanner);
            }
        }
        System.out.println("Thanks for playing. Goodbye!");
    }

    private boolean playRound(Scanner scanner) {
        SudokuGrid grid = createNewPuzzle();
        System.out.println("Here is your puzzle:");
        System.out.print(printer.render(grid));

        while (true) {
            System.out.print("\nEnter command (e.g., A3 4, C5 clear, hint, check, quit): ");
            String input = scanner.nextLine();
            Command command = parser.parse(input);

            boolean shouldReprintGrid = handleCommand(command, grid);

            if (shouldReprintGrid) {
                System.out.println("\nCurrent grid:");
                System.out.print(printer.render(grid));
            }

            if (command.getType() == Command.Type.QUIT) {
                return false;
            }

            if (grid.isComplete()) {
                System.out.println("\nYou have successfully completed the Sudoku puzzle!");
                return askPlayAgain(scanner);
            }
        }
    }

    private boolean handleCommand(Command command, SudokuGrid grid) {
        return switch (command.getType()) {
            case PLACE   -> handlePlace(command, grid);
            case CLEAR   -> handleClear(command, grid);
            case HINT    -> { handleHint(grid);  yield false; }
            case CHECK   -> { handleCheck(grid); yield false; }
            case QUIT    -> false;
            case INVALID -> { System.out.println(command.getErrorMessage()); yield false; }
        };
    }

    private boolean handlePlace(Command command, SudokuGrid grid) {
        int row = command.getRow(), col = command.getCol();
        String cell = cellLabel(row, col);

        if (grid.isLocked(row, col)) {
            System.out.println("Invalid move. " + cell + " is pre-filled.");
            return true;
        }

        grid.placeValue(row, col, command.getValue());
        System.out.println("Move accepted.");
        return true;
    }

    private boolean handleClear(Command command, SudokuGrid grid) {
        int row = command.getRow(), col = command.getCol();
        String cell = cellLabel(row, col);

        if (grid.isLocked(row, col)) {
            System.out.println("Cannot clear pre-filled cell " + cell + ".");
            return true;
        }

        grid.clearValue(row, col);
        System.out.println("Cell " + cell + " cleared.");
        return true;
    }

    private void handleHint(SudokuGrid grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid.getValue(r, c) == 0) {
                    System.out.println("Hint: Cell " + cellLabel(r, c) + " = " + grid.getSolutionValue(r, c));
                    return;
                }
            }
        }
        System.out.println("No empty cells remaining.");
    }

    private void handleCheck(SudokuGrid grid) {
        List<String> violations = validator.validate(grid.snapshot());
        if (violations.isEmpty()) {
            System.out.println("No rule violations detected.");
        } else {
            violations.forEach(System.out::println);
        }
    }

    private SudokuGrid createNewPuzzle() {
        int[][][] generated = generator.generate();
        return new SudokuGrid(generated[0], generated[1]);
    }

    private boolean askPlayAgain(Scanner scanner) {
        System.out.println("Press any key to play again...");
        String response = scanner.nextLine().trim().toLowerCase();
        return !response.equals("quit") && !response.equals("q");
    }

    private String cellLabel(int row, int col) {
        return String.valueOf((char) ('A' + row)) + (col + 1);
    }
}

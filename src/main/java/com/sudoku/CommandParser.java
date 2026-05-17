package com.sudoku;

public class CommandParser {

    public Command parse(String input) {
        if (input == null) {
            return Command.quit();
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return Command.invalid("Please enter a command.");
        }

        String lower = trimmed.toLowerCase();

        switch (lower) {
            case "hint"              -> { return Command.hint(); }
            case "check"             -> { return Command.check(); }
            case "quit", "q", "exit" -> { return Command.quit(); }
        }

        String[] parts = trimmed.split("\\s+");
        if (parts.length < 2) {
            return Command.invalid("Unknown command. Try 'A3 4', 'C5 clear', 'hint', 'check', or 'quit'.");
        }

        String cellRef = parts[0].toUpperCase();
        if (cellRef.length() < 2) {
            return Command.invalid("Invalid cell reference '" + cellRef + "'. Expected format: A3");
        }

        char rowChar = cellRef.charAt(0);
        if (rowChar < 'A' || rowChar > 'I') {
            return Command.invalid("Row must be A-I, but got '" + rowChar + "'.");
        }
        int row = rowChar - 'A';

        int col;
        try {
            col = Integer.parseInt(cellRef.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return Command.invalid("Column in '" + cellRef + "' must be a number 1-9.");
        }
        if (col < 0 || col > 8) {
            return Command.invalid("Column must be between 1 and 9.");
        }

        String action = parts[1].toLowerCase();
        if (action.equals("clear")) {
            return Command.clear(row, col);
        }

        int value;
        try {
            value = Integer.parseInt(action);
        } catch (NumberFormatException e) {
            return Command.invalid("Value must be a number 1-9 or 'clear', but got '" + parts[1] + "'.");
        }
        if (value < 1 || value > 9) {
            return Command.invalid("Number must be between 1 and 9, but got " + value + ".");
        }

        return Command.place(row, col, value);
    }
}

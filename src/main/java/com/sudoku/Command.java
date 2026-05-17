package com.sudoku;

public class Command {

    public enum Type {
        PLACE,
        CLEAR,
        HINT,
        CHECK,
        QUIT,
        INVALID
    }

    private final Type type;
    private final int row;
    private final int col;
    private final int value;
    private final String errorMessage;

    private Command(Type type, int row, int col, int value, String errorMessage) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public static Command place(int row, int col, int value) {
        return new Command(Type.PLACE, row, col, value, null);
    }

    public static Command clear(int row, int col) {
        return new Command(Type.CLEAR, row, col, 0, null);
    }

    public static Command hint() {
        return new Command(Type.HINT, -1, -1, 0, null);
    }

    public static Command check() {
        return new Command(Type.CHECK, -1, -1, 0, null);
    }

    public static Command quit() {
        return new Command(Type.QUIT, -1, -1, 0, null);
    }

    public static Command invalid(String errorMessage) {
        return new Command(Type.INVALID, -1, -1, 0, errorMessage);
    }

    public Type getType()           { return type; }
    public int getRow()             { return row; }
    public int getCol()             { return col; }
    public int getValue()           { return value; }
    public String getErrorMessage() { return errorMessage; }
}

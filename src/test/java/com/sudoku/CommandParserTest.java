package com.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    private CommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new CommandParser();
    }

    @Test
    void parsesPlaceCommand() {
        Command cmd = parser.parse("A3 4");
        assertEquals(Command.Type.PLACE, cmd.getType());
        assertEquals(0, cmd.getRow());
        assertEquals(2, cmd.getCol());
        assertEquals(4, cmd.getValue());
    }

    @Test
    void parsesPlaceCommandCaseInsensitive() {
        Command cmd = parser.parse("i9 9");
        assertEquals(Command.Type.PLACE, cmd.getType());
        assertEquals(8, cmd.getRow());
        assertEquals(8, cmd.getCol());
        assertEquals(9, cmd.getValue());
    }

    @Test
    void parsesPlaceCommandWithExtraWhitespace() {
        Command cmd = parser.parse("  B5  7  ");
        assertEquals(Command.Type.PLACE, cmd.getType());
        assertEquals(1, cmd.getRow());
        assertEquals(4, cmd.getCol());
        assertEquals(7, cmd.getValue());
    }

    @Test
    void parsesClearCommand() {
        Command cmd = parser.parse("C5 clear");
        assertEquals(Command.Type.CLEAR, cmd.getType());
        assertEquals(2, cmd.getRow());
        assertEquals(4, cmd.getCol());
    }

    @Test
    void parsesClearCommandCaseInsensitive() {
        Command cmd = parser.parse("D2 CLEAR");
        assertEquals(Command.Type.CLEAR, cmd.getType());
    }

    @Test
    void parsesHintCommand() {
        assertEquals(Command.Type.HINT, parser.parse("hint").getType());
        assertEquals(Command.Type.HINT, parser.parse("HINT").getType());
    }

    @Test
    void parsesCheckCommand() {
        assertEquals(Command.Type.CHECK, parser.parse("check").getType());
        assertEquals(Command.Type.CHECK, parser.parse("CHECK").getType());
    }

    @ParameterizedTest
    @ValueSource(strings = {"quit", "QUIT", "q", "exit"})
    void parsesQuitCommand(String input) {
        assertEquals(Command.Type.QUIT, parser.parse(input).getType());
    }

    @Test
    void rejectsNullAsQuit() {
        assertEquals(Command.Type.QUIT, parser.parse(null).getType());
    }

    @Test
    void rejectsEmptyInput() {
        Command cmd = parser.parse("  ");
        assertEquals(Command.Type.INVALID, cmd.getType());
        assertNotNull(cmd.getErrorMessage());
    }

    @Test
    void rejectsRowOutOfRange() {
        assertEquals(Command.Type.INVALID, parser.parse("Z3 5").getType());
    }

    @Test
    void rejectsColumnZero() {
        assertEquals(Command.Type.INVALID, parser.parse("A0 5").getType());
    }

    @Test
    void rejectsColumnTen() {
        assertEquals(Command.Type.INVALID, parser.parse("A10 5").getType());
    }

    @Test
    void rejectsValueZero() {
        assertEquals(Command.Type.INVALID, parser.parse("A3 0").getType());
    }

    @Test
    void rejectsValueTen() {
        assertEquals(Command.Type.INVALID, parser.parse("A3 10").getType());
    }

    @Test
    void rejectsNonNumericValue() {
        assertEquals(Command.Type.INVALID, parser.parse("A3 xyz").getType());
    }

    @Test
    void rejectsUnknownSingleWord() {
        assertEquals(Command.Type.INVALID, parser.parse("foobar").getType());
    }
}

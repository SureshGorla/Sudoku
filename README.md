# Sudoku – Command-Line Game (Java)

A fully playable Sudoku game for the command line, written in Java 17+.

---

## Features

| Command | Example | Description |
|---------|---------|-------------|
| Place   | `A3 4`  | Place the number 4 in row A, column 3 |
| Clear   | `C5 clear` | Remove a user-entered value |
| Hint    | `hint`  | Reveal the correct value for the first empty cell |
| Check   | `check` | Report any rule violations (row / column / sub-grid) |
| Quit    | `quit`  | Exit the game |

- Puzzles are randomly generated with **30 pre-filled clues** and a **guaranteed unique solution**.
- Pre-filled cells are locked and cannot be modified.
- The game detects completion and asks if you want to play again.

---

## Requirements

| Requirement | Version |
|-------------|---------|
| Java (JDK)  | 17 or newer |
| Maven       | 3.6 or newer |

The project has been developed and tested on **Linux** and **macOS**.  
It should also work on **Windows** (run `mvnw.cmd` or use a system Maven).

---

## How to Build and Run

### 1. Clone / unzip the project
```
unzip sudoku.zip
cd sudoku
```

### 2. Run the tests
```
mvn test
```
Maven will download JUnit 5 automatically on first run (requires internet access).

### 3. Build the executable JAR
```
mvn package -DskipTests
```
This produces `target/sudoku.jar`.

### 4. Play the game
```
java -jar target/sudoku.jar
```

---

## Project Structure

```
sudoku/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/sudoku/
    │   ├── Main.java              Entry point
    │   ├── SudokuGame.java        Game loop & command dispatch
    │   ├── Command.java           Immutable command value object
    │   ├── CommandParser.java     Parses raw user input → Command
    │   ├── SudokuGrid.java        Mutable grid state (values + locks)
    │   ├── SudokuGenerator.java   Random puzzle generation
    │   ├── SudokuSolver.java      Backtracking solver
    │   ├── SudokuValidator.java   Rule violation detection
    │   └── GridPrinter.java       Console rendering
    └── test/java/com/sudoku/
        ├── CommandParserTest.java
        ├── SudokuGridTest.java
        ├── SudokuValidatorTest.java
        ├── SudokuSolverTest.java
        ├── SudokuGeneratorTest.java
        └── GridPrinterTest.java
```

---

## Design Decisions

### Single Responsibility
Each class has one clear job:
- `SudokuGame` orchestrates but delegates all logic elsewhere.
- `CommandParser` only converts strings to `Command` objects.
- `SudokuValidator` only checks rules; it does not mutate state.

### Immutability at the boundary
`Command` is an immutable value object created by `CommandParser`.  
`SudokuGrid` exposes a `snapshot()` method that returns a defensive copy, preventing
validator or printer code from accidentally mutating game state.

### Puzzle generation
1. A complete valid grid is built with **randomised backtracking** (Fisher-Yates shuffle on candidates).
2. Cells are removed one at a time in random order.
3. After each removal, `countSolutions(..., limit=2)` verifies uniqueness.
4. Removal stops once 30 clues remain (or sooner if uniqueness would be lost).

This guarantees every puzzle has exactly one solution.

### No magic numbers
Grid size (9), target clues (30), and row labels ('A'–'I') are derived from constants or
computed, not scattered as literals.

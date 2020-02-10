package mazeMapping;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class MazeController {
    private byte[][] maze;
    private int rowPos;
    private int colPos;
    private int rows;
    private int cols;

    private ServerCommunicator serverCommunicator;

    public MazeController(ServerCommunicator serverCommunicator) {
        this.serverCommunicator = serverCommunicator;
    }

    public void createMaze() {
        Pair<Integer, Integer> size = serverCommunicator.getSize();
        cols = size.getKey();
        rows = size.getValue();

        rows = rows * 2 - 1;
        cols = cols * 2 - 1;


        maze = new byte[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 2;
            }
    }

    public void setPosition() {
        Pair<Integer, Integer> startPosition = serverCommunicator.getStartPosition();

        colPos = startPosition.getKey();
        rowPos = startPosition.getValue();

        rowPos = (rowPos - 1) * 2;
        colPos = (colPos - 1) * 2;
    }

    public void makeMove(Direction direction) {
        serverCommunicator.makeMove(direction.name().toLowerCase());

        rowPos += 2 * direction.getRowTranslation();
        colPos += 2 * direction.getColTranslation();
    }

    public void updateMaze() {
        serverCommunicator.setPossibilities();

        for (Direction d : Direction.values()) {
            int rowTranslation = d.getRowTranslation();
            int colTranslation = d.getColTranslation();
            int passageRow = rowPos + rowTranslation;
            int passageCol = colPos + colTranslation;
            int fieldRow = passageRow + rowTranslation;
            int fieldCol = passageCol + colTranslation;

            if (checkIfFieldIsInMaze(passageRow, passageCol)) {
                maze[passageRow][passageCol] = d.getPossibility();
                if (d.getPossibility() == 0) {
                    if (maze[fieldRow][fieldCol] == 2) {
                        maze[fieldRow][fieldCol] = 0;
                    }
                }
            }
        }
    }

    public boolean checkIfFieldIsInMaze(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) return true;
        return false;
    }

    public byte checkInformationAmount(int row, int col) {
        byte amount = 0;
        for (Direction d : Direction.values()) {
            int rowId = row + d.getRowTranslation();
            int colId = col + d.getColTranslation();
            if (checkIfFieldIsInMaze(rowId, colId) && maze[rowId][colId] == 2) amount++;
        }
        return amount;
    }

    public void saveMaze() {
        try {
            Writer writer = new BufferedWriter(new FileWriter("./upload.txt", false));
            for (int i = -1; i <= rows; i++) {
                writer.write("\n");
                for (int j = -1; j <= cols; j++) {
                    if (i == -1 || j == -1 || i == rows || j == cols || maze[i][j] == 1 || maze[i][j] == 2)
                        writer.write('+');
                    else if (maze[i][j] == 0) writer.write('0');
                }
            }
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printMaze() {
        try {
            Writer writer = new BufferedWriter(new FileWriter("./upload.txt"));
            for (int i = -1; i <= rows; i++) {
                for (int j = -1; j <= cols; j++) {
                    if (i == -1 || j == -1 || i == rows || j == cols || maze[i][j] == 1 || maze[i][j] == 2)
                        writer.write('+');
                    else if (maze[i][j] == 0) writer.write('0');
                }
                if (rows != i)
                    writer.write("\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getColPos() {
        return colPos;
    }

    public int getRowPos() {
        return rowPos;
    }

    public byte[][] getMaze() {
        return maze;
    }
}

package mazeMapping;

import javafx.util.Pair;

import java.util.LinkedList;

public class BFS {
    public Pair<Integer, Integer>[][] deletedFields;

    public LinkedList<Pair<Integer, Integer>> searchForNearestFields(MazeController mazeController) {

        LinkedList<Pair<Integer, Integer>> fifo = new LinkedList<>();
        int rows = mazeController.getRows();
        int cols = mazeController.getCols();
        int rowPos = mazeController.getRowPos();
        int colPos = mazeController.getColPos();
        byte[][] maze = mazeController.getMaze();

        byte[][] visitedFields = new byte[rows][cols];
        deletedFields = new Pair[rows][cols];
        LinkedList<Pair<Integer, Integer>> nearestFields = new LinkedList<>();

        fifo.add(new Pair<>(rowPos, colPos));
        int minDistance = Integer.MAX_VALUE;

        while (!fifo.isEmpty()) {

            Pair<Integer, Integer> field = fifo.poll();
            int row = field.getKey();
            int col = field.getValue();

            if (visitedFields[row][col] == 1)
                continue;
            if (mazeController.checkInformationAmount(row, col) > 0) {
                int distance = 0;
                int tempRow = row;
                int tempCol = col;
                while (tempRow != rowPos || tempCol != colPos) {

                    Pair<Integer, Integer> deletedField = deletedFields[tempRow][tempCol];
                    tempRow = deletedField.getKey();
                    tempCol = deletedField.getValue();
                    distance++;
                    if (distance > minDistance) break; // pomyśleć
                }
                if (distance > minDistance) break;

                minDistance = distance;
                nearestFields.add(field);
            }
            for (Direction direction : Direction.values()) {

                int passageRow = row + direction.getRowTranslation();
                int passageCol = col + direction.getColTranslation();
                int fieldRow = passageRow + direction.getRowTranslation();
                int fieldCol = passageCol + direction.getColTranslation();

                if (mazeController.checkIfFieldIsInMaze(passageRow, passageCol) && maze[passageRow][passageCol] == 0) {
                    if (visitedFields[fieldRow][fieldCol] == 0) {
                        fifo.add(new Pair<>(fieldRow, fieldCol));
                        deletedFields[fieldRow][fieldCol] = field;
                    }
                }
            }

            visitedFields[row][col] = 1;
        }
        return nearestFields;
    }

    public LinkedList<Pair<Integer, Integer>> getPathToField(Pair<Integer, Integer> field, int rowPos, int colPos) throws InterruptedException {
        int row = field.getKey();
        int col = field.getValue();
        LinkedList<Pair<Integer, Integer>> path = new LinkedList<>();
        path.add(field);
        while (row != rowPos || col != colPos) {
            Pair<Integer, Integer> deletedField = deletedFields[row][col];
            row = deletedField.getKey();
            col = deletedField.getValue();
            if (row != rowPos || col != colPos) path.add(deletedField);
        }
        return path;
    }
}
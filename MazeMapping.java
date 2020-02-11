package mazeMapping;

import javafx.util.Pair;

import java.util.LinkedList;

public class MazeMapping {
    private MazeController mazeController;
    private BFS bfs;

    public MazeMapping(MazeController mazeController, BFS bfs) {
        this.mazeController = mazeController;
        this.bfs = bfs;
    }

    public byte[][] mapMaze() throws InterruptedException {
        mazeController.createMaze();
        mazeController.setPosition();
        byte[][] maze = mazeController.getMaze();
        int rowPos = mazeController.getRowPos();
        int colPos = mazeController.getColPos();
        maze[rowPos][colPos] = 0;
        while (true) {
            mazeController.updateMaze();

            Pair<Integer, Integer> chosenField = chooseField();
            if (chosenField == null) break;
            goToField(chosenField);

        }
        mazeController.printMaze();


        return maze;
    }


    public Pair<Integer, Integer> chooseField() {
        LinkedList<Pair<Integer, Integer>> nearestFields = bfs.searchForNearestFields(mazeController);
        Pair<Integer, Integer> chosenField = getFieldWithMostInformation(nearestFields);

        return chosenField;
    }

    public Pair<Integer, Integer> getFieldWithMostInformation(LinkedList<Pair<Integer, Integer>> nearestFields) {
        Pair<Integer, Integer> chosenField = null;
        if (nearestFields.size() > 0) {
            byte maxAmount = -1;
            for (Pair<Integer, Integer> field : nearestFields) {
                int row = field.getKey();
                int col = field.getValue();
                byte amount = mazeController.checkInformationAmount(row, col);
                if (amount > maxAmount) {
                    maxAmount = amount;
                    chosenField = field;
                }
            }
        }
        return chosenField;
    }

    public void goToField(Pair<Integer, Integer> nextField) throws InterruptedException {

        LinkedList<Pair<Integer, Integer>> path = bfs.getPathToField(nextField, mazeController.getRowPos(), mazeController.getColPos());
        while (!path.isEmpty()) {
            Pair<Integer, Integer> field = path.pollLast();
            int row = field.getKey();
            int col = field.getValue();

            for (Direction direction : Direction.values()) {
                if (mazeController.getRowPos() + 2 * direction.getRowTranslation() == row && mazeController.getColPos()
                    + 2 * direction.getColTranslation() == col) {
                    mazeController.makeMove(direction);
                    break;
                }
            }
        }
    }

}

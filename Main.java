package mazeMapping;

import java.io.FileNotFoundException;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        BFS bfs = new BFS();

        for (int i = 1; i < 5; i++) {
            ServerCommunicator serverCommunicator = new ServerCommunicator("a6f09791", i);
            MazeController mazeController = new MazeController(serverCommunicator);
            MazeMapping mazeMapping = new MazeMapping(mazeController, bfs);
            serverCommunicator.makeReset();
            mazeMapping.mapMaze();
            System.out.println("Number of moves: " + serverCommunicator.getMovesNumber());
            try {
                serverCommunicator.uploadFile("./upload.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println();
        }


    }
}

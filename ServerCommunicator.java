package mazeMapping;

import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;

public class ServerCommunicator {
    private String UID;
    private int mapId;

    public ServerCommunicator(String UID, int mapId) {
        this.UID = UID;
        this.mapId = mapId;
    }

    public String getRequest(String nameOfRequest) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://tesla.iem.pw.edu.pl:4444/" + UID + "/" + mapId + "/" + nameOfRequest)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    public void postRequest(String nameOfRequest) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://tesla.iem.pw.edu.pl:4444/" + UID + "/" + mapId + "/" + nameOfRequest))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void makeMove(String direction) {
        postRequest("move/" + direction);
    }

    public void makeReset() {
        postRequest("reset");
    }

    public void uploadFile(String filePath) throws FileNotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://tesla.iem.pw.edu.pl:4444/" + UID + "/" + mapId + "/upload"))
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(filePath)))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public Pair<Integer, Integer> getSize() {
        String response = getRequest("size");
        String[] sizeString = response.split("x");
        int columns = Integer.parseInt(sizeString[0]);
        int rows = Integer.parseInt(sizeString[1]);
        Pair size = new Pair(columns, rows);
        return size;
    }

    public Pair<Integer, Integer> getStartPosition() {
        String response = getRequest("startposition");
        String[] startPositionString = response.split(",");
        int columnId = Integer.parseInt(startPositionString[0]);
        int rowId = Integer.parseInt(startPositionString[1]);
        Pair startPosition = new Pair(columnId, rowId);
        return startPosition;
    }

    public int getMovesNumber() {
        String response = getRequest("moves");
        int movesNumber = Integer.parseInt(response);
        return movesNumber;
    }

    public void setPossibilities() {
        String response = getRequest("possibilities");
        for (Direction d : Direction.values()) {
            char c = response.charAt(d.getCellId());
            if (c == '+') d.setPossibility((byte) 1);
            else if (c == '0') d.setPossibility((byte) 0);
        }
    }

}

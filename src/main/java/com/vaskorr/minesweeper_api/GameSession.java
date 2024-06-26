package com.vaskorr.minesweeper_api;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Random;

public class GameSession {
    private final String game_id;
    private final int width;
    private final int height;
    private final int mines_count;
    private boolean completed;
    private boolean win;
    private String error;
    private final List<List<Integer>> field;
    private final List<List<Boolean>> opened_cells;

    // Constructor
    public GameSession(int width, int height, int mines_count) {
        this.error = "";
        if (width > 30 || height > 30 || mines_count > width * height - 1){
            error = "Invalid game parameters. \nheight <= 30\nwidth <= 30\nmines count <= height * width - 1";
        }
        this.game_id = UUID.randomUUID().toString();
        this.width = width;
        this.height = height;
        this.mines_count = mines_count;
        this.completed = false;
        this.win = false;
        this.field = new ArrayList<>();
        this.opened_cells = new ArrayList<>();
        fillField();
    }


    // Private methods
    private void fillField(){
        //basic fill
        for (int i = 0; i < height; i++){
            List<Integer> row = new ArrayList<>();
            List<Boolean> row_opened = new ArrayList<>();
            for (int j = 0; j < width; j++){
                row.add(0);
                row_opened.add(false);
            }
            field.add(row);
            opened_cells.add(row_opened);
        }


        // Place mines randomly
        Random random = new Random();
        for (int i = 0; i < mines_count; i++) {
            int row = random.nextInt(height);
            int col = random.nextInt(width);
            while (field.get(row).get(col) == 9) {
                // If there's already a mine in this location, find another random location
                row = random.nextInt(height);
                col = random.nextInt(width);
            }
            field.get(row).set(col, 9); // Set mine
        }

        // Update numbers around mines
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (field.get(i).get(j) == 9) {
                    updateNumbers(i, j);
                }
            }
        }
    }

    private void updateNumbers(int row, int col) {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];

            if (isValidCell(newRow, newCol) && field.get(newRow).get(newCol) != 9) {
                field.get(newRow).set(newCol, field.get(newRow).get(newCol) + 1);
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < field.size() && col >= 0 && col < field.get(0).size();
    }

    private List<List<Character>> getCharacterField() {
        List<List<Character>> charField = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            List<Character> row = new ArrayList<>();
            for (int j = 0; j < field.get(i).size(); j++) {
                if (completed){
                    if (field.get(i).get(j) == 9){
                        if (win){
                            row.add('M');
                        }else{
                            row.add('X');
                        }
                    }else {
                        row.add((char)(field.get(i).get(j)+'0'));
                    }
                }else{
                    if (opened_cells.get(i).get(j)) {
                        row.add((char)(field.get(i).get(j)+'0'));
                    } else {
                        row.add(' ');
                    }
                }
            }
            charField.add(row);
        }
        return charField;
    }

    private boolean isGameWin(){
        int k = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (opened_cells.get(i).get(j)){
                    k+=1;
                }
            }
        }
        return k == height * width - mines_count;
    }

    private void openAdjacentCells(int row, int col) {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];
            if (isValidCell(newRow, newCol) && !opened_cells.get(newRow).get(newCol)) {
                opened_cells.get(newRow).set(newCol, true);
                if (field.get(newRow).get(newCol) == 0) {
                    openAdjacentCells(newRow, newCol);
                }
            }
        }
    }


    // Public methods
    public Schemas.GameInfoResponse getGameState(){
        Schemas.GameInfoResponse gameInfo = new Schemas.GameInfoResponse(game_id,
                width,
                height,
                mines_count,
                completed,
                getCharacterField());
        return gameInfo;
    }

    public String getGame_id() {
        return game_id;
    }

    public Schemas.ErrorResponse isRequestInvalid(){
        if (error.isEmpty()){
            return null;
        }
        Schemas.ErrorResponse errorResponse = new Schemas.ErrorResponse(error);
        error = "";
        return errorResponse;
    }

    public void makeTurn(int row, int col){
        if (completed){
            // You can't do your turn after the end of the game
            error = "The game is already over";
            return;
        }
        if (opened_cells.get(row).get(col)){
            // You can't open opened cell
            error = "You already open this cell";
            return;
        }
        if (!isValidCell(row, col)) {
            // Wrong cell coords
            error = "Invalid cell: wrong coordinates";
            return;
        }
        opened_cells.get(row).set(col, true);
        if (field.get(row).get(col) == 9){
            // It's mine: game over
            completed = true;
            return;
        }
        if (field.get(row).get(col) == 0) {
            // Open adjacent cells if current cell is empty
            openAdjacentCells(row, col);
        }
        if (isGameWin()){
            // You win!
            completed = true;
            win = true;
        }
    }
}

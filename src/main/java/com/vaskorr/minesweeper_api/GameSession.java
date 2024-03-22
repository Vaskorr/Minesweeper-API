package com.vaskorr.minesweeper_api;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameSession {
    private String game_id;
    private int width;
    private int height;
    private int mines_count;
    private boolean completed;
    private boolean win;
    private List<List<Integer>> field;
    private List<List<Boolean>> opened_cells;

    public GameSession(int width, int height, int mines_count) {
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
                    updateNumbers(field, i, j);
                }
            }
        }
    }

    private static void updateNumbers(List<List<Integer>> board, int row, int col) {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];

            if (isValidCell(board, newRow, newCol) && board.get(newRow).get(newCol) != 9) {
                board.get(newRow).set(newCol, board.get(newRow).get(newCol) + 1);
            }
        }
    }

    private static boolean isValidCell(List<List<Integer>> board, int row, int col) {
        return row >= 0 && row < board.size() && col >= 0 && col < board.get(0).size();
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

    public String getGameState(){
        Schemas.GameInfoResponse gameInfo = new Schemas.GameInfoResponse(game_id,
                width,
                height,
                mines_count,
                completed,
                getCharacterField());
        JSONObject gameState = new JSONObject(gameInfo);
        return gameState.toString();
    }
}

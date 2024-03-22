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
    private List<List<Character>> field;

    public GameSession(int width, int height, int mines_count) {
        this.game_id = UUID.randomUUID().toString();
        this.width = width;
        this.height = height;
        this.mines_count = mines_count;
        this.completed = false;
        this.field = new ArrayList<>();
        fillField();
    }

    private void fillField(){
        //basic fill
        for (int i = 0; i < height; i++){
            List<Character> string = new ArrayList<>();
            for (int j = 0; j < width; j++){
                string.add(' ');
            }
            this.field.add(string);
        }
    }

    public String getGameState(){
        Schemas.GameInfoResponse gameInfo = new Schemas.GameInfoResponse(game_id,
                width,
                height,
                mines_count,
                completed,
                field);
        JSONObject gameState = new JSONObject(gameInfo);
        return gameState.toString();
    }
}

package com.vaskorr.minesweeper_api;

import java.util.List;

public class Schemas {
    public static class NewGameRequest{
        private int width;
        private int height;
        private int mines_count;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getMines_count() {
            return mines_count;
        }

        public void setMines_count(int mines_count) {
            this.mines_count = mines_count;
        }
    }

    public static class GameTurnRequest{
        private String game_id;
        private int col;
        private int row;

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }
    }

    public static class GameInfoResponse{
        private String game_id;
        private int width;
        private int height;
        private int mines_count;
        private boolean completed;
        private List<List<Character>> field;

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getMines_count() {
            return mines_count;
        }

        public void setMines_count(int mines_count) {
            this.mines_count = mines_count;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public List<List<Character>> getField() {
            return field;
        }

        public void setField(List<List<Character>> field) {
            this.field = field;
        }
    }

    public static class ErrorResponse{
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}

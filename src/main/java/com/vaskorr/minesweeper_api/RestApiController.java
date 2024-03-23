package com.vaskorr.minesweeper_api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class RestApiController {

    private HashMap<String, GameSession> games = new HashMap<>();

    @PostMapping(path = "new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Schemas.GameInfoResponse> newGame(@RequestBody Schemas.NewGameRequest newGameRequest){
        GameSession game = new GameSession(newGameRequest.getWidth(), newGameRequest.getHeight(), newGameRequest.getMines_count());
        games.put(game.getGame_id(), game);
        return new ResponseEntity<>(game.getGameState(), HttpStatus.OK);
    }
}

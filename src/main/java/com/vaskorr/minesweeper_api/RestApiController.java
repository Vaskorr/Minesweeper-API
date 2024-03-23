package com.vaskorr.minesweeper_api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
public class RestApiController {

    private HashMap<String, GameSession> games = new HashMap<>();

    @PostMapping(path = "api/new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Schemas.GameInfoResponse> newGame(@RequestBody Schemas.NewGameRequest newGameRequest){
        GameSession game = new GameSession(newGameRequest.getWidth(), newGameRequest.getHeight(), newGameRequest.getMines_count());
        games.put(game.getGame_id(), game);
        return new ResponseEntity<>(game.getGameState(), HttpStatus.OK);
    }

    @PostMapping(path = "api/turn",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Schemas.GameInfoResponse> newTurn(@RequestBody Schemas.GameTurnRequest gameTurnRequest){
        games.get(gameTurnRequest.getGame_id()).makeTurn(gameTurnRequest.getRow(), gameTurnRequest.getCol());
        return new ResponseEntity<>(games.get(gameTurnRequest.getGame_id()).getGameState(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.OPTIONS, path = "api/new")
    public ResponseEntity<String> optionsRequest(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

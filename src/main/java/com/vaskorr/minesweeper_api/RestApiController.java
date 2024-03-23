package com.vaskorr.minesweeper_api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@CrossOrigin
@RestController
public class RestApiController {

    // List with active games
    private final HashMap<String, GameSession> games = new HashMap<>();

    @PostMapping(path = "api/new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newGame(@RequestBody Schemas.NewGameRequest newGameRequest){
        GameSession game = new GameSession(newGameRequest.getWidth(), newGameRequest.getHeight(), newGameRequest.getMines_count());

        // Error cathing
        Schemas.ErrorResponse errors = game.isRequestInvalid();
        if (errors != null){
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // Add valid game
        games.put(game.getGame_id(), game);
        return new ResponseEntity<>(game.getGameState(), HttpStatus.OK);
    }

    @PostMapping(path = "api/turn",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newTurn(@RequestBody Schemas.GameTurnRequest gameTurnRequest){
        // If game exists
        if (games.get(gameTurnRequest.getGame_id()) != null){
            // Make turn
            games.get(gameTurnRequest.getGame_id()).makeTurn(gameTurnRequest.getRow(), gameTurnRequest.getCol());
        }else {
            return new ResponseEntity<>(new Schemas.ErrorResponse("The game is already over"), HttpStatus.BAD_REQUEST);
        }

        // Error cathing
        Schemas.ErrorResponse errors = games.get(gameTurnRequest.getGame_id()).isRequestInvalid();
        if (errors != null){
            if (Objects.equals(errors.getError(), "The game is already over")){
                // Remove game from list
                games.remove(gameTurnRequest.getGame_id());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(games.get(gameTurnRequest.getGame_id()).getGameState(), HttpStatus.OK);
    }
}

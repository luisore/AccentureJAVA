package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Player getAuthenticatedPlayer(Authentication authentication){
        return isGuest(authentication) ? null : playerRepository.findByemail(authentication.getName());
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        dto.put("player", isGuest(authentication) ? "Guest"
                : playerRepository
                .findByemail(authentication.getName()).makePlayerDTO());

        dto.put("games", gameRepository.findAll()
                .stream()
                .map(Game::makeGameDTO)
                .collect(Collectors.toList()));

        return dto;
    }


    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No esta autorizado a crear un Juego"), HttpStatus.UNAUTHORIZED);
        }

        Game newGame = new Game(new Date());
        Player player = getAuthenticatedPlayer(authentication);
        GamePlayer newGamePlayer = new GamePlayer(new Date(),newGame,player);

        gameRepository.save(newGame);
        gamePlayerRepository.save(newGamePlayer);

        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
    }


    @RequestMapping(path = "/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long nn,Authentication authentication) {

        Player loggedPlayer = getAuthenticatedPlayer(authentication);
        if (loggedPlayer == null) {
            return new ResponseEntity<>(makeMap("error", "No esta autorizado a entrar a un Juego"), HttpStatus.UNAUTHORIZED);
        }

        Optional<Game> gameToJoin = gameRepository.findById(nn);
        if(!gameToJoin.isPresent()){
            return new ResponseEntity<>(makeMap("error", "No existe ese Juego"), HttpStatus.FORBIDDEN);
        }
        if(gameToJoin.get().isFull()){
            return new ResponseEntity<>(makeMap("error", "El Juego esta lleno"), HttpStatus.FORBIDDEN);
        }
        if(gameToJoin.get().contieneJugador(loggedPlayer)){
            return new ResponseEntity<>(makeMap("error", "Ya entraste al juego"), HttpStatus.FORBIDDEN);
        }

        GamePlayer newGamePlayer = new GamePlayer(new Date(),gameToJoin.get(),loggedPlayer);

        gamePlayerRepository.save(newGamePlayer);

        return new ResponseEntity<>(makeMap("gpid",newGamePlayer.getId()), HttpStatus.CREATED);
    }


    @RequestMapping(value="/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(Authentication authentication,
                                           @PathVariable long gamePlayerId,
                                           @RequestBody List<Ship> ships) {

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        Player loggedPlayer = getAuthenticatedPlayer(authentication);

        if(loggedPlayer == null){
            return new ResponseEntity<>(makeMap("error", "No hay jugador Loggeado"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer == null){
            return new ResponseEntity<>(makeMap("error", "No existe un gamePlayer con ese Id"), HttpStatus.UNAUTHORIZED);
        }
        if(!loggedPlayer.equals(gamePlayer.getPlayer())){
            return new ResponseEntity<>(makeMap("error", "El usuario loggeado no es el gamePlayer que el Id referencia"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.getShips().size() >= 5 || ships.size() + gamePlayer.getShips().size() >= 5){
            return new ResponseEntity<>(makeMap("error", "El usuario ya tiene ships puestos"), HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping("/players")
    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    @RequestMapping("/game_view/{nn}")
    public Object game_view(@PathVariable Long nn, Authentication authentication) {

        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).get();
        Player player = getAuthenticatedPlayer(authentication);

        boolean permisosSesion = gamePlayer
                .getPlayer()
                .equals(player);

        return permisosSesion ?
                gamePlayer
                        .getGame()
                        .makeGameViewDTO(nn)
                :
                new ResponseEntity<>("No tiene permisos para ver el game_view de otro usuario", HttpStatus.FORBIDDEN);

    }

    @RequestMapping("/leaderBoard")
    public List<Object> getLeaderBoard() {
        return playerRepository.findAll()
                .stream()
                .map(Player::makeLeaderBoardDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email,
            @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByemail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

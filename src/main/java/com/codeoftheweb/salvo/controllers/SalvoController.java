package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @RequestMapping("/games")
    public List<Object> getGames() {
        return gameRepository.findAll()
                                .stream()
                                .map(Game::makeGameDTO)
                                .collect(Collectors.toList());
    }

    @RequestMapping("/players")
    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    @RequestMapping("/game_view/{nn}")
    public Object findOwner(@PathVariable Long nn) {
        return gamePlayerRepository.findById(nn).get().getGame().makeGameViewDTO(nn);
    }

    @RequestMapping("/leaderBoard")
    public List<Object> getLeaderBoard(){
        return playerRepository.findAll()
                                .stream()
                                .map(Player::makeLeaderBoardDTO)
                                .collect(Collectors.toList());
    }
}

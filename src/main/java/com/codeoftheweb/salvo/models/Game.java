package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.PersistentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
public class Game extends PersistentEntity {

    private Date creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> scores;

    public Game(){
    }

    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }
    public Date getCreationDate(){
        return this.creationDate;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    @JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(GamePlayer::getPlayer).collect(toList());
    }


    public Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers",  this.getGamePlayers()
                .stream()
                .map(GamePlayer::makeGamePlayerDTO)
                .collect(Collectors.toList())
        );
        dto.put("scores", this.getScores()
                .stream()
                .map(Score::makeScoreDTO)
                .collect(Collectors.toList())
        );
        return dto;
    }

    public Map<String, Object> makeGameViewDTO(Long nn) {
        Map<String, Object> dto = this.makeGameDTO();
        dto.put("ships", this.makeShipsDTO(nn));
        dto.put("salvoes", this.makeSalvoesDTO());
        return dto;
    }

    private List<Object> makeSalvoesDTO(){
        return this.getGamePlayers()
                .stream()
                .flatMap(gamePlayer -> gamePlayer.getSalvoes().stream())
                .map(Salvo::makeSalvoDTO)
                .collect(toList());
    }

    private List<Object> makeShipsDTO(Long nn){
        return this.getGamePlayers()
                .stream()
                .filter(gamePlayer -> gamePlayer.getId() == nn)
                .flatMap(gamePlayer -> gamePlayer.getShips().stream())
                .map(Ship::makeShipDTO)
                .collect(toList());
    }
}

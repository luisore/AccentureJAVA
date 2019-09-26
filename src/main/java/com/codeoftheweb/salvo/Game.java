package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    @JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(gamePlayer -> gamePlayer.getPlayer()).collect(toList());
    }


    public Game(){
        //this.creationDate = new Date();
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }
    public Date getCreationDate(){
        return this.creationDate;
    }

    public long getId() {
        return id;
    }

    public Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers",  this.getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList())
        );
        return dto;
    }

    public Map<String, Object> makeGameViewDTO(Long nn) {
        Map<String, Object> dto = this.makeGameDTO();
        dto.put("ships", this.getShips(nn));
        return dto;
    }

    public List<Object> getShips(Long nn){
        return this.getGamePlayers()
                                    .stream()
                                    .filter(gamePlayer -> gamePlayer.getId() == nn)
                                    .flatMap(gamePlayer -> gamePlayer.getShips().stream())
                                    .map(ship -> ship.makeShipDTO())
                                    .collect(toList());
    }

}

package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.PersistentEntity;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo extends PersistentEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turn;

    @ElementCollection
    @CollectionTable(name = "salvo_locations", joinColumns = @JoinColumn(name = "salvo_id"))
    @Column(name = "salvo_location")
    private Set<String> salvoLocations;

    public Salvo(){}

    public Salvo(int turn, GamePlayer gamePlayer, Set<String> locations) {
        this.turn = turn;
        this.gamePlayer = gamePlayer;
        this.salvoLocations = locations;
    }

    @JsonIgnore
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getTurn() {
        return turn;
    }
    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Set<String> getSalvoLocations() {
        return salvoLocations;
    }
    public void setSalvoLocations(Set<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public void addSalvoLocations(Collection<String> newLocations){
        this.salvoLocations.addAll(newLocations);
    }

    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.getTurn());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations", this.getSalvoLocations().stream().sorted());
        return dto;
    }

    public Map<String, Object> makeTurnAnalysisDTO(Set<Ship> ships){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.getTurn());

        Set<String>          hitLocations = new HashSet<String>();
        Map<String, Integer> damages_dto  = inicializarDamagesDTO();
        Integer              missed       = 0;

        for (String salvoLocation : this.getSalvoLocations()){
            for (Ship ship : ships){
                for(String shipLocation : ship.getLocations()){
                    if (salvoLocation.equals(shipLocation)){
                        hitLocations.add(salvoLocation);
                        damages_dto.put(ship.getNormalizedType() + "Hits", damages_dto.get(ship.getNormalizedType() + "Hits") + 1);
                    }
                }
            }
            if(!hitLocations.contains(salvoLocation)){
                missed++;
            }
        }

        dto.put("hitLocations", hitLocations);
        dto.put("damages",      damages_dto);
        dto.put("missed",       missed);

        return dto;
    }

    private Map<String, Integer> inicializarDamagesDTO() {
        Map<String, Integer> damages_dto = new LinkedHashMap<String, Integer>();
        damages_dto.put("carrierHits",0);
        damages_dto.put("battleshipHits",0);
        damages_dto.put("submarineHits",0);
        damages_dto.put("destroyerHits",0);
        damages_dto.put("patrolboatHits",0);
        damages_dto.put("carrier",0);
        damages_dto.put("battleship",0);
        damages_dto.put("submarine",0);
        damages_dto.put("destroyer",0);
        damages_dto.put("patrolboat",0);
        return damages_dto;
    }
}

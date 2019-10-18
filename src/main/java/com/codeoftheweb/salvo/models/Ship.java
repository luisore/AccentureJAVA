package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.PersistentEntity;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Ship extends PersistentEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private String type;

    @ElementCollection
    @CollectionTable(name = "ship_locations", joinColumns = @JoinColumn(name = "ship_id"))
    @Column(name = "ship_location")
    private Set<String> locations;

    private Integer Hits;

    public Ship(){this.Hits = 0;}
    public Ship(String type,Set<String> shipLocations){
        this.type = type;
        this.locations = shipLocations;
        this.Hits = 0;
    }
    public Ship(GamePlayer gamePlayer,String type,Set<String> shipLocations){
        this.gamePlayer = gamePlayer;
        this.type = type;
        this.locations = shipLocations;
        this.Hits = 0;
    }

    @JsonIgnore
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getNormalizedType(){
        return this.getType().replaceAll(" ","").toLowerCase();
    }

    public Set<String> getLocations() {
        return locations;
    }
    public void setLocations(Set<String> shipLocations) {
        this.locations = shipLocations;
    }

    public Map<String, Object> makeShipDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", this.getType());
        dto.put("locations", this.getLocations().stream().sorted());
        return dto;
    }

    public boolean fueDestruidoPor(Set<Salvo> salvoes) {
        return salvoes.stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList()).containsAll(this.getLocations());
    }
}

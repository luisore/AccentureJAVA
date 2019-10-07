package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.PersistentEntity;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship extends PersistentEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private String type;

    @ElementCollection
    @CollectionTable(name = "ship_locations", joinColumns = @JoinColumn(name = "ship_id"))
    @Column(name = "ship_location")
    private Set<String> shipLocations;

    public Ship(){}
    public Ship(String type,Set<String> locations){
        this.type = type;
        this.shipLocations = locations;
    }
    public Ship(GamePlayer gamePlayer,String type,Set<String> locations){
        this.gamePlayer = gamePlayer;
        this.type = type;
        this.shipLocations = locations;
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

    public Set<String> getLocations() {
        return shipLocations;
    }

    public Map<String, Object> makeShipDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", this.getType());
        dto.put("locations", this.getLocations());
        return dto;
    }
}

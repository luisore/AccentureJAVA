package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private String type;


    @ElementCollection
    @CollectionTable(name = "ship_locations", joinColumns = @JoinColumn(name = "ship_id"))
    @Column(name = "ship_location")
    private Set<String> locations;

    public Ship(){}
    public Ship(String type,Set<String> locations){
        this.type = type;
        this.locations = locations;
    }
    public Ship(GamePlayer gamePlayer,String type,Set<String> locations){
        //gamePlayer.addShip(this);

        this.gamePlayer = gamePlayer;
        this.type = type;
        this.locations = locations;
    }

    @JsonIgnore
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Set<String> getLocations() {
        return locations;
    }

    public Map<String, Object> makeShipDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", this.getType());
        dto.put("locations", this.getLocations());
        return dto;
    }
}

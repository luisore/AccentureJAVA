package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    private Date joinDate;

    public GamePlayer(){}

    public GamePlayer(Date joinDate,Game game, Player player) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public void setJoinDate(Date joinDate){
        this.joinDate = joinDate;
    }
    public Date getJoinDate(){
        return this.joinDate;
    }

    public long getId() {
        return id;
    }
}

package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.PersistentEntity;
import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score extends PersistentEntity {

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    private Double score;

    private Date finishDate;

    public Score(){}

    public Score( Game game, Player player, Double score, Date finishDate) {
        this.player = player;
        this.game = game;
        this.score = score;
        this.finishDate = finishDate;
    }

    public Double getScoreValue() {
        return score;
    }
    @JsonIgnore
    public Player getPlayer() {
        return player;
    }
    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public Boolean esDeGame(Game game){
        return this.getGame().equals(game);
    }

    public Date getFinishDate() {
        return finishDate;
    }


    public Map<String, Object> makeScoreDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", this.getPlayer().getId());
        dto.put("score", this.getScoreValue());
        dto.put("finishDate", this.getFinishDate());
        return dto;
    }

    public boolean isAWin() {
        return this.getScoreValue()==1.0;
    }
    public boolean isALoss() {
        return this.getScoreValue()==0.0;
    }
    public boolean isATie() {
        return this.getScoreValue()==0.5;
    }

    public String getScoreState(){
        if (isALoss()){return "LOST";}
        if (isAWin()){return "WON";}
        if (isATie()){return "TIE";}
        return "";
    }
}

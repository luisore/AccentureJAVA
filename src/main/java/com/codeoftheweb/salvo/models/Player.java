package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.PersistentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Player extends PersistentEntity {

    private String userName;
    private String email;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<Score> scores;


    public Player(){}
    public Player(String email, String password){
        this.email = email;
        this.password = password;
    }
    public Player(String userName, String email, String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
    }


    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }
    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(gamePlayer -> gamePlayer.getGame()).collect(toList());
    }

    public String getEmail() {
        return email;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Optional<Score> getScore(Game game){
        return this.getScores().stream()
                .filter(score -> score.esDeGame(game))
                .findFirst();
    }



    public Map<String, Object> makePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("email", this.getEmail());
        return dto;
    }

    public Map<String, Object> makeLeaderBoardDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        dto.put("id", this.getId());
        dto.put("email", this.getEmail());

            Map<String, Object> dto_score = new LinkedHashMap<String, Object>();
            dto_score.put("total", this.getTotalScore());
            dto_score.put("won", this.getTotalWins());
            dto_score.put("lost", this.getTotalLosses());
            dto_score.put("tied", this.getTotalties());

        dto.put("score", dto_score);
        return dto;
    }

    private Long getTotalties() {
        return this.getScores().stream()
                .filter(Score::isATie)
                .count();
    }

    private Long getTotalLosses() {
        return this.getScores().stream()
                .filter(Score::isALoss)
                .count();
    }

    private Long getTotalWins() {
        return this.getScores().stream()
                .filter(Score::isAWin)
                .count();
    }

    private Double getTotalScore() {
        return this.getScores().stream()
                .mapToDouble(Score::getScoreValue)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(userName, player.userName) &&
                email.equals(player.email) &&
                Objects.equals(password, player.password) &&
                Objects.equals(gamePlayers, player.gamePlayers) &&
                Objects.equals(scores, player.scores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, email, password, gamePlayers, scores);
    }
}

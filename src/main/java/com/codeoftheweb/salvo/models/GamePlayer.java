package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.PersistentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer extends PersistentEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvoes;

    private Date joinDate;

    public GamePlayer(){}

    public GamePlayer(Player player){
        this.player = player;
    }

    public GamePlayer(Date joinDate,Game game, Player player) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
        this.ships = new HashSet<>();
        this.salvoes = new HashSet<>();
    }

    @JsonIgnore
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

    public Date getJoinDate(){
        return this.joinDate;
    }
    public void setJoinDate(Date joinDate){
        this.joinDate = joinDate;
    }

    public Set<Ship> getShips() {
        return this.ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    public Boolean tieneBarcosPuestos(){
        return (!this.getShips().isEmpty());
    }
    private boolean tieneOponente() {
        return this.getOponente().isPresent();
    }
    @JsonIgnore
    public Optional<GamePlayer> getOponente(){
        return this.getGame().getOpponentGamePlayer(this);
    }

    public boolean jugoTurno(int turno){
        return this.getSalvoes().stream().anyMatch(salvo -> salvo.getTurn() == turno);
    }

    public Optional<Score> getScore(){
        return this.getPlayer().getScore(this.getGame());
    }

    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
        dto.put("joinDate",this.getJoinDate());
        return dto;
    }

    @JsonIgnore
    public String getGameState() {
        if (!this.tieneBarcosPuestos()){
            return "PLACESHIPS";
        }
        if(!this.tieneOponente() || !this.getOponente().get().tieneBarcosPuestos()){
            return "WAITINGFOROPP";
        }
        if(!this.getGame().getScores().isEmpty()){
            return this.getScore().get().getScoreState();
        }
        if(this.puedeJugarTurno()){
            return "PLAY";
        }
        if(this.esperaTurnoOponente()){
            return "WAIT";
        }
        return "";
    }

    private boolean esperaTurnoOponente() {
        return this.getSalvoes().size() > this.getOponente().get().getSalvoes().size();
    }

    public boolean puedeJugarTurno() {
        return this.getSalvoes().size() <= this.getOponente().get().getSalvoes().size();
    }

    public boolean perdioSusBarcos(){
        return this.getShips().stream().allMatch(ship -> ship.fueDestruidoPor(this.getOponente().get().getSalvoes()));
    }

    public int getNumeroDeTurnoActual(){
        return this.getSalvoes().size();
    }
}

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

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Game() {
    }

    public Game(Date date) {
        this.creationDate = date;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayer.setJoinDate(new Date());
        gamePlayers.add(gamePlayer);
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    private GamePlayer getGamePlayer(Long gamePlayerId) {
        return this.getGamePlayers().stream()
                .filter(gamePlayer -> gamePlayer.getId() == gamePlayerId)
                .findFirst().get();
    }

    public Optional<GamePlayer> getOpponentGamePlayer(GamePlayer gamePlayer) {
        return this.getGamePlayers().stream()
                .filter(gp -> !gp.equals(gamePlayer))
                .findFirst();
    }

    private Optional<GamePlayer> getOpponentGamePlayer(Long gamePlayerId) {
        return this.getOpponentGamePlayer(getGamePlayer(gamePlayerId));
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void addScore(Score score) {
        this.scores.add(score);
    }

    private void createAndAddScore(Player player, Double scoreValue) {
        Score score = new Score(this, player, scoreValue, new Date());

        player.addScore(score);
        this.addScore(score);
    }

    @JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(GamePlayer::getPlayer).collect(toList());
    }

    @JsonIgnore
    public List<Salvo> getTotalSalvoes() {
        return this.getGamePlayers()
                .stream()
                .flatMap(gamePlayer -> gamePlayer.getSalvoes().stream())
                .collect(Collectors.toList());
    }

    public boolean isFull() {
        return this.getGamePlayers().size() >= 2;
    }

    public boolean contieneJugador(Player jugadorBuscado) {
        return this.getPlayers().stream().anyMatch(player -> player.equals(jugadorBuscado));
    }

    public Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers", makeGamePlayersDTO());
        dto.put("scores", makeScoresDTO());
        return dto;
    }

    private List<Map<String, Object>> makeScoresDTO() {
        return this.getScores()
                .stream()
                .map(Score::makeScoreDTO)
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> makeGamePlayersDTO() {
        return this.getGamePlayers()
                .stream()
                .map(GamePlayer::makeGamePlayerDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Object> makeGameViewDTO(Long gamePlayerId) {
        GamePlayer gamePlayer = this.getGamePlayer(gamePlayerId);

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gameState", gamePlayer.getGameState());
        dto.put("gamePlayers", makeGamePlayersDTO());
        dto.put("ships", this.makeShipsDTO(gamePlayer));
        dto.put("salvoes", this.makeSalvoesDTO());

        Map<String, Object> totalHitsDTO = new LinkedHashMap<>();
        totalHitsDTO.put("self", getHitsDTOList(getOpponentGamePlayer(gamePlayer)));
        totalHitsDTO.put("opponent", getHitsDTOList(gamePlayer));

        dto.put("hits", totalHitsDTO);
        return dto;
    }

    private List<Map<String, Object>> getHitsDTOList(Optional<GamePlayer> gamePlayer) {
        if (!gamePlayer.isPresent()) {
            return new LinkedList<>();
        }
        return getHitsDTOList(gamePlayer.get());
    }

    private List<Map<String, Object>> getHitsDTOList(GamePlayer gamePlayer) {
        //si no tengo oponente, si mi oponente no tiene barcos, si no tengo salvos, no hagas el analisis
        if (!this.getOpponentGamePlayer(gamePlayer).isPresent()
                || this.getOpponentGamePlayer(gamePlayer).get().getShips().isEmpty()
                || gamePlayer.getSalvoes().isEmpty()) {
            return new LinkedList<>();
        }

        Set<Ship> opponentShips = this.getOpponentGamePlayer(gamePlayer).get().getShips();
        List<Map<String, Object>> turnAnalysisDTOList = gamePlayer
                .getSalvoes()
                .stream()
                .sorted(Comparator.comparing(Salvo::getTurn))
                .map(salvo -> salvo.makeTurnAnalysisDTO(opponentShips))
                .collect(Collectors.toList());
        //Logica para insertar informacion de los Hits agregada
        Set<String> opponentShipTypes = opponentShips.stream().map(Ship::getNormalizedType).collect(Collectors.toSet());
        Map<String, Integer> damagesAux = new LinkedHashMap<>();
        for (Map<String, Object> turnAnalysis : turnAnalysisDTOList) {
            Map<String, Integer> turnAnalysisDamages = (Map<String, Integer>) turnAnalysis.get("damages");
            for (String shipType : opponentShipTypes) {
                damagesAux.put(shipType, (damagesAux.get(shipType) != null ? damagesAux.get(shipType) : 0)
                        + turnAnalysisDamages.get(shipType + "Hits"));
            }
            turnAnalysisDamages.putAll(damagesAux);
        }
        return turnAnalysisDTOList;
    }

    private List<Object> makeSalvoesDTO() {
        return this.getTotalSalvoes()
                .stream()
                .sorted(Comparator.comparing(Salvo::getTurn))
                .map(Salvo::makeSalvoDTO)
                .collect(toList());
    }

    private List<Object> makeShipsDTO(GamePlayer gamePlayer) {
        return gamePlayer
                .getShips()
                .stream()
                .map(Ship::makeShipDTO)
                .collect(toList());
    }

    public boolean isOver() {
        List<GamePlayer> gamePlayersSinBarcos = this.getGamePlayers().stream().filter(GamePlayer::perdioSusBarcos).collect(Collectors.toList());
        return gamePlayersSinBarcos.size() > 0;
    }

    public Game finishGame() {
        if (this.isOver()) {
            List<GamePlayer> gamePlayersSinBarcos = this.getGamePlayers().stream().filter(GamePlayer::perdioSusBarcos).collect(Collectors.toList());

            if (gamePlayersSinBarcos.size() == 2 /*empataron ambos jugadores*/) {
                gamePlayersSinBarcos.forEach(gamePlayer ->
                        createAndAddScore(gamePlayer.getPlayer(), 0.5)
                );

            } else {
                GamePlayer gamePlayerPerdedor = gamePlayersSinBarcos.get(0);

                Player playerPerdedor = gamePlayerPerdedor.getPlayer();
                Player playerGanador = gamePlayerPerdedor.getOponente().get().getPlayer();

                createAndAddScore(playerPerdedor, 0.0);
                createAndAddScore(playerGanador, 1.0);
            }
        }
        return this;
    }

    public void jugarSalvo(GamePlayer gamePlayer, Salvo salvo) {
        int numeroDeTurnoNuevo = gamePlayer.getNumeroDeTurnoActual() + 1;
        salvo.setTurn(numeroDeTurnoNuevo);
        gamePlayer.addSalvo(salvo);
    }

    public boolean gamePlayersJugaronUnTurno(){
        GamePlayer primerGamePlayer = this.gamePlayers.stream().findFirst().get();
        return primerGamePlayer.getNumeroDeTurnoActual() == primerGamePlayer.getOponente().get().getNumeroDeTurnoActual();
    }

}

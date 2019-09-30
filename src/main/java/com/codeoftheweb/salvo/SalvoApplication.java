package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      SalvoRepository salvoRepository,
                                      ScoreRepository scoreRepository) {
		return (args) -> {
			// save a couple of Players

			Player player1 = new Player("Jack Bauer", "j.bauer@ctu.gov", "24");
			Player player2 = new Player("Chloe O'Brian", "c.obrian@ctu.gov", "42");
			Player player3 = new Player("Kim Bauer", "kim_bauer@gmail.com", "kb");
			Player player4 = new Player("Tony Almeida", "t.almeida@ctu.gov", "mole");
			playerRepository.saveAll(Arrays.asList(player1,player2,player3,player4));


			Game game1 = new Game();
			Game game2 = new Game();
			Game game3 = new Game();
			Game game4 = new Game();
			Game game5 = new Game();
			Game game6 = new Game();
			Game game7 = new Game();
			Game game8 = new Game();

			Date d1 = new Date();
			Date d2 = Date.from(d1.toInstant().plusSeconds(3600));
            Date d3 = Date.from(d1.toInstant().plusSeconds(7200));
            Date d4 = Date.from(d1.toInstant().plusSeconds(10800));
            Date d5 = Date.from(d1.toInstant().plusSeconds(14400));
            Date d6 = Date.from(d1.toInstant().plusSeconds(18000));
            Date d7 = Date.from(d1.toInstant().plusSeconds(21600));
            Date d8 = Date.from(d1.toInstant().plusSeconds(25200));

			game1.setCreationDate(d1);
			game2.setCreationDate(d2);
			game3.setCreationDate(d3);
			game4.setCreationDate(d4);
			game5.setCreationDate(d5);
			game6.setCreationDate(d6);
			game7.setCreationDate(d7);
			game8.setCreationDate(d8);

			gameRepository.saveAll(Arrays.asList(game1,game2,game3,game4,game5,game6,game7,game8));


			GamePlayer gp1 = new GamePlayer(d1,game1,player1);
			GamePlayer gp2 = new GamePlayer(d1,game1,player2);
			GamePlayer gp3 = new GamePlayer(d1,game2,player1);
			GamePlayer gp4 = new GamePlayer(d1,game2,player2);
			GamePlayer gp5 = new GamePlayer(d1,game3,player2);
			GamePlayer gp6 = new GamePlayer(d1,game3,player4);
			GamePlayer gp7 = new GamePlayer(d1,game4,player2);
			GamePlayer gp8 = new GamePlayer(d1,game4,player1);
			GamePlayer gp9 = new GamePlayer(d1,game5,player4);
			GamePlayer gp10 = new GamePlayer(d1,game5,player1);
			GamePlayer gp11 = new GamePlayer(d1,game6,player3);
			GamePlayer gp12 = new GamePlayer(d1,game7,player4);
			GamePlayer gp13 = new GamePlayer(d1,game8,player3);
			GamePlayer gp14 = new GamePlayer(d1,game8,player4);

			gamePlayerRepository.saveAll(Arrays.asList(gp1,gp2,gp3,gp4,gp5,gp6,gp7,gp8,gp9,gp10,gp11,gp12,gp13,gp14));


			Ship s1 = new Ship(gp1, "Destroyer", new HashSet<>(Arrays.asList("H2", "H3", "H4")));
			Ship s2 = new Ship(gp1, "Submarine", new HashSet<>(Arrays.asList("E1", "F1", "G1")));
			Ship s3 = new Ship(gp1, "Patrol Boat", new HashSet<>(Arrays.asList("B4","B5")));
			Ship s4 = new Ship(gp2, "Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")));
			Ship s5 = new Ship(gp2, "Patrol Boat", new HashSet<>(Arrays.asList("F1", "F2")));
			Ship s6 = new Ship(gp3, "Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")));
			Ship s7 = new Ship(gp3, "Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")));
			Ship s8 = new Ship(gp4, "Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")));
			Ship s9 = new Ship(gp4, "Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")));
			Ship s10 = new Ship(gp5, "Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")));
			Ship s11 = new Ship(gp5, "Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")));
			Ship s12 = new Ship(gp6, "Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")));
			Ship s13 = new Ship(gp6, "Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")));
			Ship s14 = new Ship(gp7, "Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")));
			Ship s15 = new Ship(gp7, "Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")));
			Ship s16 = new Ship(gp8, "Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")));
			Ship s17 = new Ship(gp8, "Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")));
			Ship s18 = new Ship(gp9, "Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")));
			Ship s19 = new Ship(gp9, "Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")));
			Ship s20 = new Ship(gp10, "Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")));
			Ship s21 = new Ship(gp10, "Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")));
			Ship s22 = new Ship(gp11, "Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")));
			Ship s23 = new Ship(gp11, "Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")));
			Ship s24 = new Ship(gp13, "Destroyer", new HashSet<>(Arrays.asList("B5", "C5", "D5")));
			Ship s25 = new Ship(gp13, "Patrol Boat", new HashSet<>(Arrays.asList("C6", "C7")));
			Ship s26 = new Ship(gp14, "Submarine", new HashSet<>(Arrays.asList("A2", "A3", "A4")));
			Ship s27 = new Ship(gp14, "Patrol Boat", new HashSet<>(Arrays.asList("G6", "H6")));

			shipRepository.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8,
					s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21,
					s22, s23, s24, s25, s26, s27));



			Salvo sa1 = new Salvo(1, gp1, new HashSet<>(Arrays.asList("B5", "C5", "F1")));
			Salvo sa2 = new Salvo(1, gp2, new HashSet<>(Arrays.asList("B4", "B5", "B6")));
			Salvo sa3 = new Salvo(2, gp1, new HashSet<>(Arrays.asList("F2", "D5")));
			Salvo sa4 = new Salvo(2, gp2, new HashSet<>(Arrays.asList("E1", "H3", "A2")));
			Salvo sa5 = new Salvo(1, gp3, new HashSet<>(Arrays.asList("A2", "A4", "G6")));
			Salvo sa6 = new Salvo(1, gp4, new HashSet<>(Arrays.asList("B5", "D5", "C7")));
			Salvo sa7 = new Salvo(2, gp3, new HashSet<>(Arrays.asList("A3", "H6")));
			Salvo sa8 = new Salvo(2, gp4, new HashSet<>(Arrays.asList("C5", "C6")));
			Salvo sa9 = new Salvo(1, gp5, new HashSet<>(Arrays.asList("G6", "H6", "A4")));
			Salvo sa10 = new Salvo(1, gp6, new HashSet<>(Arrays.asList("H1", "H2", "H3")));
			Salvo sa11 = new Salvo(2, gp5, new HashSet<>(Arrays.asList("A2", "A3", "D8")));
			Salvo sa12 = new Salvo(2, gp6, new HashSet<>(Arrays.asList("E1", "F2", "G3")));
			Salvo sa13 = new Salvo(1, gp7, new HashSet<>(Arrays.asList("A3", "A4", "F7")));
			Salvo sa14 = new Salvo(1, gp8, new HashSet<>(Arrays.asList("B5", "C6", "H1")));
			Salvo sa15 = new Salvo(2, gp7, new HashSet<>(Arrays.asList("A2", "G6", "H6")));
			Salvo sa16 = new Salvo(2, gp8, new HashSet<>(Arrays.asList("C5", "C7", "D5")));
			Salvo sa17 = new Salvo(1, gp9, new HashSet<>(Arrays.asList("A1", "A2", "A3")));
			Salvo sa18 = new Salvo(1, gp10, new HashSet<>(Arrays.asList("B5", "B6", "C7")));
			Salvo sa19 = new Salvo(2, gp9, new HashSet<>(Arrays.asList("G6", "G7", "G8")));
			Salvo sa20 = new Salvo(2, gp10, new HashSet<>(Arrays.asList("C6", "D6", "E6")));
			Salvo sa21 = new Salvo(3, gp10, new HashSet<>(Arrays.asList("H1", "H8")));

			salvoRepository.saveAll(Arrays.asList(sa1, sa2, sa3, sa4, sa5, sa6, sa7, sa8, sa9, sa10,
					sa11, sa12, sa13, sa14, sa15, sa16, sa17, sa18, sa19, sa20, sa21));


            Score sc1 = new Score(game1, player1, 1.0, d2);
            Score sc2 = new Score(game1, player2, 0.0, d2);
            Score sc3 = new Score(game2, player1, 0.5, d3);
            Score sc4 = new Score(game2, player2, 0.5, d3);
            Score sc5 = new Score(game3, player2, 1.0, d4);
            Score sc6 = new Score(game3, player4, 0.0, d4);
            Score sc7 = new Score(game4, player4, 0.5, d5);
            Score sc8 = new Score(game4, player1, 0.5, d5);

            scoreRepository.saveAll(Arrays.asList(sc1, sc2, sc3, sc4, sc5, sc6, sc7, sc8));



        };
	}


}

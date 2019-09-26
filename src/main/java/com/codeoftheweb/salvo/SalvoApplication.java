package com.codeoftheweb.salvo;

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
									  ShipRepository shipRepository) {
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

			Date creationDate = new Date();
			game1.setCreationDate(creationDate);
			game2.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(3600)));
			game3.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(7200)));
			game4.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(10800)));
			game5.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(14400)));
			game6.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(18000)));
			game7.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(21600)));
			game8.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(25200)));
			gameRepository.saveAll(Arrays.asList(game1,game2,game3,game4,game5,game6,game7,game8));


			GamePlayer gp1 = new GamePlayer(creationDate,game1,player1);
			GamePlayer gp2 = new GamePlayer(creationDate,game1,player2);
			GamePlayer gp3 = new GamePlayer(creationDate,game2,player1);
			GamePlayer gp4 = new GamePlayer(creationDate,game2,player2);
			GamePlayer gp5 = new GamePlayer(creationDate,game3,player2);
			GamePlayer gp6 = new GamePlayer(creationDate,game3,player4);
			GamePlayer gp7 = new GamePlayer(creationDate,game4,player2);
			GamePlayer gp8 = new GamePlayer(creationDate,game4,player1);
			GamePlayer gp9 = new GamePlayer(creationDate,game5,player4);
			GamePlayer gp10 = new GamePlayer(creationDate,game5,player1);
			GamePlayer gp11 = new GamePlayer(creationDate,game6,player3);
			GamePlayer gp12 = new GamePlayer(creationDate,game7,player4);
			GamePlayer gp13 = new GamePlayer(creationDate,game8,player3);
			GamePlayer gp14 = new GamePlayer(creationDate,game8,player4);

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


		};
	}


}

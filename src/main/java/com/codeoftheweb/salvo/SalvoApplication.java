package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
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

			Date creationDate = new Date();
			game1.setCreationDate(creationDate);
			game2.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(3600)));
			game3.setCreationDate(Date.from(creationDate.toInstant().plusSeconds(7200)));
			gameRepository.saveAll(Arrays.asList(game1,game2,game3));


			GamePlayer gp1 = new GamePlayer(creationDate,game1,player1);
			GamePlayer gp2 = new GamePlayer(creationDate,game1,player2);
			GamePlayer gp3 = new GamePlayer(creationDate,game2,player3);
			GamePlayer gp4 = new GamePlayer(creationDate,game2,player4);
			//GamePlayer gp5 = new GamePlayer(creationDate,game3,player5);

			gamePlayerRepository.saveAll(Arrays.asList(gp1,gp2,gp3,gp4));

		};
	}


}

package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository) {
		return (args) -> {
			// save a couple of Players
			Player player1 = new Player("Jack Bauer", "j.bauer@ctu.gov", "24");
			Player player2 = new Player("Chloe O'Brian", "c.obrian@ctu.gov", "42");
			Player player3 = new Player("Kim Bauer", "kim_bauer@gmail.com", "kb");
			Player player4 = new Player("Tony Almeida", "t.almeida@ctu.gov", "mole");
			Player player5 = new Player("Michelle Dessler", "michelle_dessler@gmail.com", "pinch");
			repository.saveAll(Arrays.asList(player1,player2,player3,player4,player5));
		};
	}


}

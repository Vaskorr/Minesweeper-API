package com.vaskorr.minesweeper_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class MinesweeperApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinesweeperApiApplication.class, args);
		// TEST GAME
//		GameSession g = new GameSession(10, 10, 7);
//		System.out.println(g.getGameState());
//		g.printField();
//		Scanner scanner = new Scanner(System.in);
//		while (true){
//			int row = scanner.nextInt();
//			int col = scanner.nextInt();
//			g.move(row, col);
//			g.printHiddenField();
//		}
	}

}

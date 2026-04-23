package com.kissene3.main;

import com.kissene3.console.*;
import com.kissene3.game.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CLIMain {
	private final Scanner input = new Scanner(System.in);
	private final Game game;
	private boolean running = true;
	
	private void init() {
		DifficultyRegistry.init();
		
		System.out.println(ConsoleColor.GREEN_BRIGHT + "Welcome to Minesweeper\n" + ConsoleColor.RESET);
		System.out.flush();
		
		Difficulty difficulty = null;
		List<String> options = new ArrayList<>(DifficultyRegistry.getAvailableNames());
		Collections.sort(options);
		
		while (difficulty == null) {
			for (int i = 0; i < options.size(); i++) {
				System.out.println(ConsoleColor.YELLOW.toString() + (i + 1) + ". " + capitalize(options.get(i)) + ConsoleColor.RESET);
			}
			System.out.println(ConsoleColor.YELLOW.toString() + (options.size() + 1) + ". Custom" + ConsoleColor.RESET);
			
			System.out.print("Enter the number: ");
			System.out.flush();
			
			String in = input.nextLine().trim();
			
			try {
				int choice = Integer.parseInt(in);
				if (choice >= 1 && choice <= options.size()) {
					difficulty = DifficultyRegistry.get(options.get(choice - 1));
				} else if (choice == options.size() + 1) {
					difficulty = customDifficulty();
				} else {
					System.out.println(ConsoleColor.RED + "Invalid choice.\n" + ConsoleColor.RESET);
					System.out.flush();
				}
			} catch (NumberFormatException ex) {
				System.out.println(ConsoleColor.RED + "Please enter a number.\n" + ConsoleColor.RESET);
				System.out.flush();
			}
		}
		System.out.println("\n\n\n\n\n\n");
		System.out.flush();
		this.game.newGame(difficulty);
	}
	
	private @NotNull String capitalize(@NotNull String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	@Contract(" -> new")
	private @NotNull Difficulty customDifficulty() {
		while (true) {
			try {
				System.out.print("Enter rows, columns and mines (e.g. 10 10 15): ");
				String[] p = this.input.nextLine().trim().split("\\s+");
				if (p.length != 3) throw new Exception("Need 3 values.");
				
				return new Difficulty(
						Integer.parseInt(p[0]),
						Integer.parseInt(p[1]),
						Integer.parseInt(p[2])
				);
			} catch (Exception e) {
				System.out.println(ConsoleColor.RED + "Error: " + e.getMessage() + "\n" + ConsoleColor.RESET);
			}
		}
	}
	
	private void playAgain() {
		while (true) {
			System.out.print("Do you want to play again? (yes/no): ");
			System.out.flush();
			String inp = input.nextLine().trim().toLowerCase();
			if (inp.equals("yes") || inp.equals("y")) {
				break;
			} else if (inp.equals("no") || inp.equals("n")) {
				this.running = false;
				break;
			} else {
				System.out.println(ConsoleColor.RED + "Invalid input. Please enter 'yes' or 'no'.\n" + ConsoleColor.RESET);
				System.out.flush();
			}
		}
	}
	
	public CLIMain() {
		this.game = new Game(System.in, System.out, System.err);
		while (this.running) {
			init();
			this.playAgain();
		}
	}
}

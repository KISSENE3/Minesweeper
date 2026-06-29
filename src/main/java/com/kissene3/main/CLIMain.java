package com.kissene3.main;

import com.kissene3.console.*;
import com.kissene3.game.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CLIMain {
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	protected final Scanner input = new Scanner(System.in);
	protected final PrintStream output = new PrintStream(System.out);
	protected final PrintStream error = new PrintStream(System.err);
	protected final Game game;
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- ATTRIBUTES ---------- */
	
	protected boolean running = true;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public CLIMain() {
		this.game = new Game(this.input, this.output, this.error);
		while (this.running) {
			this.init();
			this.playAgain();
		}
	}
	
	protected void init() {
		this.output.println(ConsoleColor.GREEN_BRIGHT + "Welcome to Minesweeper\n" + ConsoleColor.RESET);
		this.output.flush();
		
		Difficulty difficulty = null;
		List<String> options = new ArrayList<>(DifficultyRegistry.getAvailableNames());
		Collections.sort(options);
		
		while (difficulty == null) {
			for (int i = 0; i < options.size(); i++) {
				this.output.println(ConsoleColor.YELLOW.toString() + (i + 1) + ". " + capitalize(options.get(i)) + ConsoleColor.RESET);
			}
			this.output.println(ConsoleColor.YELLOW.toString() + (options.size() + 1) + ". Custom" + ConsoleColor.RESET);
			
			this.output.print("Enter the number: ");
			this.output.flush();
			
			if (!this.input.hasNextLine()) {
				this.output.println("\nGame terminated by user. Goodbye!");
				this.output.flush();
				System.exit(0);
			}
			String in = input.nextLine().trim();
			
			try {
				int choice = Integer.parseInt(in);
				if (choice >= 1 && choice <= options.size()) {
					difficulty = DifficultyRegistry.get(options.get(choice - 1));
				} else if (choice == options.size() + 1) {
					difficulty = customDifficulty();
				} else {
					this.output.println(ConsoleColor.RED + "Invalid choice.\n" + ConsoleColor.RESET);
					this.output.flush();
				}
			} catch (NumberFormatException ex) {
				this.error.println(ConsoleColor.RED + "Please enter a number.\n" + ConsoleColor.RESET);
				this.error.flush();
			}
		}
		this.output.println("\n\n\n\n\n\n");
		this.output.flush();
		this.game.newGame(difficulty);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- I/O METHODS ---------- */
	
	@Contract(" -> new")
	protected @NotNull Difficulty customDifficulty() {
		while (true) {
			try {
				this.output.print("Enter rows, columns and mines (e.g. 10 10 15): ");
				this.output.flush();
				if (!this.input.hasNextLine()) {
					this.output.println("\nGame terminated by user. Goodbye!");
					this.output.flush();
					System.exit(0);
				}
				String[] p = this.input.nextLine().trim().split("\\s+");
				if (p.length != 3) throw new Exception("Need 3 values.");
				
				return new Difficulty(
						Integer.parseInt(p[0]),
						Integer.parseInt(p[1]),
						Integer.parseInt(p[2])
				);
			} catch (Exception e) {
				this.error.println(ConsoleColor.RED + "Error: " + e.getMessage() + "\n" + ConsoleColor.RESET);
				this.error.flush();
			}
		}
	}
	
	protected void playAgain() {
		while (true) {
			this.output.print("Do you want to play again? (yes/no): ");
			this.output.flush();
			if (!this.input.hasNextLine()) {
				this.output.println("\nGame terminated by user. Goodbye!");
				this.output.flush();
				System.exit(0);
			}
			String inp = input.nextLine().trim().toLowerCase();
			if (inp.equals("yes") || inp.equals("y")) {
				break;
			} else if (inp.equals("no") || inp.equals("n")) {
				this.running = false;
				break;
			} else {
				this.output.println(ConsoleColor.RED + "Invalid input. Please enter 'yes' or 'no'.\n" + ConsoleColor.RESET);
				this.output.flush();
			}
		}
	}
	
	/* ---------- I/O METHODS ---------- */
	/* ---------- UTILITIES ---------- */
	
	protected @NotNull String capitalize(@NotNull String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	/* ---------- UTILITIES ---------- */
}

package com.kissene3.main;

import com.kissene3.cli.*;
import com.kissene3.game.*;
import com.kissene3.io.AppLogger;
import com.kissene3.io.Save;
import com.kissene3.io.DifficultyRegistry;
import org.jetbrains.annotations.*;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

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
		AppLogger.info("Booting CLI in version" + Main.version + "...");
		this.game = new Game(this.input, this.output, this.error);
		while (this.running) {
			this.init();
			this.playAgain();
			this.output.println(Main.useAnsi() ? "\033[3J\033[H" : "\n\n\n\n\n\n");
			this.output.flush();
		}
	}
	
	protected void init() {
		this.output.println(ConsoleColor.GREEN_BRIGHT + "Welcome to Minesweeper\n" + ConsoleColor.RESET);
		this.output.flush();
		
		List<String> options = new ArrayList<>(DifficultyRegistry.getAvailableNames());
		Collections.sort(options);
		
		while (true) {
			if (Save.hasSaveFile())
				this.output.println(ConsoleColor.YELLOW + "-1. Load from save" + ConsoleColor.RESET);
			for (int i = 0; i < options.size(); i++)
				this.output.println(ConsoleColor.YELLOW.toString() + (i + 1) + ". " + capitalize(options.get(i)) + ConsoleColor.RESET);
			this.output.println(ConsoleColor.YELLOW.toString() + (options.size() + 1) + ". Custom" + ConsoleColor.RESET);
			
			this.output.print("Enter the number: ");
			this.output.flush();
			
			isDisconnected();
			String in = input.nextLine().trim();
			
			try {
				int choice = Integer.parseInt(in);
				if (choice == -1) {
					BigInteger gameData = Save.loadFile();
					if (gameData == null) {
						this.output.println(ConsoleColor.RED + "The save file is corrupted.\n" + ConsoleColor.RESET);
						this.output.flush();
						continue;
					}
					Save.SaveDataPackage data = Save.parseSaveData(gameData);
					Grid<?> grid = new Grid<>(data.columns(), data.rows(), data.gameData());
					this.game.loadGame(grid, data.thirties());
					break;
				} else if (choice >= 1 && choice <= options.size()) {
					this.game.newGame(DifficultyRegistry.get(options.get(choice - 1)));
					break;
				} else if (choice == options.size() + 1) {
					this.game.newGame(customDifficulty());
					break;
				} else {
					this.output.println(ConsoleColor.RED + "Invalid choice.\n" + ConsoleColor.RESET);
					this.output.flush();
				}
			} catch (NumberFormatException e) {
				this.output.println(ConsoleColor.RED + "Please enter a number.\n" + ConsoleColor.RESET);
				this.output.flush();
			} catch (Exception e) {
				this.error.println("Error: " + e.getMessage() + "\n");
				this.error.flush();
				AppLogger.warning(e.getMessage());
			}
		}
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- I/O METHODS ---------- */
	
	@Contract(" -> new")
	protected @NotNull Difficulty customDifficulty() {
		while (true) {
			try {
				this.output.print("Enter rows, columns and mines (e.g. 10 10 15): ");
				this.output.flush();
				isDisconnected();
				String[] p = this.input.nextLine().trim().split("\\s+");
				if (p.length != 3) throw new Exception("Need 3 values.");
				
				return new Difficulty(
						Integer.parseInt(p[0]),
						Integer.parseInt(p[1]),
						Integer.parseInt(p[2])
				);
			} catch (Exception e) {
				this.error.println("Error: " + e.getMessage() + "\n");
				this.error.flush();
				AppLogger.warning("Error: " + e.getMessage() + "\n");
			}
		}
	}
	
	protected void playAgain() {
		while (true) {
			this.output.print("Do you want to play again? (Y/N): ");
			this.output.flush();
			isDisconnected();
			String inp = input.nextLine().trim().toLowerCase();
			if (inp.equals("y") || inp.equals("yes") || inp.equals("t") || inp.equals("true")) {
				break;
			} else if (inp.equals("n") || inp.equals("no") || inp.equals("f") || inp.equals("false")) {
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
	
	protected void isDisconnected() {
		if (!this.input.hasNextLine()) {
			this.output.println(ConsoleColor.YELLOW_BRIGHT + "\nGame terminated by user. Goodbye!" + ConsoleColor.RESET);
			this.output.flush();
			AppLogger.info("Quitting...");
			System.exit(0);
		}
	}
	
	/* ---------- UTILITIES ---------- */
}

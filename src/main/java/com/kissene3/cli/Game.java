package com.kissene3.cli;

import com.kissene3.game.*;
import com.kissene3.game.Timer;
import com.kissene3.io.AppLogger;
import com.kissene3.io.Save;
import com.kissene3.main.Main;
import org.jetbrains.annotations.*;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

@SuppressWarnings("unused")
public class Game {
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	protected final Scanner input;
	protected final PrintStream output;
	@SuppressWarnings("FieldCanBeLocal") protected final PrintStream error;
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- ATTRIBUTES ---------- */
	
	protected Grid<?> grid;
	protected long startTime;
	protected Timer timer;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public Game(@NotNull Scanner scanner, @NotNull PrintStream outputStream, @NotNull PrintStream errorStream) {
		this.input = scanner;
		this.output = outputStream;
		this.error = errorStream;
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- INITIALIZERS ---------- */
	
	public void newGame(@NotNull Difficulty difficulty) {
		AppLogger.info("Game started with grid: " + difficulty.width() + " * " + difficulty.height() + " | Mines: " + difficulty.numMines());
		this.grid = new Grid<>(difficulty);
		this.grid.createCells();
		this.timer = new Timer();
		this.startTime = System.currentTimeMillis();
		this.mainLoop();
	}
	
	public void loadGame(Grid<?> grid, long thirties) {
		this.grid = grid;
		this.grid.loadCellsFromState();
		this.timer = new Timer(thirties);
		this.startTime = System.currentTimeMillis() - (thirties * 1000L / 30L);
		this.mainLoop();
	}
	
	protected void mainLoop() {
		while (this.grid.getGameStatus() == Status.RUNNING) {
			this.output.println(Main.useAnsi() ? "\033[3J\033[H" : "\n\n\n\n\n\n");
			this.output.flush();
			this.printGrid();
			this.input();
		}
		switch (this.grid.getGameStatus()) {
			case WON -> this.win();
			case LOST -> this.lose();
		}
	}
	
	/* ---------- INITIALIZERS ---------- */
	/* ---------- GETTERS ---------- */
	
	public Scanner getInputStream() {
		return this.input;
	}
	
	public PrintStream getOutputStream() {
		return this.output;
	}
	
	public PrintStream getErrorStream() {
		return this.error;
	}
	
	/* ---------- GETTERS ---------- */
	/* ---------- INTERACTORS ---------- */
	
	protected void printGrid() {
		long elapsedMillis = System.currentTimeMillis() - this.startTime;
		this.timer.setThirties(elapsedMillis * 30L / 1000L);
		this.output.print(ConsoleColor.YELLOW + "Elapsed time: " + this.timer + ConsoleColor.RESET);
		this.output.println(this.grid);
		this.output.println();
		this.output.flush();
	}
	
	protected void input() {
		label:
		while (this.grid.getGameStatus() == Status.RUNNING) {
			this.output.println(ConsoleColor.GREEN + "Commands: [r] Reveal, [f] Flag/Unflag, [save] Save & Exit, [quit] Exit without saving" + ConsoleColor.RESET);
			this.output.printf("%sMove -> [r/f] [0-%d] [0-%d]%s%n", ConsoleColor.GREEN, this.grid.getColumns() - 1, this.grid.getRows() - 1, ConsoleColor.RESET);
			this.output.println(ConsoleColor.GREEN + "e. g. r 5 10" + ConsoleColor.RESET);
			this.output.print("Enter your move: ");
			this.output.flush();
			this.isDisconnected();
			String line = this.input.nextLine().trim().toLowerCase();
			switch (line) {
				case "" -> {
					continue;
				}
				case "quit", "exit" -> {
					this.output.printf(ConsoleColor.CYAN + "Are you sure you want to quit? (Y/N): " + ConsoleColor.RESET);
					this.output.flush();
					this.isDisconnected();
					String confirm = this.input.nextLine().trim().toLowerCase();
					if (confirm.equals("y") || confirm.equals("yes") || confirm.equals("t") || confirm.equals("true")) {
						this.quit();
					} else
						continue;
				}
				case "save" -> {
					long elapsedMillis = System.currentTimeMillis() - this.startTime;
					this.timer.setThirties(elapsedMillis * 30L / 1000L);
					
					BigInteger gameData = Save.convertGameToBigInteger(this.grid, this.timer.getThirties());
					Save.saveFile(gameData);
					
					this.output.println(ConsoleColor.YELLOW + "Saved with elapsed time: " + this.timer + ConsoleColor.RESET);
					this.output.println(ConsoleColor.BLUE_BRIGHT + "Game saved successfully! Goodbye" + ConsoleColor.RESET);
					this.output.flush();
					AppLogger.info("Game saved successfully!");
					System.exit(0);
				}
			}
			String[] data = line.split("\\s+");
			if (data.length != 3) {
				this.output.println(ConsoleColor.RED + "Invalid input. Please enter the action and two numbers separated by a space.\n" + ConsoleColor.RESET);
				this.output.flush();
				continue;
			}
			String action = data[0];
			int row;
			int col;
			try {
				row = Integer.parseInt(data[1]);
				col = Integer.parseInt(data[2]);
			} catch (NumberFormatException ex) {
				this.output.println(ConsoleColor.RED + "Invalid input. Please enter valid numbers.\n" + ConsoleColor.RESET);
				this.output.flush();
				continue;
			}
			int result = this.grid.act(action, row, col);
			switch (result) {
				case 0:  // Cell revealed successfully
					break label;
				case -1:  // Invalid action
					this.output.println(ConsoleColor.RED + "Invalid input. Please enter a valid action.\n" + ConsoleColor.RESET);
					this.output.flush();
					break;
				case -2:  // Invalid coordinates
					this.output.println(ConsoleColor.RED + "Invalid input. Please enter valid numbers.\n" + ConsoleColor.RESET);
					this.output.flush();
					break;
				case -3:  // Cell already revealed
					this.output.println(ConsoleColor.RED + "Cell already revealed. Please choose another cell.\n" + ConsoleColor.RESET);
					this.output.flush();
					break;
				case -4:  // Cell is flagged
					this.output.println(ConsoleColor.RED + "Cell is flagged. Please choose another cell.\n" + ConsoleColor.RESET);
					this.output.flush();
					break;
				case -5:  // Can't flag as a first move
					this.output.println(ConsoleColor.RED + "Can't flag as the first move.\n" + ConsoleColor.RESET);
					this.output.flush();
			}
		}
		this.output.println("\n\n\n\n\n\n");
		this.output.flush();
	}
	
	protected void win() {
		this.printGrid();
		this.output.println(ConsoleColor.GREEN_BRIGHT + "You won!" + ConsoleColor.RESET);
		this.output.flush();
		AppLogger.info("Game finished, won");
	}
	
	protected void lose() {
		this.printGrid();
		this.output.println(ConsoleColor.RED_BRIGHT + "You lost!" + ConsoleColor.RESET);
		this.output.flush();
		AppLogger.info("Game finished, lost");
	}
	
	/* ---------- INTERACTORS ---------- */
	
	protected void isDisconnected() {
		if (!this.input.hasNextLine()) {
			this.quit();
		}
	}
	
	protected void quit() {
		this.output.println(ConsoleColor.YELLOW_BRIGHT + "\nGame terminated by user. Goodbye!" + ConsoleColor.RESET);
		this.output.flush();
		AppLogger.info("Quitting...");
		System.exit(0);
	}
}
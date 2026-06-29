package com.kissene3.console;

import com.kissene3.game.*;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.Scanner;


@SuppressWarnings("unused")
public class Game {
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	protected final Scanner input;
	protected final PrintStream output;
	@SuppressWarnings("FieldCanBeLocal") protected final PrintStream error;
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- ATTRIBUTES ---------- */
	
	protected Grid<?> grid;
	
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
		this.grid = new Grid<>(difficulty);
		this.grid.createCells();
		this.mainLoop();
	}
	
	protected void mainLoop() {
		while (this.grid.getGameStatus() == Status.RUNNING) {
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
		this.output.println(this.grid);
		this.output.println();
		this.output.flush();
	}
	
	protected void input() {
		label:
		while (this.grid.getGameStatus() == Status.RUNNING) {
			this.output.println(ConsoleColor.GREEN + "Commands: [r] Reveal, [f] Flag/Unflag" + ConsoleColor.RESET);
			this.output.printf("%sMove -> [r/f] [0-%d] [0-%d]%n%s", ConsoleColor.GREEN, this.grid.getColumns() - 1, this.grid.getRows() - 1, ConsoleColor.RESET);
			this.output.println(ConsoleColor.GREEN + "e. g. r 5 10" + ConsoleColor.RESET);
			this.output.print("Enter type of move, row and column: ");
			this.output.flush();
			if (!this.input.hasNextLine()) {
				this.output.println("\nGame terminated by user. Goodbye!");
				this.output.flush();
				System.exit(0);
			}
			String[] data = this.input.nextLine().trim().split("\\s+");
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
		this.grid.flagAllMines();
		this.printGrid();
		this.output.println(ConsoleColor.GREEN_BRIGHT + "You won!" + ConsoleColor.RESET);
		this.output.flush();
	}
	
	protected void lose() {
		this.grid.showAllMines();
		this.printGrid();
		this.output.println(ConsoleColor.RED_BRIGHT + "You lost!" + ConsoleColor.RESET);
		this.output.flush();
	}
	
	/* ---------- INTERACTORS ---------- */
}
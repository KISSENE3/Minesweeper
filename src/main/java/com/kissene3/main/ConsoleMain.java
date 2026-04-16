package com.kissene3.main;

import com.kissene3.console.*;
import com.kissene3.game.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class ConsoleMain {
	private static final Scanner input = new Scanner(System.in);
	private static Game game;
	private static boolean running = true;
	
	private static void init() {
		boolean cycle = true;
		Difficulties difficulty = null;
		System.out.println(ConsoleColor.GREEN_BRIGHT + "Welcome to Minesweeper\n" + ConsoleColor.RESET);
		System.out.flush();
		while (cycle) {
			System.out.println(ConsoleColor.YELLOW + "Choose a difficulty:" + ConsoleColor.RESET);
			System.out.println(ConsoleColor.YELLOW + "1. Easy" + ConsoleColor.RESET);
			System.out.println(ConsoleColor.YELLOW + "2. Medium" + ConsoleColor.RESET);
			System.out.println(ConsoleColor.YELLOW + "3. Hard" + ConsoleColor.RESET);
			System.out.println(ConsoleColor.YELLOW + "4. Custom" + ConsoleColor.RESET);
			System.out.print("Enter the number: ");
			System.out.flush();
			String in = input.nextLine().trim();
			int chose;
			try {
				chose = Integer.parseInt(in);
			} catch (NumberFormatException ex) {
				System.out.println(ConsoleColor.RED + "Invalid input. Please enter a number between 1 and 4.\n" + ConsoleColor.RESET);
				System.out.flush();
				continue;
			}
			difficulty = switch (chose) {
				case 1 -> {
					cycle = false;
					yield Difficulties.EASY;
				}
				case 2 -> {
					cycle = false;
					yield Difficulties.MEDIUM;
				}
				case 3 -> {
					cycle = false;
					yield Difficulties.HARD;
				}
				case 4 -> {
					cycle = false;
					yield customDifficulty();
				}
				default -> {
					System.out.println(ConsoleColor.RED + "Invalid input. Please enter a number between 1 and 4.\n" + ConsoleColor.RESET);
					System.out.flush();
					yield null;
				}
			};
		}
		System.out.println("\n\n\n\n\n\n");
		System.out.flush();
		game.newGame(difficulty);
	}
	
	@Contract(" -> new")
	private static @NotNull Difficulties customDifficulty() {
		int rows, columns, mines;
		while (true) {
			System.out.print("Enter the number of rows and columns of the grid (e.g. 10 16): ");
			System.out.flush();
			String[] parts = input.nextLine().trim().split(" ");
			if (parts.length != 2) {
				System.out.println(ConsoleColor.RED + "Invalid input. Please enter two numbers separated by a space.\n" + ConsoleColor.RESET);
				System.out.flush();
				continue;
			}
			try {
				rows = Integer.parseInt(parts[0]);
				columns = Integer.parseInt(parts[1]);
			} catch (NumberFormatException ex) {
				System.out.println(ConsoleColor.RED + "Invalid input. Please enter valid numbers for rows and columns.\n" + ConsoleColor.RESET);
				System.out.flush();
				continue;
			}
			if (rows <= 0 || columns <= 0) {
				System.out.println(ConsoleColor.RED + "The number of rows and columns cannot be less than or equal to 0.\n" + ConsoleColor.RESET);
				System.out.flush();
				continue;
			}
			if (rows * columns <= 9) {
				System.out.println(ConsoleColor.RED + "The size is too small.\n" + ConsoleColor.RESET);
				System.out.flush();
				continue;
			}
			break;
		}
		while (true) {
			System.out.print("Enter the number of mines: ");
			System.out.flush();
			try {
				mines = Integer.parseInt(input.nextLine().trim());
			} catch (NumberFormatException ex) {
				System.out.println(ConsoleColor.RED + "Invalid input. Please enter a valid number for mines.\n" + ConsoleColor.RESET);
				System.out.flush();
				continue;
			}
			if (mines > (rows * columns) - 9) {
				System.out.println(ConsoleColor.RED + "The number of mines must be lesser than the size of the grid minus nine.\n" + ConsoleColor.RESET);
				System.out.flush();
				continue;
			}
			if (mines <= 0) {
				System.out.println(ConsoleColor.RED + "The number of mines cannot be less than or equal to 0.\n" + ConsoleColor.RESET);
				System.out.flush();
				continue;
			}
			break;
		}
		return new Difficulties(rows, columns, mines);
	}
	
	private static void playAgain() {
		while (true) {
			System.out.print("Do you want to play again? (yes/no): ");
			System.out.flush();
			String inp = input.nextLine().trim().toLowerCase();
			if (inp.equals("yes") || inp.equals("y")) {
				break;
			} else if (inp.equals("no") || inp.equals("n")) {
				running = false;
				break;
			} else {
				System.out.println(ConsoleColor.RED + "Invalid input. Please enter 'yes' or 'no'.\n" + ConsoleColor.RESET);
				System.out.flush();
			}
		}
	}
	
	public static void main() {
		game = new Game(System.in, System.out, System.err);
		while (running) {
			init();
			playAgain();
		}
	}
}

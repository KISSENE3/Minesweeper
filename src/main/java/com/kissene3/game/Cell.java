package com.kissene3.game;

import com.kissene3.console.ConsoleColor;
import org.jetbrains.annotations.NotNull;

public class Cell<T extends Grid<? extends Cell<T>>> {
	protected final int row;
	protected final int column;
	protected final T grid;
	
	protected CellDisplay display;
	protected boolean isRevealed;
	protected boolean isFlagged;
	protected boolean hasExploded;
	
	public Cell(int row, int column, @NotNull T grid) {
		this.row = row;
		this.column = column;
		this.grid = grid;
		this.display = CellDisplay.HIDDEN;
		this.isRevealed = false;
		this.isFlagged = false;
		this.hasExploded = false;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public @NotNull CellDisplay getDisplay() {
		return this.display;
	}
	
	public boolean isRevealed() {
		return this.isRevealed;
	}
	
	public boolean isFlagged() {
		return this.isFlagged;
	}
	
	public boolean isMine() { return this.display == CellDisplay.MINE; }
	
	public boolean hasExploded() {
		return this.hasExploded;
	}
	
	public void placeMine() {
		this.display = CellDisplay.MINE;
	}
	
	public void reveal() {
		if (this.isFlagged)
			return;
		this.isRevealed = true;
	}
	
	public void toggleFlagged() {
		if (this.isRevealed)
			return;
		this.isFlagged = !this.isFlagged;
	}
	
	public void explode() {
		this.hasExploded = true;
	}
	
	public void setDisplay(@NotNull CellDisplay display) {
		this.display = display;
	}
	
	public void checkNear() {
		if (this.display == CellDisplay.MINE) return;
		int count = 0;
		for (int r = -1; r <= 1; r++) {
			for (int c = -1; c <= 1; c++) {
				int row = this.row + r;
				int column = this.column + c;
				if (row < 0 || row >= this.grid.getRows() || column < 0 || column >= this.grid.getColumns() || (r == 0 && c == 0))
					continue;
				if (this.grid.getCell(row, column).isMine())
					count++;
			}
		}
		this.display = switch (count) {
			case 0 -> CellDisplay.ZERO;
			case 1 -> CellDisplay.ONE;
			case 2 -> CellDisplay.TWO;
			case 3 -> CellDisplay.THREE;
			case 4 -> CellDisplay.FOUR;
			case 5 -> CellDisplay.FIVE;
			case 6 -> CellDisplay.SIX;
			case 7 -> CellDisplay.SEVEN;
			case 8 -> CellDisplay.EIGHT;
			default -> throw new IllegalStateException("Unexpected value: " + count);
		};
	}
	
	@Override
	public @NotNull String toString() {
		if (this.isFlagged)
			return ConsoleColor.RED_BRIGHT + " " + CellDisplay.FLAGGED + " " + ConsoleColor.RESET;
		if (!this.isRevealed)
			return " " + CellDisplay.HIDDEN + " ";
		return switch (this.display) {
			case ONE   -> ConsoleColor.BLUE + " 1 " + ConsoleColor.RESET;
			case TWO   -> ConsoleColor.GREEN + " 2 " + ConsoleColor.RESET;
			case THREE -> ConsoleColor.RED + " 3 " + ConsoleColor.RESET;
			case FOUR  -> ConsoleColor.PURPLE + " 4 " + ConsoleColor.RESET;
			case FIVE  -> ConsoleColor.YELLOW + " 5 " + ConsoleColor.RESET;
			case SIX   -> ConsoleColor.CYAN + " 6 " + ConsoleColor.RESET;
			case SEVEN -> ConsoleColor.BLACK + " 7 " + ConsoleColor.RESET;
			case EIGHT -> ConsoleColor.GRAY + " 8 " + ConsoleColor.RESET;
			case MINE  -> (this.hasExploded ? ConsoleColor.MINE_EXPLODED : "") + " * " + ConsoleColor.RESET;
			case ZERO  -> "   ";
			default    -> " " + this.display.getSymbol() + " ";
		};
	}
}

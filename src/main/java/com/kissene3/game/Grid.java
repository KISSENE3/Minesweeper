package com.kissene3.game;

import com.kissene3.console.ConsoleColor;
import org.jetbrains.annotations.NotNull;
import java.util.LinkedList;
import java.util.Random;

@SuppressWarnings("unused")
public class Grid<C extends Cell<? extends Grid<C>>> {
	protected final int columns;
	protected final int rows;
	protected final int numMines;
	
	protected C[][] grid;
	protected Status gameStatus;
	protected boolean firstMove;
	protected int flagsRemaining;
	protected int cellsToReveal;
	
	public Grid(@NotNull Difficulty difficulty) {
		this.columns = difficulty.width();
		this.rows = difficulty.height();
		this.numMines = difficulty.numMines();
		this.gameStatus = Status.RUNNING;
		this.firstMove = true;
		this.flagsRemaining = this.numMines;
		this.cellsToReveal = this.columns * this.rows - this.numMines;
	}
	
	public void createCells() {
		//noinspection unchecked
		this.grid = (C[][]) new Cell[this.rows][this.columns];
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.columns; c++) {
				this.grid[r][c] = this.createCell(r, c);
			}
		}
	}
	
	protected C createCell(int r, int c) {
		//noinspection unchecked, rawtypes
		return (C) new Cell<>(r, c, (Grid) this);
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public @NotNull C getCell(int r, int c) {
		return this.grid[r][c];
	}
	
	public int getNumMines() {
		return this.numMines;
	}
	
	public @NotNull Status getGameStatus() {
		return this.gameStatus;
	}
	
	public int getFlagsRemaining() {
		return this.flagsRemaining;
	}
	
	protected void initializeGrid(int r, int c) {
		this.firstMove = false;
		this.placeMines(r, c);
		for (C[] row : this.grid) {
			for (C cell : row) {
				cell.checkNear();
			}
		}
	}
	
	protected void placeMines(int r, int c) {
		Random random = new Random();
		int minesPlaced = 0;
		while (minesPlaced < this.numMines) {
			int row = random.nextInt(this.rows);
			int column = random.nextInt(this.columns);
			if (Math.abs(row - r) <= 1 && Math.abs(column - c) <= 1) continue;
			
			if (!this.grid[row][column].isMine()) {
				this.grid[row][column].placeMine();
				minesPlaced++;
			}
		}
	}
	
	public int act(String action, int r, int c) {
		action = action.toLowerCase().trim();
		if (r < 0 || r >= this.rows || c < 0 || c >= this.columns) return -2;
		if (action.equals("f")) {
			if (this.firstMove) return -5;
			if (this.grid[r][c].isRevealed()) return -3;
			this.invertFlag(r, c);
		} else if (action.equals("r")) {
			if (this.firstMove) this.initializeGrid(r, c);
			return this.revealCell(r, c);
		} else return -1;
		return 0;
	}
	
	protected void invertFlag(int r, int c) {
		C cell = this.grid[r][c];
		if (cell.isRevealed()) return;
		cell.toggleFlagged();
		this.flagsRemaining += cell.isFlagged() ? -1 : +1;
	}
	
	protected int revealCell(int startingRow, int startingColumn) {
		C startingCell = this.grid[startingRow][startingColumn];
		if (startingCell.isRevealed()) return -3;
		if (startingCell.isFlagged()) return -4;
		if (startingCell.isMine()) {
			startingCell.explode();
			this.gameStatus = Status.LOST;
			this.showAllMines();
			return 0;
		}
		
		LinkedList<C> queue = new LinkedList<>();
		queue.add(startingCell);
		
		while (!queue.isEmpty()) {
			C currentCell = queue.poll();
			if (currentCell.isRevealed() || currentCell.isFlagged()) continue;
			
			currentCell.reveal();
			this.cellsToReveal--;
			if (currentCell.getDisplay() != CellDisplay.ZERO) continue;
			
			for (int r = -1; r <= 1; r++) {
				for (int c = -1; c <= 1; c++) {
					int row = currentCell.getRow() + r;
					int column = currentCell.getColumn() + c;
					if (row < 0 || row >= this.rows || column < 0 || column >= this.columns || (r == 0 && c == 0)) continue;
					C neighbourCell = this.grid[row][column];
					if (neighbourCell.isRevealed() || neighbourCell.isFlagged()) continue;
					queue.add(neighbourCell);
				}
			}
		}
		
		if (this.cellsToReveal == 0) {
			this.gameStatus = Status.WON;
			this.flagAllMines();
		}
		return 0;
	}
	
	public void showAllMines() {
		for (C[] rows : this.grid) {
			for (C cell : rows) {
				if (cell.isMine()) cell.reveal();
			}
		}
	}
	
	public void flagAllMines() {
		for (C[] rows : this.grid) {
			for (C cell : rows) {
				if (cell.isMine() && !cell.isFlagged()) cell.toggleFlagged();
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append(String.format("%sFlag(s) remaining: %d%n%s", ConsoleColor.YELLOW_BRIGHT, this.flagsRemaining, ConsoleColor.RESET));
		toReturn.append("\n     ");
		for (int c = 0; c < this.columns; c++) toReturn.append(String.format("%2d ", c));
		toReturn.append("\n\n");
		for (int r = 0; r < this.rows; r++) {
			toReturn.append(String.format("%2d   ", r));
			for (int c = 0; c < this.columns; c++) {
				toReturn.append(this.grid[r][c].toString());
			}
			toReturn.append("\n");
		}
		return toReturn.toString();
	}
}

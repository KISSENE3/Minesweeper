package com.kissene3.game;

import com.kissene3.console.*;
import com.kissene3.gui.*;
import com.kissene3.gui.utilities.MyUtilities.*;
import com.kissene3.gui.utilities.MyUtilities.Dimension;
import com.kissene3.gui.components.*;
import com.kissene3.gui.interfaces.Scrollable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.Random;

@SuppressWarnings("unused")
public class Grid extends MyAbstractButton implements Scrollable {
	
	/* ---------- FINAL GAME ATTRIBUTES ---------- */
	
	/** The width of the grid. The horizontal number of {@link Cell}s */
	private final int columns;
	
	/** The height of the grid. The vertical number of {@link Cell}s */
	private final int rows;
	
	/** The grid of the main.java.com.kissene3.game. */
	private final Cell[][] grid;
	
	/** The number of mines for the main.java.com.kissene3.game. */
	private final int numMines;
	
	/* ---------- FINAL GAME ATTRIBUTES ---------- */
	/* ---------- GAME ATTRIBUTES ---------- */
	
	/**
	 * The internal {@link Status} of the main.java.com.kissene3.game.
	 * ({@code RUNNING}, {@code WON}, {@code LOST})
	 */
	private Status gameStatus;
	
	/**
	 * A flag that indicates if the current move it's the first.
	 * It's used to place the mines after the first move.
	 */
	private boolean firstMove;
	
	/**
	 * The number of flags remaining.
	 * This value is initialized to the number of mines.
	 * It can be negative, it's just a visual counter for the player.
	 */
	private int flagsRemaining;
	
	/**
	 * Counter used as checking to end the main.java.com.kissene3.game.
	 * It's decreased at every revealed non-mine cell.
	 */
	private int cellsToReveal;
	/* ---------- GAME ATTRIBUTES ---------- */
	/* ---------- GUI ATTRIBUTES ---------- */
	
	/** The size (width and height) used to draw each {@link Cell} */
	private int cellSize;
	
	/** The settings of the main.java.com.kissene3.game. */
	private Settings settings;
	
	/** The last hovered cell. Used to handle the overlays. */
	private Cell lastHovered = null;
	
	/** The last pressed cell. Used to handle clicks. */
	private Cell lastPressed = null;
	
	/** The index for the horizontal scrolling. */
	private int scrollX = 0;
	
	/** The index for the vertical scrolling. */
	private int scrollY = 0;
	
	/** The visible width of the grid. */
	private int viewWidth;
	
	/** The visible height of the grid. *  */
	private int viewHeight;
	
	/* ---------- GUI ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	/**
	 * Creates a new Grid object for {@code com.kissene3.console} use.
	 * @param columns The number of columns of the grid.
	 * @param rows The number of rows of the grid.
	 * @param numMines The number of mines to place on the grid.
	 */
	public Grid(int columns, int rows, int numMines) {
		super(0, 0, 0, 0);
		this.columns = columns;
		this.rows = rows;
		this.grid = new Cell[rows][columns];
		this.numMines = numMines;
		this.gameStatus = Status.RUNNING;
		this.firstMove = true;
		this.flagsRemaining = this.numMines;
		this.cellsToReveal = this.columns * this.rows - this.numMines;
		
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.columns; c++) {
				this.grid[r][c] = new Cell(r, c, this, this.getX() + c * cellSize, this.getY() + r * cellSize, cellSize);
			}
		}
	}
	
	/**
	 * Creates a new Grid object for GUI use.
	 * @param x The x-coordinate of the grid's top-left corner for the GUI.
	 * @param y The y-coordinate of the grid's top-left corner for the GUI.
	 * @param columns The number of columns of the grid.
	 * @param rows The number of rows of the grid.
	 * @param numMines The number of mines to place on the grid.
	 * @param cellSize The size (width and height) of the {@link Cell} for the GUI.
	 */
	public Grid(int x, int y, int columns, int rows, int numMines, int cellSize, @NotNull Settings settings) {
		this(new Coordinates(x, y), columns, rows, numMines, cellSize, settings);
	}
	
	/**
	 * Creates a new Grid object for GUI use.
	 * @param coordinates The top-left coordinates of the component for the GUI.
	 * @param columns The number of columns of the grid.
	 * @param rows The number of rows of the grid.
	 * @param numMines The number of mines to place on the grid.
	 * @param cellSize The size (width and height) of the {@link Cell} for the GUI.
	 */
	public Grid(@NotNull Coordinates coordinates, int columns, int rows, int numMines, int cellSize, @NotNull Settings settings) {
		super(coordinates, new Dimension(columns * cellSize, rows * cellSize));
		this.columns = columns;
		this.rows = rows;
		this.setCellSize(cellSize);
		this.grid = new Cell[rows][columns];
		this.numMines = numMines;
		this.settings = settings;
		this.gameStatus = Status.RUNNING;
		this.firstMove = true;
		this.flagsRemaining = this.numMines;
		this.cellsToReveal = this.columns * this.rows - this.numMines;
		
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.columns; c++) {
				this.grid[r][c] = new Cell(r, c, this, this.getX() + c * cellSize, this.getY() + r * cellSize, cellSize);
			}
		}
		
		this.onBoundsChanged();
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GAME GETTERS ---------- */
	
	public int getColumns() {
		return this.columns;
	}
	public int getRows() {
		return this.rows;
	}
	
	/** Gets the {@link Cell} at the given indexes. */
	public Cell getCell(int r, int c) {
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
	
	/* ---------- GAME GETTERS ---------- */
	/* ---------- GUI GETTERS/SETTERS ---------- */
	
	public void setCellSize(int size) {
		this.cellSize = size < 16 ? 16 : size / 16 * 16;
		this.onBoundsChanged();
	}
	public int getCellSize() {
		return this.cellSize;
	}
	
	public void setSettings(@NotNull Settings settings) {
		this.settings = settings;
	}
	public @NotNull Settings getSettings() {
		return this.settings;
	}
	
	@Override
	protected void onBoundsChanged() {
		if (this.grid == null)
			return;
		
		int expectedW = this.columns * this.cellSize;
		int expectedH = this.rows * this.cellSize;
		
		if (this.getWidth() != expectedW || this.getHeight() != expectedH) {
			this.setDimensions(expectedW, expectedH);
			return;
		}
		
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.columns; c++) {
				Cell cell = this.grid[r][c];
				int cellX = this.getX() + (c * this.cellSize);
				int cellY = this.getY() + (r * this.cellSize);
				
				if (cell.getX() != cellX || cell.getY() != cellY || cell.getWidth() != this.cellSize) {
					cell.setBounds(cellX, cellY, this.cellSize, this.cellSize);
				}
			}
		}
	}
	
	public void setViewport(int w, int h) {
		this.viewWidth = w;
		this.viewHeight = h;
	}
	
	public void updateScroll(int sx, int sy) {
		this.scrollX = sx;
		this.scrollY = sy;
	}
	
	/* ---------- GUI GETTERS/SETTERS ---------- */
	/* ---------- INITIALIZERS ---------- */
	
	/**
	 * Initializes the grid after the first move, placing mines and calculating adjacent mine counts.
	 * @param r The row of the first clicked {@link Cell}.
	 * @param c The column of the first clicked {@link Cell}.
	 */
	private void initializeGrid(int r, int c) {
		this.firstMove = false;
		this.placeMines(r, c);
		
		for (Cell[] row : this.grid) {
			for (Cell cell : row) {
				cell.checkNear();
			}
		}
	}
	
	/**
	 * Places the mines on the grid, ensuring no mine is placed at the starting {@link Cell} or its immediate neighbors.
	 * @param r The row of the first clicked {@code Cell}.
	 * @param c The column of the first clicked {@code Cell}.
	 */
	private void placeMines(int r, int c) {
		Random random = new Random();
		int minesPlaced = 0;
		while (minesPlaced < this.numMines) {
			int row = random.nextInt(this.rows);
			int column = random.nextInt(this.columns);
			if (Math.abs(row - r) <= 1 && Math.abs(column - c) <= 1)
				continue;
				
			if (!this.grid[row][column].isMine()) {
				this.grid[row][column].placeMine();
				minesPlaced++;
			}
		}
	}
	
	/* ---------- INITIALIZERS ---------- */
	/* ---------- LOGIC ---------- */
	
	/**
	 * Handles the user's action (flag or reveal) on a specific {@link Cell}.
	 * @param action The action to perform ("f" for flag, "r" for reveal).
	 * @param r The row index of the {@code Cell}.
	 * @param c The column index of the {@code Cell}.
	 * @return The result of the action: {@code 0} for success, negative values for errors.
	 *         {@code -1}: Invalid action, {@code -2}: Invalid coordinates, {@code -3}: Cell already revealed, {@code -4}: Cell is flagged, {@code -5}: Cannot flag on first move.
	 * @see #invertFlag(int, int)
	 * @see #revealCell(int, int)
	 */
	public int act(String action, int r, int c) {
		action = action.toLowerCase().trim();
		if (r < 0 || r >= this.rows || c < 0 || c >= this.columns)
			return -2; // Invalid coordinates
		if (action.equals("f")) {
			if (this.firstMove)
				return -5; // Can't flag as a first move
			if (this.grid[r][c].isRevealed())
				return -3; // Cell already revealed
			this.invertFlag(r, c);
		} else if (action.equals("r")) {
			if (this.firstMove)
				this.initializeGrid(r, c);
			return this.revealCell(r, c);
		} else
			return -1; // Invalid action
		return 0;
	}
	
	/**
	 * Invert the flagged status of a {@link Cell} if it's not revealed.
	 * Increases/decreases the flags remaining counter.
	 * @param r The row index of the {@code Cell}.
	 * @param c The column index of the {@code Cell}.
	 */
	private void invertFlag(int r, int c) {
		Cell cell = this.grid[r][c];
		if (cell.isRevealed())
			return;
		cell.toggleFlagged();
		this.flagsRemaining += cell.isFlagged() ? -1 : +1;
	}
	
	/**
	 * Reveal the {@link Cell} at the given coordinates.
	 * If it's a bomb it marks it as exploded.
	 * Iteratively reveals every adjacent {@code Cell} if the revealed {@code Cell} has no adjacent mines.
	 * @param startingRow The row of the {@code Cell}.
	 * @param startingColumn The column of the {@code Cell}.
	 * @return {@code 0} if the Cell is revealed successfully, {@code -3} if the Cell is already revealed, {@code -4} if the Cell is flagged.
	 */
    private int revealCell(int startingRow, int startingColumn) {
		Cell startingCell = this.grid[startingRow][startingColumn];
		if (startingCell.isRevealed())
			return -3; // Cell already revealed
		if (startingCell.isFlagged())
			return -4; // Cell is flagged
		if(startingCell.isMine()) {
			startingCell.explode();
			this.gameStatus = Status.LOST;
			this.showAllMines();
			return 0;
		}
		
		LinkedList<Cell> queue = new LinkedList<>();
		
		queue.add(startingCell);
		
		while (!queue.isEmpty()) {
			Cell currentCell = queue.poll();
			
			if (currentCell.isRevealed() || currentCell.isFlagged())
				continue;
			
			currentCell.reveal();
			this.cellsToReveal--;
			
			if (currentCell.getDisplay() != CellDisplay.ZERO)
				continue;
			
			for (int r = -1; r <= 1; r++)
				for (int c = -1; c <= 1; c++) {
					int row = currentCell.getRow() + r;
					int column = currentCell.getColumn() + c;
					if (row < 0 || row >= this.rows || column < 0 || column >= this.columns || (r == 0 && c == 0))
						continue;
					Cell neighbourCell = this.grid[row][column];
					if (neighbourCell.isRevealed() || neighbourCell.isFlagged())
						continue;
					
					queue.add(neighbourCell);
				}
		}
		
		if (this.cellsToReveal == 0) {
			this.gameStatus = Status.WON;
			this.flagAllMines();
		}
		return 0;
    }
	
	/**
	 * Shows all the mines on the grid.
	 * It's intended to be called if the player lose.
	 * (aka. {@link Status#LOST})
	 */
	public void showAllMines() {
		for (Cell[] rows : this.grid)
			for (Cell cell : rows) {
				cell.setActive(false);
				if (cell.isMine())
					cell.reveal();
			}
	}
	
	/**
	 * Flags all the mines on the grid.
	 * It's intended to be called if the player wins.
	 * (aka. {@link Status#WON})
	 */
	public void flagAllMines() {
		for (Cell[] rows : this.grid)
			for (Cell cell : rows) {
				cell.setActive(false);
				if (cell.isMine() && !cell.isFlagged())
					cell.toggleFlagged();
			}
	}
	
	/* ---------- LOGIC ---------- */
	/* ---------- @Override Object ---------- */
	
	@Override
	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		
		toReturn.append(String.format("%sFlag(s) remaining: %d%n%s", ConsoleColor.YELLOW_BRIGHT, this.flagsRemaining, ConsoleColor.RESET));
		toReturn.append("\n");
		
		toReturn.append("     ");
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
	
	/* ---------- @Override Object ---------- */
	/* ---------- @Override Drawable ---------- */
	
	/**
	 * Draws the grid on the screen.
	 * Iterates every {@link Cell} and uses it's {@code draw()}
	 * @param g The {@link Graphics2D} object used for drawing.
	 */
	@Override
	public void draw(@NotNull Graphics2D g) {
		if (!this.isVisible())
			return;
		
		Shape oldClip = g.getClip();
		
		g.setClip(this.getX(), this.getY(), this.viewWidth, this.viewHeight);

		int startRow = Math.max(0, this.scrollY / this.cellSize);
		int endRow = Math.min(this.rows, (this.scrollY + this.viewHeight) / this.cellSize + 1);
		int startCol = Math.max(0, this.scrollX / this.cellSize);
		int endCol = Math.min(this.columns, (this.scrollX + this.viewWidth) / this.cellSize + 1);
		
		g.translate(-this.scrollX, -this.scrollY);
		
		for (int r = startRow; r < endRow; r++) {
			for (int c = startCol; c < endCol; c++) {
				this.grid[r][c].draw(g);
			}
		}
		
		g.translate(this.scrollX, this.scrollY);
		
		this.drawScrollbars(g);
		
		g.setClip(oldClip);
	}
	
	private void drawScrollbars(@NotNull Graphics2D g) {
		g.setColor(this.settings.getTheme().scrollBar());
		
		// Vertical
		if (this.getHeight() > this.viewHeight) {
			int barH = (this.viewHeight * this.viewHeight) / this.getHeight();
			int barY = (this.scrollY * this.viewHeight) / this.getHeight();
			g.fillRect(this.getX() + this.viewWidth - 6, this.getY() + barY, 4, barH);
		}
		
		// Horizontal
		if (this.getWidth() > this.viewWidth) {
			int barW = (this.viewWidth * this.viewWidth) / this.getWidth();
			int barX = (this.scrollX * this.viewWidth) / this.getWidth();
			g.fillRect(this.getX() + barX, this.getY() + this.viewHeight - 6, barW, 4);
		}
	}
	
	/**
	 * Calls the {@code drawTooltip()} method on the last hovered {@link Cell}.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param e The last event generated by the mouse.
	 */
	@Override
	public void drawTooltip(@NotNull Graphics2D g, MouseEvent e) {
		if (!this.isVisible || this.lastHovered == null || !this.lastHovered.isVisible())
			return;
		
		this.lastHovered.drawTooltip(g, e);
	}
	
	/* ---------- @Override Drawable ---------- */
	/* ---------- @Override Clickable ---------- */
	
	@Override
	public void onClick(@NotNull MouseEvent e) {
		if (!this.isVisible || !this.isActive)
			return;
		
		Cell currentCell = this.getCellFromMouse(e);
		Cell lastCell = this.lastPressed;
		this.lastPressed = null;
		
		if (currentCell == null || currentCell != lastCell)
			return;
		
		currentCell.onClick(e);
	}
	
	@Override
	public void onPress(@NotNull MouseEvent e) {
		if (!this.isVisible || !this.isActive)
			return;
		
		this.lastPressed = this.getCellFromMouse(e);
		
		if (this.lastPressed == null)
			return;
		
		this.lastPressed.onPress(e);
	}
	
	@Override
	public void onRelease(@NotNull MouseEvent e) {
		if (!this.isVisible || !this.isActive)
			return;
		
		if (this.lastPressed == null)
			return;
		
		this.lastPressed.onRelease(e);
	}
	
	/* ---------- @Override Clickable ---------- */
	/* ---------- @Override Hoverable ---------- */
	
	@Override
	public void onExit(@NotNull MouseEvent e) {
		if (this.lastHovered != null) {
			this.lastHovered.setHovered(false);
			this.lastHovered.onExit(e);
			this.lastHovered = null;
		}
	}
	
	@Override
	public void onHover(@NotNull MouseEvent e) {
		if (!this.isVisible || !this.isActive)
			return;
		
		Cell cell = this.getCellFromMouse(e);
		
		if (cell != this.lastHovered) {
			if (this.lastHovered != null) {
				this.lastHovered.setHovered(false);
				this.lastHovered.onExit(e);
			}
			if (cell != null) {
				cell.setHovered(true);
				cell.onEnter(e);
			}
			this.lastHovered = cell;
		}
		
		if (cell != null) {
			cell.onHover(e);
		}
	}
	
	/* ---------- @Override Hoverable ---------- */
	/* ---------- @Override Scrollable ---------- */
	
	@Override
	public void onMouseWheel(@NotNull MouseWheelEvent e) {
		if (e.isShiftDown()) {
			this.scrollX += e.getWheelRotation() * this.cellSize;
		} else {
			this.scrollY += e.getWheelRotation() * this.cellSize;
		}
		
		this.scrollX = Math.max(0, Math.min(this.scrollX, this.getWidth() - this.viewWidth));
		this.scrollY = Math.max(0, Math.min(this.scrollY, this.getHeight() - this.viewHeight));
		
		this.onHover(e);
	}
	
	/* ---------- @Override Scrollable ---------- */
	/* ---------- UTILITY ---------- */
	
	/** From the {@link MouseEvent} gets the coordinates and calculates and returns the cell under the mouse pointer. */
	private @Nullable Cell getCellFromMouse(@NotNull MouseEvent e) {
		int mx = e.getX() - this.getX() + this.scrollX;
		int my = e.getY() - this.getY() + this.scrollY;
		
		if (
				e.getX() < this.getX() ||
				e.getX() > this.getX() + this.viewWidth ||
				e.getY() < this.getY() ||
				e.getY() > this.getY() + this.viewHeight
		)
			return null;
		
		int r = my / this.cellSize;
		int c = mx / this.cellSize;
		
		if (r < 0 || r >= this.rows || c < 0 || c >= this.columns)
			return null;
		
		return this.grid[r][c];
	}
	
	/* ---------- UTILITY ---------- */
}

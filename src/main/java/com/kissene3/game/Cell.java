package com.kissene3.game;

import com.kissene3.console.*;
import com.kissene3.gui.*;
import com.kissene3.gui.components.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;

@SuppressWarnings("unused")
public class Cell extends MyAbstractButton {
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	/** The row coordinate of the cell from the {@link Grid} object. */
	protected final int row;
	
	/** The column coordinate of the cell from the {@link Grid} object. */
	protected final int column;
	
	protected final Grid grid;
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- ATTRIBUTES ---------- */
	
	/** The display state of the cell. */
	protected CellDisplay display;
	
	/** The flag that indicates if the cell has been revealed. */
	protected boolean isRevealed;
	
	/** The flag that indicates if the cell is flagged. */
	protected boolean isFlagged;
	
	/** The flag that indicates if this is the mine that exploded. */
	protected boolean hasExploded;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	/**
	 * Initializes the cell for main.java.com.kissene3.console use.
	 * @param row The row coordinate of the cell from the {@link Grid} object.
	 * @param column The column coordinate of the cell from the {@link Grid} object.
	 * @param grid The actual {@link Grid} object.
	 * */
	public Cell(int row, int column, @NotNull Grid grid) {
		this(row, column, grid, 0, 0, 0);
	}
	
	/**
	 * Initializes the cell for GUI use.
	 * @param row The row coordinate of the cell from the <code>grid</code>.
	 * @param column The column coordinate of the cell from the <code>grid</code>.
	 * @param grid The actual {@link Grid} object.
	 * @param x The x-coordinate of the cell's top-left corner for the GUI.
	 * @param y The y-coordinate of the cell's top-left corner for the GUI.
	 * @param size The size (width and height) of the cell for the GUI.
	 * */
	public Cell(int row, int column, @NotNull Grid grid, int x, int y, int size) {
		super(x, y, size, size);
		this.row = row;
		this.column = column;
		this.grid = grid;
		this.init();
	}
	
	protected Cell(int x, int y, int size) {
		super(x, y, size, size);
		this.grid = null;
		this.row = 0;
		this.column = 0;
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- INITIALIZERS ---------- */
	
	protected void init() {
		this.display = CellDisplay.HIDDEN;
		this.isRevealed = false;
		this.isFlagged = false;
		this.hasExploded = false;
	}
	
	/* ---------- INITIALIZERS ---------- */
	/* ---------- GETTERS ---------- */
	
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
	
	public boolean isMine() {
		return this.display == CellDisplay.MINE;
	}
	
	public boolean hasExploded() {
		return this.hasExploded;
	}
	
	public Theme getTheme() {
		return this.grid.getSettings().getTheme();
	}
	
	/* ---------- GETTERS ---------- */
	/* ---------- SETTERS ---------- */
	
	/** Sets the cell's display to MINE. */
	public void placeMine() {
		this.display = CellDisplay.MINE;
	}
	
	/** Reveals the cell. If the cell is already revealed, or it's flagged, nothing happens. */
	public void reveal() {
		if (this.isFlagged)
			return;
		this.isRevealed = true;
	}

	/** Toggles the flagged state of the cell. If the cell is already revealed nothing happens. */
	public void toggleFlagged() {
		if (this.isRevealed)
			return;
		this.isFlagged = !this.isFlagged;
	}
	
	/** Flags the cell as the one that exploded. */
	public void explode() {
		this.hasExploded = true;
	}
	
	public void setDisplay(@NotNull CellDisplay display) {
		this.display = display;
	}
	
	/* ---------- SETTERS ---------- */
	/* ---------- LOGIC ---------- */
	
	/**
	 * Counts the mines adjacent to the cell and updates the display.
	 * @throws IllegalStateException If the count of adjacent mines is not between 0 and 8. (It shouldn't be possible).
	 */
	public void checkNear() {
		if (this.display == CellDisplay.MINE)
			return;
		int count = 0;
		for (int r = -1; r <= 1; r++)
			for (int c = -1; c <= 1; c++) {
				int row = this.row + r;
				int column = this.column + c;
				if (row < 0 || row >= this.grid.getRows() || column < 0 || column >= this.grid.getColumns() || (r == 0 && c == 0))
					continue;
				if (this.grid.getCell(row, column).isMine())
					count++;
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
	
	/* ---------- LOGIC ---------- */
	/* ---------- @Override Object ---------- */
	
	@Override
	public @NotNull String toString() {
		if (this.isFlagged)
			return  ConsoleColor.RED_BRIGHT + " " + CellDisplay.FLAGGED + " " + ConsoleColor.RESET;
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
	
	/* ---------- @Override Object ---------- */
	/* ---------- @Override Drawable ---------- */
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		int p = this.getWidth() / 16;
		Theme theme = this.getTheme();
		
		this.drawCellBackground(g, theme);
		
		if (!this.isRevealed) {
			this.drawCellShades(g, p, theme);
			
			if (this.isFlagged)
				this.drawFlag(g, p, theme);
		} else {
			this.drawCellBorder(g, p, theme);
			this.drawContent(g, p, theme);
		}
		
		this.drawOverlay(g, theme);
	}
	
	/* ---------- @Override Drawable ---------- */
	/* ---------- #Draw(Graphics) UTILITIES ---------- */
	
	/**
	 * Draws a transparent white overlay over the button when it's hovered.
	 * @param g The {@link Graphics2D} object used for drawing.
	 */
	protected void drawOverlay(@NotNull Graphics2D g, @NotNull Theme theme) {
		if (!this.isVisible || !this.isActive || !this.isHovered || this.isRevealed)
			return;
		
		g.setColor(theme.overlay());
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	/** Draws the background of the cell, it changes the color if it's the mine that exploded. */
	protected void drawCellBackground(@NotNull Graphics2D g, @NotNull Theme theme) {
		g.setColor(this.hasExploded ? theme.backgroundExplodedMine() : theme.background());
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	/**
	 * Draws the shades for the cell when it's not revealed.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawCellShades(@NotNull Graphics2D g, int p, @NotNull Theme theme) {
		int p2 = 2 * p;
		int reducedSize = this.getWidth() - p;
		int reducedSize2 = this.getWidth() - p2;
		
		g.setColor(theme.backgroundLighterShade());
		g.fillRect(this.getX(), this.getY(), reducedSize, p);								// top top-line
		g.fillRect(this.getX(), this.getY(), reducedSize2, p2);								// bottom top-line
		g.fillRect(this.getX(), this.getY(), p, reducedSize);								// left left-line
		g.fillRect(this.getX(), this.getY(), p2, reducedSize2);								// right left-line
		
		g.setColor(theme.backgroundDarkerShade());
		g.fillRect(this.getX() + reducedSize, this.getY() + p, p, reducedSize);		// right right-line
		g.fillRect(this.getX() + reducedSize2, this.getY() + p2, p, reducedSize2);	// left right-line
		g.fillRect(this.getX() + p, this.getY() + reducedSize, reducedSize, p);		// bottom bottom-line
		g.fillRect(this.getX() + p2, this.getY() + reducedSize2, reducedSize2, p);	// top bottom-line
	}
	
	/**
	 * Draws the flag.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawFlag(@NotNull Graphics2D g, int p, @NotNull Theme theme) {
		g.setColor(theme.flag());
		g.fillRect(this.getX() + 7 * p, this.getY() + 3 * p, 2 * p, 5 * p);	// right column
		g.fillRect(this.getX() + 5 * p, this.getY() + 4 * p, 2 * p, 3 * p);	// middle column
		g.fillRect(this.getX() + 4 * p, this.getY() + 5 * p, p, p);						// left dot
		
		g.setColor(theme.flagBase());
		g.fillRect(this.getX() + 8 * p, this.getY() + 8 * p, p, 2 * p);				// vertical pole
		g.fillRect(this.getX() + 6 * p, this.getY() + 10 * p, 4 * p, p);			// top row
		g.fillRect(this.getX() + 4 * p, this.getY() + 11 * p, 8 * p, 2 * p);	// bottom row
		
		if (this.isMine() || this.grid.getGameStatus() != Status.LOST)
			return;
		
		this.drawCross(g, p, theme);
	}
	
	/**
	 * Draws a cross over the cell.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawCross(@NotNull Graphics2D g, int p, @NotNull Theme theme) {
		g.setStroke(new BasicStroke(p));
		g.setColor(theme.cross());
		g.drawLine(this.getX() + p, this.getY() + p, this.getX() + this.getWidth() - p, this.getY() + this.getHeight() - p);
		g.drawLine(this.getX() + this.getWidth() - p, this.getY() + p, this.getX() + p, this.getY() + this.getHeight() - p);
		g.setStroke(new BasicStroke(1));
	}
	
	/**
	 * Draws the border for the cell when it's revealed.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawCellBorder(@NotNull Graphics2D g, int p, @NotNull Theme theme) {
		g.setColor(theme.border());
		g.fillRect(this.getX(), this.getY(), this.getWidth(), p);	// top line
		g.fillRect(this.getX(), this.getY(), p, this.getHeight());	// left line
	}
	
	/**
	 * Draws the content of the cell when it's revealed (mine or number).
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawContent(@NotNull Graphics2D g, int p, @NotNull Theme theme) {
		if (this.display == CellDisplay.MINE) {
			this.drawMine(g, p, theme);
			return;
		}
		if (this.display == CellDisplay.ZERO)
			return;
		
		g.setColor(theme.getNumberColor(Integer.parseInt(this.display.getSymbol())));
		
		switch (this.display) {
			case ONE   -> this.drawNumberOne(g, p);
			case TWO   -> this.drawNumberTwo(g, p);
			case THREE -> this.drawNumberThree(g, p);
			case FOUR  -> this.drawNumberFour(g, p);
			case FIVE  -> this.drawNumberFive(g, p);
			case SIX   -> this.drawNumberSix(g, p);
			case SEVEN -> this.drawNumberSeven(g, p);
			case EIGHT -> this.drawNumberEight(g, p);
		}
	}
	
	/**
	 * Draws the mine.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawMine(@NotNull Graphics2D g, int p, @NotNull Theme theme) {
		g.setColor(theme.mine());
		g.fillRect(this.getX() + 6 * p, this.getY() + 4 * p, 5 * p, 9 * p);	// vertical rect
		g.fillRect(this.getX() + 4 * p, this.getY() + 6 * p, 9 * p, 5 * p);	// horizontal rect
		g.fillRect(this.getX() + 5 * p, this.getY() + 5 * p, 7 * p, 7 * p);	// centered square
		g.fillRect(this.getX() + 8 * p, this.getY() + 2 * p, p, 13 * p);			// vertical middle line
		g.fillRect(this.getX() + 2 * p, this.getY() + 8 * p, 13 * p, p);			// horizontal middle line
		g.fillRect(this.getX() + 4 * p, this.getY() + 4 * p, p, p);						// top-left dot
		g.fillRect(this.getX() + 12 * p, this.getY() + 4 * p, p, p);						// top-right dot
		g.fillRect(this.getX() + 4 * p, this.getY() + 12 * p, p, p);						// bottom-left dot
		g.fillRect(this.getX() + 12 * p, this.getY() + 12 * p, p, p);						// bottom-right dot
		
		g.setColor(theme.mineShade());
		g.fillRect(this.getX() + 6 * p, this.getY() + 6 * p, 2 * p, 2 * p);	// light dot
	}
	
	/**
	 * Draws the number 1.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawNumberOne(@NotNull Graphics2D g, int p) {
		g.fillRect(this.getX() + 8 * p, this.getY() + 3 * p, 2 * p, 8 * p); 	// Right column
		g.fillRect(this.getX() + 7 * p, this.getY() + 4 * p, p, 7 * p);				// Left column
		g.fillRect(this.getX() + 6 * p, this.getY() + 5 * p, p, p);						// Top appendix
		g.fillRect(this.getX() + 5 * p, this.getY() + 6 * p, 2 * p, p);				// Bottom appendix
		g.fillRect(this.getX() + 5 * p, this.getY() + 11 * p, 7 * p, 2 * p);	// Bottom row
	}
	
	/**
	 * Draws the number 2.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawNumberTwo(@NotNull Graphics2D g, int p) {
		g.fillRect(this.getX() + 3 * p, this.getY() + 4 * p, 3 * p, 2 * p);	// top-left appendix
		g.fillRect(this.getX() + 4 * p, this.getY() + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(this.getX() + 10 * p, this.getY() + 4 * p, 3 * p, 3 * p);	// right square
		g.fillRect(this.getX() + 8 * p, this.getY() + 7 * p, 4 * p, p);				// first middle row
		g.fillRect(this.getX() + 6 * p, this.getY() + 8 * p, 5 * p, p);				// second middle row
		g.fillRect(this.getX() + 4 * p, this.getY() + 9 * p, 5 * p, p);				// third middle row
		g.fillRect(this.getX() + 3 * p, this.getY() + 10 * p, 4 * p, 3 * p);	// bottom-left corner
		g.fillRect(this.getX() + 7 * p, this.getY() + 11 * p, 6 * p, 2 * p);	// bottom row
	}
	
	/**
	 * Draws the number 3.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawNumberThree(@NotNull Graphics2D g, int p) {
		g.fillRect(this.getX() + 3 * p, this.getY() + 3 * p, 9 * p, 2 * p);	// top row
		g.fillRect(this.getX() + 6 * p, this.getY() + 7 * p, 6 * p, 2 * p);	// middle row
		g.fillRect(this.getX() + 3 * p, this.getY() + 11 * p, 9 * p, 2 * p);	// bottom row
		g.fillRect(this.getX() + 10 * p, this.getY() + 4 * p, 3 * p, 3 * p);	// top column
		g.fillRect(this.getX() + 10 * p, this.getY() + 9 * p, 3 * p, 3 * p);	// bottom column
	}
	
	/**
	 * Draws the number 4.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawNumberFour(@NotNull Graphics2D g, int p) {
		g.fillRect(this.getX() + 5 * p, this.getY() + 3 * p, 3 * p, 2 * p);	// top diagonal
		g.fillRect(this.getX() + 4 * p, this.getY() + 5 * p, 3 * p, 2 * p);	// bottom diagonal
		g.fillRect(this.getX() + 3 * p, this.getY() + 7 * p, 10 * p, 2 * p);	// horizontal line
		g.fillRect(this.getX() + 9 * p, this.getY() + 3 * p, 3 * p, 10 * p);	// vertical line
	}
	
	/**
	 * Draws the number 5.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawNumberFive(@NotNull Graphics2D g, int p) {
		g.fillRect(this.getX() + 3 * p, this.getY() + 3 * p, 10 * p, 2 * p);	// top row
		g.fillRect(this.getX() + 3 * p, this.getY() + 5 * p, 3 * p, 2 * p);	// left column
		g.fillRect(this.getX() + 3 * p, this.getY() + 7 * p, 9 * p, 2 * p);	// middle row
		g.fillRect(this.getX() + 10 * p, this.getY() + 8 * p, 3 * p, 4 * p);	// right column
		g.fillRect(this.getX() + 3 * p, this.getY() + 11 * p, 9 * p, 2 * p);	// bottom row
	}
	
	/**
	 * Draws the number 6.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawNumberSix(@NotNull Graphics2D g, int p) {
		g.fillRect(this.getX() + 4 * p, this.getY() + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(this.getX() + 3 * p, this.getY() + 4 * p, 3 * p, 8 * p);	// left column
		g.fillRect(this.getX() + 6 * p, this.getY() + 7 * p, 6 * p, 2 * p);	// middle row
		g.fillRect(this.getX() + 4 * p, this.getY() + 11 * p, 8 * p, 2 * p);	// bottom row
		g.fillRect(this.getX() + 10 * p, this.getY() + 8 * p, 3 * p, 4 * p);	// right column
	}
	
	/**
	 * Draws the number 7.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawNumberSeven(@NotNull Graphics2D g, int p) {
		g.fillRect(this.getX() + 3 * p, this.getY() + 3 * p, 10 * p, 2 * p);	// row
		g.fillRect(this.getX() + 10 * p, this.getY() + 5 * p, 3 * p, 2 * p);	// first diagonal
		g.fillRect(this.getX() + 9 * p, this.getY() + 7 * p, 3 * p, 2 * p);	// second diagonal
		g.fillRect(this.getX() + 8 * p, this.getY() + 9 * p, 3 * p, 2 * p);	// third diagonal
		g.fillRect(this.getX() + 7 * p, this.getY() + 11 * p, 3 * p, 2 * p);	// fourth diagonal
	}
	
	/**
	 * Draws the number 8.
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param p The pixel size of the cell.
	 */
	protected void drawNumberEight(@NotNull Graphics2D g, int p) {
		g.fillRect(this.getX() + 4 * p, this.getY() + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(this.getX() + 4 * p, this.getY() + 7 * p, 8 * p, 2 * p);	// middle row
		g.fillRect(this.getX() + 4 * p, this.getY() + 11 * p, 8 * p, 2 * p);	// bottom row
		g.fillRect(this.getX() + 3 * p, this.getY() + 4 * p, 3 * p, 3 * p);	// top-left column
		g.fillRect(this.getX() + 10 * p, this.getY() + 4 * p, 3 * p, 3 * p);	// top-right column
		g.fillRect(this.getX() + 3 * p, this.getY() + 9 * p, 3 * p, 3 * p);	// bottom-left column
		g.fillRect(this.getX() + 10 * p, this.getY() + 9 * p, 3 * p, 3 * p);	// bottom-right column
	}
	
	/* ---------- #Draw(Graphics) UTILITIES ---------- */
	/* ---------- @Override Clickable ---------- */
	
	@Override
	public void onClick(@NotNull MouseEvent e) {
		if (!this.isVisible || !this.isActive)
			return;
		
		if (this.isRevealed)
			return;
		
		int mouseButton = e.getButton();
		
		if (mouseButton == MouseEvent.BUTTON1 && !this.isFlagged) {
			this.grid.act("r", this.row, this.column);
		} else if (mouseButton == MouseEvent.BUTTON3) {
			this.grid.act("f", this.row, this.column);
		}
	}
	
	/* ---------- @Override Clickable ---------- */
}

package com.kissene3.game;

import com.kissene3.console.*;
import com.kissene3.gui.*;
import com.kissene3.gui.components.*;
import com.kissene3.gui.screens.MenuScreen;
import com.kissene3.gui.utilities.MyUtilities;
import com.kissene3.gui.utilities.MyUtilities.Dimension;
import com.kissene3.gui.utilities.Textures;
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
	
	private final Textures textures;
	
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
		this.textures = new Textures() {
			@Override
			protected MyUtilities.@NotNull Dimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, EXPLODED, CLOSED, REVEALED, MINE, FLAG, CROSS,
						 ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT ->  new Dimension(
							Cell.this.grid.getSettings().getCellSize().getSize(),
							Cell.this.grid.getSettings().getCellSize().getSize()
					);
					default ->  throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, MyUtilities.@NotNull Dimension dimension) {
				switch (key) {
					case BACKGROUND -> this.drawBackground(
							g,
							new MyUtilities.Coordinates(0, 0),
							this.getAssetDimension(Key.BACKGROUND),
							Cell.this.grid.getSettings().getTheme().background()
					);
					case EXPLODED -> this.drawBackground(
							g,
							new MyUtilities.Coordinates(0, 0),
							this.getAssetDimension(Key.EXPLODED),
							Cell.this.grid.getSettings().getTheme().backgroundExplodedMine()
					);
					case CLOSED -> this.drawClosed(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							this.getAssetDimension(Key.CLOSED),
							Cell.this.grid.getSettings().getTheme().backgroundLighterShade(),
							Cell.this.grid.getSettings().getTheme().backgroundDarkerShade()
					);
					case REVEALED -> this.drawRevealed(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							this.getAssetDimension(Key.REVEALED),
							Cell.this.grid.getSettings().getTheme().border()
					);
					case MINE -> this.drawMine(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().mine(),
							Cell.this.grid.getSettings().getTheme().mineShade()
					);
					case FLAG -> this.drawFlag(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().flag(),
							Cell.this.grid.getSettings().getTheme().flagBase()
					);
					case CROSS -> this.drawCross(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							this.getAssetDimension(Key.CROSS),
							Cell.this.grid.getSettings().getTheme().cross()
					);
					case ONE -> this.drawNumberOne(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().numberOne()
					);
					case TWO -> this.drawNumberTwo(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().numberTwo()
					);
					case THREE -> this.drawNumberThree(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().numberThree()
					);
					case FOUR -> this.drawNumberFour(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().numberFour()
					);
					case FIVE -> this.drawNumberFive(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().numberFive()
					);
					case SIX -> this.drawNumberSix(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().numberSix()
					);
					case SEVEN -> this.drawNumberSeven(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().numberSeven()
					);
					case EIGHT -> this.drawNumberEight(
							g,
							Cell.this.grid.getSettings().getCellSize().getSize() / 16,
							new MyUtilities.Coordinates(0, 0),
							Cell.this.grid.getSettings().getTheme().numberEight()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
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
		
		if (this.hasExploded)
			g.drawImage(this.textures.getAsset(Textures.Key.EXPLODED), this.getX(), this.getY(), null);
		else
			g.drawImage(this.textures.getAsset(Textures.Key.BACKGROUND), this.getX(), this.getY(), null);
		
		if (!this.isRevealed) {
			g.drawImage(this.textures.getAsset(Textures.Key.CLOSED), this.getX(), this.getY(), null);
			if (this.isFlagged) {
				g.drawImage(this.textures.getAsset(Textures.Key.FLAG), this.getX(), this.getY(), null);
				if (!this.isMine() && this.grid.getGameStatus() == Status.LOST)
					g.drawImage(this.textures.getAsset(Textures.Key.CROSS), this.getX(), this.getY(), null);
			}
		} else {
			g.drawImage(this.textures.getAsset(Textures.Key.REVEALED), this.getX(), this.getY(), null);
			
			switch (this.display) {
				case MINE -> g.drawImage(this.textures.getAsset(Textures.Key.MINE), this.getX(), this.getY(), null);
				case ONE -> g.drawImage(this.textures.getAsset(Textures.Key.ONE), this.getX(), this.getY(), null);
				case TWO -> g.drawImage(this.textures.getAsset(Textures.Key.TWO), this.getX(), this.getY(), null);
				case THREE -> g.drawImage(this.textures.getAsset(Textures.Key.THREE), this.getX(), this.getY(), null);
				case FOUR -> g.drawImage(this.textures.getAsset(Textures.Key.FOUR), this.getX(), this.getY(), null);
				case FIVE -> g.drawImage(this.textures.getAsset(Textures.Key.FIVE), this.getX(), this.getY(), null);
				case SIX -> g.drawImage(this.textures.getAsset(Textures.Key.SIX), this.getX(), this.getY(), null);
				case SEVEN -> g.drawImage(this.textures.getAsset(Textures.Key.SEVEN), this.getX(), this.getY(), null);
				case EIGHT -> g.drawImage(this.textures.getAsset(Textures.Key.EIGHT), this.getX(), this.getY(), null);
				default -> {}
			}
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

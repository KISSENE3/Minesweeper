package com.kissene3.gui.game;

import com.kissene3.game.*;
import com.kissene3.gui.models.Settings;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.components.Smiley;
import com.kissene3.gui.interfaces.*;
import com.kissene3.gui.interfaces.Component;
import com.kissene3.gui.screens.*;
import com.kissene3.gui.util.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
public class GuiGrid extends Grid<GuiCell> implements Component, Clickable, Hoverable, Drawable, Scrollable {
	protected MyRectangle bounds;
	protected Screen parentScreen;
	protected Smiley smiley;
	protected Settings settings;
	protected Textures texture;
	protected boolean isActive;
	protected boolean isVisible;
	protected boolean isHovered;
	protected int lastSize = -1;
	protected GuiCell lastHovered;
	protected GuiCell lastPressed;
	protected int scrollX = 0;
	protected int scrollY = 0;
	protected int viewWidth;
	protected int viewHeight;
	
	public GuiGrid(@NotNull MyCoordinates coordinates, @NotNull Difficulty difficulty, @NotNull Screen parentScreen, @Nullable Smiley smiley) {
		super(difficulty);
		this.parentScreen = parentScreen;
		this.settings = this.parentScreen.getManager().getSettings();
		this.bounds = new MyRectangle(coordinates, new MyDimension(
				difficulty.width() * this.settings.getCellSize().getSize(),
				difficulty.height() * this.settings.getCellSize().getSize()
		));
		this.smiley = smiley;
		this.texture = this.createTexture();
		TextureManager.addPack(this.texture);
		this.isVisible = true;
		this.isActive = true;
	}
	
	public GuiGrid(@NotNull MyCoordinates coordinates, int gameWidth, int gameHeight, @NotNull BigInteger cellStates, @NotNull Screen parentScreen, @Nullable Smiley smiley) {
		super(gameWidth, gameHeight, cellStates);
		this.parentScreen = parentScreen;
		this.settings = this.parentScreen.getManager().getSettings();
		this.bounds = new MyRectangle(coordinates, new MyDimension(
				gameWidth * this.settings.getCellSize().getSize(),
				gameHeight * this.settings.getCellSize().getSize()
		));
		this.smiley = smiley;
		this.texture = this.createTexture();
		TextureManager.addPack(this.texture);
		this.isVisible = true;
		this.isActive = true;
	}
	
	protected @NotNull Textures createTexture() {
		return new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				int size = GuiGrid.this.getCellSize();
				return switch (key) {
					case BACKGROUND, EXPLODED, CLOSED, REVEALED, MINE, FLAG, CROSS,
						 ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT ->
							new MyDimension(size, size);
					default ->  throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull MyDimension dimension) {
				MyCoordinates coordinates = new MyCoordinates(0, 0);
				Theme theme = GuiGrid.this.getSettings().getTheme();
				int p = GuiGrid.this.getCellSize() / 16;
				
				switch (key) {
					case BACKGROUND -> this.drawBackground(g, coordinates, dimension, theme.background());
					case EXPLODED -> this.drawBackground(g, coordinates, dimension, theme.backgroundExplodedMine());
					case CLOSED -> this.drawClosed(g, p, coordinates, dimension, theme.backgroundLighterShade(), theme.backgroundDarkerShade());
					case REVEALED -> this.drawRevealed(g, p, coordinates, dimension, theme.border());
					case MINE -> this.drawMine(g, p, coordinates, theme.mine(), theme.mineShade());
					case FLAG -> this.drawFlag(g, p, coordinates, theme.flag(), theme.flagBase());
					case CROSS -> this.drawCross(g, p, coordinates, dimension, theme.cross());
					case ONE -> this.drawNumberOne(g, p, coordinates, theme.numberOne());
					case TWO -> this.drawNumberTwo(g, p, coordinates, theme.numberTwo());
					case THREE -> this.drawNumberThree(g, p, coordinates, theme.numberThree());
					case FOUR -> this.drawNumberFour(g, p, coordinates, theme.numberFour());
					case FIVE -> this.drawNumberFive(g, p, coordinates, theme.numberFive());
					case SIX -> this.drawNumberSix(g, p, coordinates, theme.numberSix());
					case SEVEN -> this.drawNumberSeven(g, p, coordinates, theme.numberSeven());
					case EIGHT -> this.drawNumberEight(g, p, coordinates, theme.numberEight());
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
	}
	
	@Override
	public void createCells() {
		this.grid = new GuiCell[this.rows][this.columns];
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.columns; c++) {
				int cellX = this.getX() + c * this.getCellSize();
				int cellY = this.getY() + r * this.getCellSize();
				this.grid[r][c] = new GuiCell(r, c, this, cellX, cellY);
			}
		}
	}
	
	@Override
	protected GuiCell[][] createGridArray(int rows, int columns) {
		return new GuiCell[rows][columns];
	}
	
	@Override
	protected GuiCell createCellFromState(int r, int c, int bitState) {
		int cellX = this.getX() + c * this.getCellSize();
		int cellY = this.getY() + r * this.getCellSize();
		return new GuiCell(r, c, this, cellX, cellY, bitState);
	}
	
	@Override
	public void showAllMines() {
		if (smiley != null)
			this.smiley.setGameWon(false);
		for (GuiCell[] rows : this.grid) {
			for (GuiCell cell : rows) {
				cell.setActive(false);
				if (cell.isMine())
					cell.reveal();
			}
		}
	}
	
	@Override
	public void flagAllMines() {
		if (smiley != null)
			this.smiley.setGameWon(true);
		for (GuiCell[] rows : this.grid) {
			for (GuiCell cell : rows) {
				cell.setActive(false);
				if (cell.isMine() && !cell.isFlagged()) cell.toggleFlagged();
			}
		}
	}
	
	public void setSmiley(@NotNull Smiley smiley) {
		this.smiley = smiley;
	}
	
	public void setSettings(@NotNull Settings settings) {
		this.settings = settings;
	}
	
	public @NotNull Settings getSettings() {
		return this.settings;
	}
	
	public @NotNull Textures getTextures() {
		return this.texture;
	}
	
	public int getCellSize() {
		return this.settings.getCellSize().getSize();
	}
	
	public void setViewport(int w, int h) {
		this.viewWidth = w;
		this.viewHeight = h;
	}
	
	public void updateScroll(int sx, int sy) {
		this.scrollX = sx;
		this.scrollY = sy;
	}
	
	protected void onBoundsChanged() {
		if (this.grid == null || this.grid[0][0] == null)
			return;
		
		int expectedW = this.columns * this.getCellSize();
		int expectedH = this.rows * this.getCellSize();
		
		this.bounds = new MyRectangle(this.getX(), this.getY(), expectedW, expectedH);
		
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.columns; c++) {
				GuiCell cell = this.grid[r][c];
				int cellX = this.getX() + (c * this.getCellSize());
				int cellY = this.getY() + (r * this.getCellSize());
				cell.setBounds(cellX, cellY, this.getCellSize(), this.getCellSize());
			}
		}
		
		this.scrollX = Math.max(0, Math.min(this.scrollX, this.getWidth() - this.viewWidth));
		this.scrollY = Math.max(0, Math.min(this.scrollY, this.getHeight() - this.viewHeight));
	}
	
	@Override
	public final void setBounds(int x, int y, int width, int height) {
		this.setBounds(new MyRectangle(x, y, width, height));
	}
	
	@Override
	public final void setBounds(@NotNull MyRectangle bounds) {
		this.bounds = requireNonNull(bounds);
		this.onBoundsChanged();
	}
	
	@Override
	public int getX() {
		return this.bounds.x();
	}
	
	@Override
	public int getY() {
		return this.bounds.y();
	}
	
	@Override
	public int getWidth() {
		return this.columns * this.getCellSize();
	}
	
	@Override
	public int getHeight() {
		return this.rows * this.getCellSize();
	}
	
	@Override
	public boolean isActive() {
		return this.isActive;
	}
	
	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	@Override
	public boolean contains(int mx, int my) {
		return this.bounds.contains(mx, my);
	}
	
	@Override
	public void dispose() {
		this.texture.dispose();
	}
	
	protected @Nullable GuiCell getCellFromMouse(@NotNull MouseEvent e) {
		int mx = e.getX() - this.getX() + this.scrollX;
		int my = e.getY() - this.getY() + this.scrollY;
		
		if (
				e.getX() < this.getX() ||
						e.getX() > this.getX() + this.viewWidth ||
						e.getY() < this.getY() ||
						e.getY() > this.getY() + this.viewHeight
		)
			return null;
		
		int r = my / this.getCellSize();
		int c = mx / this.getCellSize();
		
		if (r < 0 || r >= this.rows || c < 0 || c >= this.columns)
			return null;
		
		return this.grid[r][c];
	}
	
	protected void drawScrollbars(@NotNull Graphics2D g) {
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
	
	@Override
	public boolean isVisible() {
		return this.isVisible;
	}
	
	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		int currentSize = this.getCellSize();
		if (currentSize != this.lastSize) {
			if (parentScreen instanceof GameScreen screen)
				screen.moveGrid();
			else
				this.onBoundsChanged();
			this.lastSize = currentSize;
		}
		
		if (!this.isVisible())
			return;
		
		Shape oldClip = g.getClip();
		
		g.setClip(this.getX(), this.getY(), this.viewWidth, this.viewHeight);
		
		int startRow = Math.max(0, this.scrollY / this.getCellSize());
		int endRow = Math.min(this.rows, (this.scrollY + this.viewHeight) / this.getCellSize() + 1);
		int startCol = Math.max(0, this.scrollX / this.getCellSize());
		int endCol = Math.min(this.columns, (this.scrollX + this.viewWidth) / this.getCellSize() + 1);
		
		g.translate(-this.scrollX, -this.scrollY);
		
		for (int r = startRow; r < endRow; r++) {
			for (int c = startCol; c < endCol; c++) {
				if (!this.grid[r][c].isVisible()) continue;
				this.grid[r][c].draw(g);
			}
		}
		
		g.translate(this.scrollX, this.scrollY);
		
		this.drawScrollbars(g);
		
		g.setClip(oldClip);
	}
	
	@Override
	public void drawTooltip(@NotNull Graphics2D g, MouseEvent e) {
		if (!this.isVisible || this.lastHovered == null || !this.lastHovered.isVisible())
			return;
		
		this.lastHovered.drawTooltip(g, e);
	}
	
	@Override
	public void onClick(@NotNull MouseEvent e) {
		if (!this.isVisible || !this.isActive)
			return;
		
		GuiCell currentCell = this.getCellFromMouse(e);
		GuiCell lastCell = this.lastPressed;
		this.lastPressed = null;
		
		if (currentCell == null || currentCell != lastCell)
			return;
		
		currentCell.onClick(e);
	}
	
	@Override
	public void onPress(@NotNull MouseEvent e) {
		if (!this.isVisible || !this.isActive)
			return;
		
		if (smiley != null)
			this.smiley.setMouseDown(true);
		
		this.lastPressed = this.getCellFromMouse(e);
		
		if (this.lastPressed == null)
			return;
		
		this.lastPressed.onPress(e);
	}
	
	@Override
	public void onRelease(@NotNull MouseEvent e) {
		if (!this.isVisible || !this.isActive)
			return;
		
		if (smiley != null)
			this.smiley.setMouseDown(false);
		
		if (this.lastPressed == null)
			return;
		
		this.lastPressed.onRelease(e);
	}
	
	@Override
	public boolean isHovered() {
		return this.isHovered;
	}
	
	@Override
	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}
	
	@Override
	public void onEnter(@NotNull MouseEvent e) {}
	
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
		
		GuiCell cell = this.getCellFromMouse(e);
		
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
	
	@Override
	public void onMouseWheel(@NotNull MouseWheelEvent e) {
		if (e.isShiftDown()) {
			this.scrollX += e.getWheelRotation() * this.getCellSize();
		} else {
			this.scrollY += e.getWheelRotation() * this.getCellSize();
		}
		
		this.scrollX = Math.max(0, Math.min(this.scrollX, this.getWidth() - this.viewWidth));
		this.scrollY = Math.max(0, Math.min(this.scrollY, this.getHeight() - this.viewHeight));
		
		this.onHover(e);
	}
}

package com.kissene3.gui;

import com.kissene3.game.Difficulty;
import com.kissene3.game.Grid;
import com.kissene3.gui.interfaces.*;
import com.kissene3.gui.interfaces.Component;
import com.kissene3.gui.utilities.MyUtilities.Coordinates;
import com.kissene3.gui.utilities.MyUtilities.Dimension;
import com.kissene3.gui.utilities.MyUtilities.Rectangle;
import com.kissene3.gui.utilities.TextureManager;
import com.kissene3.gui.utilities.Textures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
public class GUIGrid extends Grid<GUICell> implements Component, Clickable, Hoverable, Drawable, Scrollable {
	protected Rectangle bounds;
	protected Settings settings;
	protected Textures texture;
	protected boolean isActive;
	protected boolean isVisible;
	protected boolean isHovered;
	protected int lastSize = -1;
	protected GUICell lastHovered;
	protected GUICell lastPressed;
	protected int scrollX = 0;
	protected int scrollY = 0;
	protected int viewWidth;
	protected int viewHeight;
	
	public GUIGrid(@NotNull Coordinates coordinates, @NotNull Difficulty difficulty, @NotNull Settings settings) {
		super(difficulty);
		this.settings = settings;
		this.bounds = new Rectangle(coordinates, new Dimension(
				difficulty.width() * this.settings.getCellSize().getSize(),
				difficulty.height() * this.settings.getCellSize().getSize()
		));
		this.texture = new Textures() {
			@Override
			protected @NotNull Dimension getAssetDimension(@NotNull Key key) {
				int size = GUIGrid.this.getCellSize();
				return switch (key) {
					case BACKGROUND, EXPLODED, CLOSED, REVEALED, MINE, FLAG, CROSS,
						 ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT ->
							new Dimension(size, size);
					default ->  throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull Dimension dimension) {
				Coordinates coordinates = new Coordinates(0, 0);
				Theme theme = GUIGrid.this.getSettings().getTheme();
				int p = GUIGrid.this.getCellSize() / 16;
				
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
		this.isVisible = true;
		this.isActive = true;
		TextureManager.addPack(this.texture);
	}
	
	@Override
	public void createCells() {
		this.grid = new GUICell[this.rows][this.columns];
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.columns; c++) {
				this.grid[r][c] = new GUICell(r, c, this, this.getX() + c * this.getCellSize(), this.getY() + r * this.getCellSize());
			}
		}
	}
	
	@Override
	public void showAllMines() {
		for (GUICell[] rows : this.grid) {
			for (GUICell cell : rows) {
				cell.setActive(false);
				if (cell.isMine())
					cell.reveal();
			}
		}
	}
	
	@Override
	public void flagAllMines() {
		for (GUICell[] rows : this.grid) {
			for (GUICell cell : rows) {
				cell.setActive(false);
				if (cell.isMine() && !cell.isFlagged()) cell.toggleFlagged();
			}
		}
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
		
		this.bounds = new Rectangle(this.getX(), this.getY(), expectedW, expectedH);
		
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.columns; c++) {
				GUICell cell = this.grid[r][c];
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
		this.setBounds(new Rectangle(x, y, width, height));
	}
	
	@Override
	public final void setBounds(@NotNull Rectangle bounds) {
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
		return this.bounds.width();
	}
	
	@Override
	public int getHeight() {
		return this.bounds.height();
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
	
	protected @Nullable GUICell getCellFromMouse(@NotNull MouseEvent e) {
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
		
		GUICell currentCell = this.getCellFromMouse(e);
		GUICell lastCell = this.lastPressed;
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
		
		GUICell cell = this.getCellFromMouse(e);
		
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

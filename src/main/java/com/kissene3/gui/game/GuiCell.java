package com.kissene3.gui.game;

import com.kissene3.game.*;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.interfaces.*;
import com.kissene3.gui.interfaces.Component;
import com.kissene3.gui.util.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;

import static java.util.Objects.requireNonNull;

public class GuiCell extends Cell<GuiGrid> implements Component, Clickable, Hoverable, Drawable {
	/* ---------- ATTRIBUTES ---------- */
	
	private final Textures textures;
	
	private MyRectangle bounds;
	private boolean isActive;
	private boolean isVisible;
	private boolean isHovered;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public GuiCell(int row, int column, @NotNull GuiGrid grid, int x, int y) {
		super(row, column, grid);
		this.textures = grid.getTextures();
		this.bounds = new MyRectangle(x, y, grid.getCellSize(), grid.getCellSize());
		this.isActive = true;
		this.isVisible = true;
	}
	
	public GuiCell(int row, int column, @NotNull GuiGrid grid, int x, int y, int bitState) {
		super(row, column, grid, bitState);
		this.textures = grid.getTextures();
		this.bounds = new MyRectangle(x, y, grid.getCellSize(), grid.getCellSize());
		this.isActive = true;
		this.isVisible = true;
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- SETTERS ---------- */
	
	public void setX(int x) {
		this.setBounds(this.bounds.withX(x));
	}
	
	public void setY(int y) {
		this.setBounds(this.bounds.withY(y));
	}
	
	public void setSize(int size) {
		this.setBounds(this.bounds.withDimensions(new MyDimension(size, size)));
	}
	
	/* ---------- SETTERS ---------- */
	/* ---------- @Override Component ---------- */
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.setBounds(new MyRectangle(x, y, width, height));
	}
	
	@Override
	public void setBounds(@NotNull MyRectangle bounds) {
		this.bounds = requireNonNull(bounds);
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
	public void dispose() {}
	
	/* ---------- @Override Component ---------- */
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
	
	@Override
	public void onPress(@NotNull MouseEvent e) {}
	
	@Override
	public void onRelease(@NotNull MouseEvent e) {}
	
	/* ---------- @Override Clickable ---------- */
	/* ---------- @Override Hoverable ---------- */
	
	@Override
	public boolean isHovered() {
		return this.isHovered;
	}
	
	@Override
	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}
	
	@Override
	public void onHover(@NotNull MouseEvent e) {}
	
	@Override
	public void onEnter(@NotNull MouseEvent e) {}
	
	@Override
	public void onExit(@NotNull MouseEvent e) {}
	
	/* ---------- @Override Hoverable ---------- */
	/* ---------- @Override Drawable ---------- */
	
	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	@Override
	public boolean isVisible() {
		return this.isVisible;
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		Color oldColor = g.getColor();
		Theme theme = this.grid.getSettings().getTheme();
		
		if (this.hasExploded && this.isRevealed)
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
		
		// Overlay
		if (!this.isActive || !this.isHovered || this.isRevealed)
			return;
		
		g.setColor(theme.overlay());
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		g.setColor(oldColor);
	}
	
	/* ---------- @Override Drawable ---------- */
}

package com.kissene3.gui.components;

import com.kissene3.gui.screens.*;
import com.kissene3.gui.util.*;
import com.kissene3.gui.util.*;
import com.kissene3.io.AppLogger;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("unused")
public abstract class Smiley extends MinesweeperButton {
	/* ---------- ATTRIBUTES ---------- */
	
	protected Screen parentScreen;
	protected boolean mouseDown = false;
	protected Boolean gameWon = null;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public Smiley(int x, int y, int width, int height, @NotNull Screen parentScreen) {
		this(new MyRectangle(x, y, width, height), parentScreen);
	}
	
	public Smiley(@NotNull MyCoordinates topLeft, @NotNull MyDimension dimensions, @NotNull Screen parentScreen) {
		this(new MyRectangle(topLeft, dimensions), parentScreen);
	}
	
	public Smiley(@NotNull MyRectangle bounds, @NotNull Screen parentScreen) {
		super(bounds);
		this.parentScreen = parentScreen;
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	@Override
	protected Textures createTexture() {
		return new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, CLOSED, REVEALED ->
							new MyDimension(Smiley.this.getWidth(), Smiley.this.getHeight());
					case SMILEY_HAPPY, SMILEY_OOH, SMILEY_COOL, SMILEY_DEAD ->
							new MyDimension(Smiley.this.getWidth() - 6 * Smiley.this.getP(), Smiley.this.getHeight() - 6 * Smiley.this.getP());
					default -> throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull MyDimension dimension) {
				switch (key) {
					case BACKGROUND -> this.drawBackground(
							g,
							new MyCoordinates(0, 0),
							dimension,
							Smiley.this.getTheme().background()
					);
					case REVEALED -> this.drawRevealed(
							g,
							Smiley.this.getP(),
							new MyCoordinates(0, 0),
							dimension,
							Smiley.this.getTheme().border()
					);
					case CLOSED -> this.drawClosed(
							g,
							Smiley.this.getP(),
							new MyCoordinates(0, 0),
							dimension,
							Smiley.this.getTheme().backgroundLighterShade(),
							Smiley.this.getTheme().backgroundDarkerShade()
					);
					case SMILEY_HAPPY -> this.drawSmileyHappy(
							g,
							Smiley.this.getP(),
							new MyCoordinates(0, 0),
							Smiley.this.getTheme().smileyBase(),
							Smiley.this.getTheme().smileyBorder()
					);
					case SMILEY_OOH -> this.drawSmileyOoh(
							g,
							Smiley.this.getP(),
							new MyCoordinates(0, 0),
							Smiley.this.getTheme().smileyBase(),
							Smiley.this.getTheme().smileyBorder()
					);
					case SMILEY_COOL -> this.drawSmileyCool(
							g,
							Smiley.this.getP(),
							new MyCoordinates(0, 0),
							Smiley.this.getTheme().smileyBase(),
							Smiley.this.getTheme().smileyBorder()
					);
					case SMILEY_DEAD -> this.drawSmileyDead(
							g,
							Smiley.this.getP(),
							new MyCoordinates(0, 0),
							Smiley.this.getTheme().smileyBase(),
							Smiley.this.getTheme().smileyBorder()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
	}
	
	@Override
	public int getP() {
		return this.getWidth() / 23;
	}
	
	public void setMouseDown(boolean mouseDown) {
		this.mouseDown = mouseDown;
	}
	
	public void setGameWon(boolean gameWon) {
		this.gameWon = gameWon;
	}
	
	/* ---------- GETTERS/SETTERS ---------- */
	/* ---------- #draw() UTILITIES ---------- */
	
	@Override
	protected void drawLabel(@NotNull Graphics2D g) {}
	
	/* ---------- #draw() UTILITIES ---------- */
	/* ---------- @Override Drawable ---------- */
	
	@SuppressWarnings("PointlessBooleanExpression")
	@Override
	public void draw(@NotNull Graphics2D g) {
		if (this.getTheme() == null) {
			AppLogger.severe(String.format(
					"Theme is null, cannot draw %s.",
					this.getLogName()
			));
			return;
		}
		g.drawImage(this.getTexture(Textures.Key.BACKGROUND), this.getX(), this.getY(), null);
		
		if (this.isPressed)
			g.drawImage(this.getTexture(Textures.Key.REVEALED), this.getX(), this.getY(), null);
		else
			g.drawImage(this.getTexture(Textures.Key.CLOSED), this.getX(), this.getY(), null);
		
		if (this.mouseDown && this.gameWon == null)
			g.drawImage(this.getTexture(Textures.Key.SMILEY_OOH), this.getX() + 3 * this.getP(), this.getY() + 3 * this.getP(), null);
		else if (this.gameWon == null)
			g.drawImage(this.getTexture(Textures.Key.SMILEY_HAPPY), this.getX() + 3 * this.getP(), this.getY() + 3 * this.getP(), null);
		else if (Boolean.TRUE.equals(this.gameWon))
			g.drawImage(this.getTexture(Textures.Key.SMILEY_COOL), this.getX() + 3 * this.getP(), this.getY() + 3 * this.getP(), null);
		else
			g.drawImage(this.getTexture(Textures.Key.SMILEY_DEAD), this.getX() + 3 * this.getP(), this.getY() + 3 * this.getP(), null);
		
		if (this.isHovered)
			this.drawOverlay(g);
	}
	
	/* ---------- @Override Drawable ---------- */
	/* ---------- @Override Clickable ---------- */
	
	@Override
	public void onClick(@NotNull MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			this.parentScreen.getManager().runAfterDraw(() ->
				this.parentScreen.getManager().pushScreen(new DifficultyScreen(this.parentScreen.getManager()))
			);
		}
	}
	
	/* ---------- @Override Clickable ---------- */
}

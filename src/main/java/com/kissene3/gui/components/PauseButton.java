package com.kissene3.gui.components;

import com.kissene3.gui.screens.*;
import com.kissene3.gui.util.*;
import com.kissene3.io.AppLogger;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;

public abstract class PauseButton extends MinesweeperButton {
	/* ---------- ATTRIBUTES ---------- */
	
	protected Screen parentScreen;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public PauseButton(int x, int y, int width, int height, @NotNull Screen parentScreen) {
		this(new MyRectangle(x, y, width, height), parentScreen);
	}
	
	@SuppressWarnings("unused")
	public PauseButton(@NotNull MyCoordinates topLeft, @NotNull MyDimension dimensions, @NotNull Screen parentScreen) {
		this(new MyRectangle(topLeft, dimensions), parentScreen);
	}
	
	public PauseButton(@NotNull MyRectangle bounds, @NotNull Screen parentScreen) {
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
							new MyDimension(PauseButton.this.getWidth(), PauseButton.this.getHeight());
					case PAUSE ->
							new MyDimension(PauseButton.this.getWidth() - 6 * PauseButton.this.getP(), PauseButton.this.getHeight() - 6 * PauseButton.this.getP());
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
							PauseButton.this.getTheme().background()
					);
					case REVEALED -> this.drawRevealed(
							g,
							PauseButton.this.getP(),
							new MyCoordinates(0, 0),
							dimension,
							PauseButton.this.getTheme().border()
					);
					case CLOSED -> this.drawClosed(
							g,
							PauseButton.this.getP(),
							new MyCoordinates(0, 0),
							dimension,
							PauseButton.this.getTheme().backgroundLighterShade(),
							PauseButton.this.getTheme().backgroundDarkerShade()
					);
					case PAUSE -> this.drawPause(
							g,
							PauseButton.this.getP(),
							new MyCoordinates(0, 0),
							PauseButton.this.getTheme().pauseBase(),
							PauseButton.this.getTheme().pauseBorder()
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
	
	/* ---------- GETTERS/SETTERS ---------- */
	/* ---------- #draw() UTILITIES ---------- */
	
	@Override
	protected void drawLabel(@NotNull Graphics2D g) {}
	
	/* ---------- #draw() UTILITIES ---------- */
	/* ---------- @Override Drawable ---------- */
	
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
		
		g.drawImage(this.getTexture(Textures.Key.PAUSE), this.getX() + 3 * this.getP(), this.getY() + 3 * this.getP(), null);
		
		if (this.isHovered)
			this.drawOverlay(g);
	}
	
	/* ---------- @Override Drawable ---------- */
	/* ---------- @Override Clickable ---------- */
	
	@Override
	public void onClick(@NotNull MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1 || !(this.parentScreen instanceof GameScreen screen))
			return;
		this.parentScreen.getManager().runAfterDraw(() ->
			this.parentScreen.getManager().pushScreen(new PauseScreen(this.parentScreen.getManager(), screen.getGrid(), screen.getThirties()))
		);
	}
	
	/* ---------- @Override Clickable ---------- */
}

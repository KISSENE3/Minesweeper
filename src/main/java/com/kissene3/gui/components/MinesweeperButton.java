package com.kissene3.gui.components;

import com.kissene3.gui.utilities.MyUtilities.Coordinates;
import com.kissene3.gui.utilities.MyUtilities.Dimension;
import com.kissene3.gui.utilities.MyUtilities.Rectangle;
import com.kissene3.gui.Theme;
import com.kissene3.gui.io.AppLogger;
import com.kissene3.gui.utilities.TextureManager;
import com.kissene3.gui.utilities.Textures;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

@SuppressWarnings("unused")
public abstract class MinesweeperButton extends MyAbstractButton {
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	private final Textures textures;
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- ATTRIBUTES ---------- */
	
	private String label;
	private Font font;
	private boolean pressed = false;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public MinesweeperButton(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height), null);
	}
	
	public MinesweeperButton(int x, int y, int width, int height, @Nullable String label) {
		this(new Rectangle(x, y, width, height), label);
	}
	
	public MinesweeperButton(@NotNull Coordinates topLeft, @NotNull Dimension dimensions) {
		this(new Rectangle(topLeft, dimensions), null);
	}
	
	public MinesweeperButton(@NotNull Coordinates topLeft, @NotNull Dimension dimensions, @Nullable String label) {
		this(new Rectangle(topLeft, dimensions), label);
	}
	
	public MinesweeperButton(@NotNull Rectangle bounds) {
		this(bounds, null);
	}
	
	public MinesweeperButton(@NotNull Rectangle bounds, @Nullable String label) {
		super(bounds);
		this.label = label;
		this.font = new Font("Monospaced", Font.BOLD, (int)(this.getHeight() * 0.4));
		this.textures = new Textures() {
			@Override
			protected @NotNull Dimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, CLOSED, REVEALED ->
							new Dimension(MinesweeperButton.this.getWidth(), MinesweeperButton.this.getHeight());
					default -> throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull Dimension dimension) {
				switch (key) {
					case BACKGROUND -> this.drawBackground(
							g,
							new Coordinates(0, 0),
							dimension,
							MinesweeperButton.this.getTheme().background()
					);
					case REVEALED -> this.drawRevealed(
							g,
							MinesweeperButton.this.getP(),
							new Coordinates(0, 0),
							dimension,
							MinesweeperButton.this.getTheme().border()
					);
					case CLOSED -> this.drawClosed(
							g,
							MinesweeperButton.this.getP(),
							new Coordinates(0, 0),
							dimension,
							MinesweeperButton.this.getTheme().backgroundLighterShade(),
							MinesweeperButton.this.getTheme().backgroundDarkerShade()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
		TextureManager.addPack(this.textures);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	public void setLabel(@Nullable String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setFont(@NotNull Font font) {
		this.font = font;
	}
	
	public Font getFont() {
		return this.font;
	}
	
	public int getP() {
		return 4;
	}
	
	public abstract Theme getTheme();
	
	private @NotNull BufferedImage getTexture(@NotNull Textures.Key key) {
		return this.textures.getAsset(key);
	}
	
	protected @NotNull String getLogName() {
		Class<?> clazz = this.getClass();
		return (clazz.isAnonymousClass() ? clazz.getSuperclass() : clazz).getSimpleName();
	}
	
	@Override
	public void dispose() {
		this.textures.dispose();
		super.dispose();
	}
	
	/* ---------- GETTERS/SETTERS ---------- */
	/* ---------- #draw() UTILITIES ---------- */
	
	protected void drawOverlay(@NotNull Graphics2D g) {
		g.setColor(this.getTheme().overlay());
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	protected void drawLabel(@NotNull Graphics2D g) {
		g.setColor(this.getTheme().text());
		g.setFont(this.font);
		FontMetrics fm = g.getFontMetrics();
		int x = this.getX() + (this.getWidth() - fm.stringWidth(this.label)) / 2;
		int y = this.getY() + (this.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
		g.drawString(this.label, x, y);
	}
	
	/* ---------- #draw() UTILITIES ---------- */
	/* ---------- @Override Drawable ---------- */
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		if (this.getTheme() == null) {
			AppLogger.severe(String.format(
					"Theme is null, cannot draw %s: %s.",
					this.getLogName(),
					this.getLabel()
			));
			return;
		}
		g.drawImage(this.getTexture(Textures.Key.BACKGROUND), this.getX(), this.getY(), null);
		
		if (this.pressed)
			g.drawImage(this.getTexture(Textures.Key.REVEALED), this.getX(), this.getY(), null);
		else
			g.drawImage(this.getTexture(Textures.Key.CLOSED), this.getX(), this.getY(), null);
		
		g.setColor(this.getTheme().text());
		g.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		if (this.isHovered)
			this.drawOverlay(g);
		
		if (this.label != null)
			this.drawLabel(g);
	}
	
	/* ---------- @Override Drawable ---------- */
	/* ---------- @Override Clickable ---------- */
	
	@Override
	public void onPress(@NotNull MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1)
			return;
		this.pressed = true;
	}
	
	@Override
	public void onRelease(@NotNull MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1)
			return;
		this.pressed = false;
	}
	
	public abstract void onClick(@NotNull MouseEvent e);
	
	/* ---------- @Override Clickable ---------- */
	/* ---------- @Override Hoverable ---------- */
	
	@Override
	public void onEnter(@NotNull MouseEvent e) {
		this.setHovered(true);
	}
	
	@Override
	public void onExit(@NotNull MouseEvent e) {
		this.setHovered(false);
	}
	
	/* ---------- @Override Hoverable ---------- */
}

package com.kissene3.gui.util;

import com.kissene3.io.AppLogger;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public abstract class Textures {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	public enum Key {
		BACKGROUND, EXPLODED,
		CLOSED, REVEALED, MINE,
		FLAG, CROSS, ZERO,
		ONE, TWO, THREE,
		FOUR, FIVE, SIX,
		SEVEN, EIGHT, NINE,
		M, I, N, E, S, W, P, R,
		MINESWEEPER, TITLE,
		SMILEY_BASE, SMILEY_HAPPY,
		SMILEY_OOH, SMILEY_DEAD,
		SMILEY_COOL, PAUSE
	}
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	protected final Map<Key, BufferedImage> CACHE = new HashMap<>();
	protected final String name;
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public Textures() {
		this(null);
	}
	
	public Textures(@Nullable String name) {
		if (name == null)
			this.name = this.resolveName();
		else
			this.name = name;
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS ---------- */
	
	public BufferedImage getAsset(Key key) {
		return CACHE.computeIfAbsent(key, this::renderAsset);
	}
	
	/* ---------- GETTERS ---------- */
	/* ---------- UTILITIES ---------- */
	
	protected String resolveName() {
		Class<?> clazz = this.getClass();
		
		if (clazz.isAnonymousClass()) {
			Class<?> enclosing = clazz.getEnclosingClass();
			if (enclosing != null) {
				return enclosing.getSimpleName();
			}
		}
		
		String simpleName = clazz.getSimpleName();
		return simpleName.isEmpty() ? "UnknownTextures" : simpleName;
	}
	
	public void clear() {
		AppLogger.info(CACHE.size() + " graphics cache invalidated for " + this.name + ".");
		CACHE.values().forEach(BufferedImage::flush);
		CACHE.clear();
	}
	
	public void removeKey(@NotNull Key key) {
		BufferedImage img = CACHE.remove(key);
		if (img != null) {
			img.flush();
		}
	}
	
	@SuppressWarnings("unused")
	public void removeKeys(@NotNull Key @NotNull ... keys) {
		for (Key key : keys)
			this.removeKey(key);
	}
	
	public void dispose() {
		this.clear();
		TextureManager.unregister(this);
	}
	
	protected @NotNull BufferedImage renderAsset(Key key) {
		MyDimension d = this.getAssetDimension(key);
		
		GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage img = config.createCompatibleImage(d.width(), d.height(), Transparency.TRANSLUCENT);
		Graphics2D g = img.createGraphics();
		
		drawMapping(g, key, d);
		
		g.dispose();
		return img;
	}
	
	/* ---------- UTILITIES ---------- */
	/* ---------- ABSTRACT METHODS ---------- */
	
	protected abstract @NotNull MyDimension getAssetDimension(@NotNull Key key);
	protected abstract void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull MyDimension dimension);
	
	/* ---------- ABSTRACT METHODS ---------- */
	/* ---------- TEXTURES ---------- */
	
	protected void drawBackground(@NotNull Graphics2D g, @NotNull MyCoordinates topLeft, @NotNull MyDimension dimension, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x(), topLeft.y(), dimension.width(), dimension.height());
		g.setColor(oldColor);
	}
	
	protected void drawClosed(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @NotNull MyDimension dimension, @Nullable Color lighterColor, @Nullable Color darkerColor) {
		int p2 = 2 * p;
		int reducedW = dimension.width() - p;
		int reducedW2 = dimension.width() - p2;
		int reducedH = dimension.height() - p;
		int reducedH2 = dimension.height() - p2;
		Color oldColor = g.getColor();
		
		if (lighterColor != null)
			g.setColor(lighterColor);
		g.fillRect(topLeft.x(), topLeft.y(), reducedW, p);								// top top-line
		g.fillRect(topLeft.x(), topLeft.y(), reducedW2, p2);							// bottom top-line
		g.fillRect(topLeft.x(), topLeft.y(), p, reducedH);								// left left-line
		g.fillRect(topLeft.x(), topLeft.y(), p2, reducedH2);							// right left-line
		
		if (darkerColor != null)
			g.setColor(darkerColor);
		else
			g.setColor(oldColor);
		g.fillRect(topLeft.x() + reducedW, topLeft.y() + p, p, reducedH);			// right right-line
		g.fillRect(topLeft.x() + reducedW2, topLeft.y() + p2, p, reducedH2);		// left right-line
		g.fillRect(topLeft.x() + p, topLeft.y() + reducedH, reducedW, p);			// bottom bottom-line
		g.fillRect(topLeft.x() + p2, topLeft.y() + reducedH2, reducedW2, p);		// top bottom-line
		
		g.setColor(oldColor);
	}
	
	protected void drawRevealed(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @NotNull MyDimension dimension, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x(), topLeft.y(), dimension.width(), p);		// top line
		g.fillRect(topLeft.x(), topLeft.y(), p, dimension.height());	// left line
		g.setColor(oldColor);
	}
	
	protected void drawMine(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color mineColor, @Nullable Color reflexColor) {
		Color oldColor = g.getColor();
		
		if (mineColor != null)
			g.setColor(mineColor);
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 4 * p, 5 * p, 9 * p);	// vertical rect
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 6 * p, 9 * p, 5 * p);	// horizontal rect
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 5 * p, 7 * p, 7 * p);	// centered square
		g.fillRect(topLeft.x() + 8 * p, topLeft.y() + 2 * p, p, 13 * p);			// vertical middle line
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 8 * p, 13 * p, p);			// horizontal middle line
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 4 * p, p, p);						// top-left dot
		g.fillRect(topLeft.x() + 12 * p, topLeft.y() + 4 * p, p, p);						// top-right dot
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 12 * p, p, p);						// bottom-left dot
		g.fillRect(topLeft.x() + 12 * p, topLeft.y() + 12 * p, p, p);						// bottom-right dot
		
		
		if (reflexColor != null)
			g.setColor(reflexColor);
		else
			g.setColor(oldColor);
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 6 * p, 2 * p, 2 * p);	// light dot
		
		g.setColor(oldColor);
	}
	
	protected void drawFlag(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color flagColor, @Nullable Color baseColor) {
		Color oldColor = g.getColor();
		
		if (flagColor != null)
			g.setColor(flagColor);
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 3 * p, 2 * p, 5 * p);	// right column
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 4 * p, 2 * p, 3 * p);	// middle column
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 5 * p, p, p);						// left dot
		
		if (baseColor != null)
		g.setColor(baseColor);
		else
			g.setColor(oldColor);
		g.fillRect(topLeft.x() + 8 * p, topLeft.y() + 8 * p, p, 2 * p);				// vertical pole
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 10 * p, 4 * p, p);			// top row
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 11 * p, 8 * p, 2 * p);	// bottom row
		
		g.setColor(oldColor);
	}
	
	protected void drawCross(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @NotNull MyDimension dimension, @Nullable Color color) {
		int x2 = topLeft.x() + dimension.width() - p;
		int y2 = topLeft.y() + dimension.height() - p;
		Stroke oldStroke = g.getStroke();
		Color oldColor = g.getColor();
		
		g.setStroke(new BasicStroke(p));
		if (color != null)
			g.setColor(color);
		g.drawLine(topLeft.x() + p, topLeft.y() + p, x2, y2);
		g.drawLine(x2, topLeft.y() + p, topLeft.x() + p, y2);
		
		g.setStroke(oldStroke);
		g.setColor(oldColor);
	}
	
	protected void drawNumberOne(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 8 * p, topLeft.y() + 3 * p, 2 * p, 8 * p); 	// Right column
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 4 * p, p, 7 * p);				// Left column
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 5 * p, p, p);						// Top appendix
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 6 * p, 2 * p, p);				// Bottom appendix
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 11 * p, 7 * p, 2 * p);	// Bottom row
		g.setColor(oldColor);
	}
	
	protected void drawNumberTwo(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 4 * p, 3 * p, 2 * p);	// top-left appendix
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 4 * p, 3 * p, 3 * p);	// right square
		g.fillRect(topLeft.x() + 8 * p, topLeft.y() + 7 * p, 4 * p, p);				// first middle row
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 8 * p, 5 * p, p);				// second middle row
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 9 * p, 5 * p, p);				// third middle row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 10 * p, 4 * p, 3 * p);	// bottom-left corner
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 11 * p, 6 * p, 2 * p);	// bottom row
		g.setColor(oldColor);
	}
	
	protected void drawNumberThree(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 3 * p, 9 * p, 2 * p);	// top row
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 7 * p, 6 * p, 2 * p);	// middle row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 11 * p, 9 * p, 2 * p);	// bottom row
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 4 * p, 3 * p, 3 * p);	// top column
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 9 * p, 3 * p, 3 * p);	// bottom column
		g.setColor(oldColor);
	}
	
	protected void drawNumberFour(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 3 * p, 3 * p, 2 * p);	// top diagonal
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 5 * p, 3 * p, 2 * p);	// bottom diagonal
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 7 * p, 10 * p, 2 * p);	// horizontal line
		g.fillRect(topLeft.x() + 9 * p, topLeft.y() + 3 * p, 3 * p, 10 * p);	// vertical line
		g.setColor(oldColor);
	}
	
	protected void drawNumberFive(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 3 * p, 10 * p, 2 * p);	// top row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 5 * p, 3 * p, 2 * p);	// left column
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 7 * p, 9 * p, 2 * p);	// middle row
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 8 * p, 3 * p, 4 * p);	// right column
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 11 * p, 9 * p, 2 * p);	// bottom row
		g.setColor(oldColor);
	}
	
	protected void drawNumberSix(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 4 * p, 3 * p, 8 * p);	// left column
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 7 * p, 6 * p, 2 * p);	// middle row
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 11 * p, 8 * p, 2 * p);	// bottom row
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 8 * p, 3 * p, 4 * p);	// right column
		g.setColor(oldColor);
	}
	
	protected void drawNumberSeven(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 3 * p, 10 * p, 2 * p);	// row
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 5 * p, 3 * p, 2 * p);	// first diagonal
		g.fillRect(topLeft.x() + 9 * p, topLeft.y() + 7 * p, 3 * p, 2 * p);	// second diagonal
		g.fillRect(topLeft.x() + 8 * p, topLeft.y() + 9 * p, 3 * p, 2 * p);	// third diagonal
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 11 * p, 3 * p, 2 * p);	// fourth diagonal
		g.setColor(oldColor);
	}
	
	protected void drawNumberEight(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 7 * p, 8 * p, 2 * p);	// middle row
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 11 * p, 8 * p, 2 * p);	// bottom row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 4 * p, 3 * p, 3 * p);	// top-left column
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 4 * p, 3 * p, 3 * p);	// top-right column
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 9 * p, 3 * p, 3 * p);	// bottom-left column
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 9 * p, 3 * p, 3 * p);	// bottom-right column
		g.setColor(oldColor);
	}
	
	protected void drawNumberNine(@NotNull Graphics2D g, @SuppressWarnings("SameParameterValue") int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 3 * p, 8 * p, 2 * p);    // top row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 4 * p, 3 * p, 4 * p);    // left column
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 7 * p, 6 * p, 2 * p);    // middle row
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 11 * p, 8 * p, 2 * p);    // bottom row
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 4 * p, 3 * p, 8 * p);    // right column
		g.setColor(oldColor);
	}
	
	protected void drawNumberZero(@NotNull Graphics2D g, @SuppressWarnings("SameParameterValue") int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 11 * p, 8 * p, 2 * p);	// bottom row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 4 * p, 3 * p, 8 * p);	// left column
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 4 * p, 3 * p, 8 * p);	// right column
		g.setColor(oldColor);
	}
	
	protected void drawM(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x(), topLeft.y() + 3 * p, 2 * p, 10 * p);				// left column
		g.fillRect(topLeft.x() + 8 * p, topLeft.y() + 3 * p, 2 * p, 10 * p);		// right column
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 3 * p, p, 3 * p);					// leftest segment
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 3 * p, p, 3 * p);					// rightest segment
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 4 * p, p, 3 * p);					// left segment
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 4 * p, p, 3 * p);					// right segment
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 5 * p, 2 * p, 3 * p);		// middle rectangle
		g.setColor(oldColor);
	}
	
	protected void drawI(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x(), topLeft.y() + 3 * p, 3 * p, 10 * p);		// I
		g.setColor(oldColor);
	}
	
	protected void drawN(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x(), topLeft.y() + 4 * p, 2 * p, 9 * p);					// left column
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 4 * p, 2 * p, 8 * p);		// middle column
		g.fillRect(topLeft.x() + 8 * p, topLeft.y() + 3 * p, 2 * p, 9 * p);		// right column
		g.fillRect(topLeft.x() + p, topLeft.y() + 3 * p, 4 * p, 3 * p);			// top row
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 10 * p, 4 * p, 3 * p);		// bottom row
		g.setColor(oldColor);
	}
	
	protected void drawE(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x(), topLeft.y() + 3 * p, 2 * p, 10 * p);				// vertical line
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 3 * p, 7 * p, 2 * p);		// top row
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 7 * p, 4 * p, 2 * p);		// middle row
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 11 * p, 7 * p, 2 * p);		// bottom row
		g.setColor(oldColor);
	}
	
	protected void drawS(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x() + p, topLeft.y() + 3 * p, 9 * p, 2 * p);			// top row
		g.fillRect(topLeft.x() + p, topLeft.y() + 7 * p, 8 * p, 2 * p);			// middle row
		g.fillRect(topLeft.x(), topLeft.y() + 11 * p, 9 * p, 2 * p);				// bottom row
		g.fillRect(topLeft.x(), topLeft.y() + 4 * p, 3 * p, 4 * p);					// left column
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 8 * p, 3 * p, 4 * p);		// right column
		g.setColor(oldColor);
	}
	
	protected void drawW(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x(), topLeft.y() + 3 * p, 2 * p, 9 * p);					// left row
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 6 * p, 2 * p, 3 * p);		// middle row
		g.fillRect(topLeft.x() + 8 * p, topLeft.y() + 3 * p, 2 * p, 9 * p);		// right row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 9 * p, 4 * p, 2 * p);		// top column
		g.fillRect(topLeft.x(), topLeft.y() + 10 * p, 4 * p, 2 * p);				// left middle column
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 10 * p, 4 * p, 2 * p);		// right middle column
		g.fillRect(topLeft.x() + p, topLeft.y() + 12 * p, 2 * p, p);					// left bottom column
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 12 * p, 2 * p, p);				// right bottom column
		g.setColor(oldColor);
	}
	
	protected void drawP(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		g.fillRect(topLeft.x(), topLeft.y() + 3 * p, 3 * p, 10 * p);				// left column
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 4 * p, 3 * p, 4 * p);		// right column
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 3 * p, 6 * p, 2 * p);		// top row
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 7 * p, 6 * p, 2 * p);		// bottom row
		g.setColor(oldColor);
	}
	
	protected void drawR(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		if (color != null)
			g.setColor(color);
		this.drawP(g, p, topLeft, color);															// P base
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 9 * p, 4 * p, p);					// leg's top row
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 10 * p, 4 * p, p);				// leg's middle-top row
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 11 * p, 4 * p, p);				// leg's middle-bottom row
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 12 * p, 3 * p, p);				// leg's bottom row
		g.setColor(oldColor);
	}
	
	protected void drawMinesweeper(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color color) {
		Color oldColor = g.getColor();
		int x = topLeft.x();
		if (color != null)
			g.setColor(color);
		this.drawM(g, p, topLeft.withX(x), color);
		x += 12 * p;
		this.drawI(g, p, topLeft.withX(x), color);
		x += 5 * p;
		this.drawN(g, p, topLeft.withX(x), color);
		x += 12 * p;
		this.drawE(g, p, topLeft.withX(x), color);
		x += 11 * p;
		this.drawS(g, p, topLeft.withX(x), color);
		x += 12 * p;
		this.drawW(g, p, topLeft.withX(x), color);
		x += 12 * p;
		this.drawE(g, p, topLeft.withX(x), color);
		x += 11 * p;
		this.drawE(g, p, topLeft.withX(x), color);
		x += 11 * p;
		this.drawP(g, p, topLeft.withX(x), color);
		x += 12 * p;
		this.drawE(g, p, topLeft.withX(x), color);
		x += 11 * p;
		this.drawR(g, p, topLeft.withX(x), color);
		g.setColor(oldColor);
	}
	
	protected void drawTitle(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color textColor, @Nullable Color borderColor) {
		Color oldColor = g.getColor();
		int offset = Math.max(2, p / 4 * 2);
		
		if (borderColor != null)
			g.setColor(borderColor);
		this.drawMinesweeper(g, p, topLeft, borderColor);
		this.drawMinesweeper(g, p, topLeft.withX(topLeft.x() + offset), borderColor);
		this.drawMinesweeper(g, p, topLeft.withY(topLeft.y() + offset), borderColor);
		this.drawMinesweeper(g, p, new MyCoordinates(topLeft.x() + offset, topLeft.y() + offset), borderColor);
		
		if (textColor != null)
			g.setColor(textColor);
		else
			g.setColor(oldColor);
		this.drawMinesweeper(g, p, new MyCoordinates(topLeft.x() + offset / 2, topLeft.y() + offset / 2), textColor);
		
		g.setColor(oldColor);
	}
	
	// 17 * 17
	protected void drawSmileyBase(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color backgroundColor, @Nullable Color borderColor) {
		Color oldColor = g.getColor();
		
		if (borderColor != null)
			g.setColor(borderColor);
		g.fillRect(topLeft.x() + 6 * p, topLeft.y(), 5 * p, 17 * p);
		g.fillRect(topLeft.x(), topLeft.y() + 6 * p, 17 * p, 5 * p);
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + p, 9 * p, 15 * p);
		g.fillRect(topLeft.x() + p, topLeft.y() + 4 * p, 15 * p, 9 * p);
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 2 * p, 11 * p, 13 * p);
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 3 * p, 13 * p, 11 * p);
		
		if (backgroundColor != null)
			g.setColor(backgroundColor);
		else
			g.setColor(oldColor);
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + p, 5 * p, 15 * p);
		g.fillRect(topLeft.x() + p, topLeft.y() + 6 * p, 15 * p, 5 * p);
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 2 * p, 9 * p, 13 * p);
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 4 * p, 13 * p, 9 * p);
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 3 * p, 11 * p, 11 * p);
		
		g.setColor(oldColor);
	}
	
	// 17 * 17
	protected void drawSmileyHappy(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color backgroundColor, @Nullable Color borderColor) {
		Color oldColor = g.getColor();
		
		if  (borderColor != null)
			g.setColor(borderColor);
		this.drawSmileyBase(g, p, topLeft, backgroundColor, borderColor);
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 5 * p, 2 * p, 2 * p);
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 5 * p, 2 * p, 2 * p);
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 10 * p, p, p);
		g.fillRect(topLeft.x() + 12 * p, topLeft.y() + 10 * p, p, p);
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 11 * p, p, p);
		g.fillRect(topLeft.x() + 11 * p, topLeft.y() + 11 * p, p, p);
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 12 * p, 5 * p, p);
		
		g.setColor(oldColor);
	}
	
	// 17 * 17
	protected void drawSmileyOoh(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color backgroundColor, @Nullable Color borderColor) {
		Color oldColor = g.getColor();
		
		this.drawSmileyBase(g, p, topLeft, backgroundColor, borderColor);
		
		if  (borderColor != null)
			g.setColor(borderColor);
		
		g.setColor(new Color((g.getColor().getRGB() & 0x00FFFFFF) | 0x80000000, true));
		
		g.fillRect(topLeft.x() + 4 * p,  topLeft.y() + 4 * p, 3 * p, 3 * p);
		g.fillRect(topLeft.x() + 10 * p,  topLeft.y() + 4 * p, 3 * p, 3 * p);
		g.fillRect(topLeft.x() + 6 * p,  topLeft.y() + 10 * p, p, 3 * p);
		g.fillRect(topLeft.x() + 10 * p,  topLeft.y() + 10 * p, p, 3 * p);
		
		if  (borderColor != null)
			g.setColor(borderColor);
		else
			g.setColor(oldColor);
		
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 5 * p, 3 * p, p);
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 4 * p, p, 3 * p);
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 5 * p, 3 * p, p);
		g.fillRect(topLeft.x() + 11 * p, topLeft.y() + 4 * p, p, 3 * p);
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 9 * p, 3 * p, p);
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 10 * p, p, p);
		g.fillRect(topLeft.x() + 9 * p, topLeft.y() + 10 * p, p, p);
		g.fillRect(topLeft.x() + 6 * p, topLeft.y() + 11 * p, p, p);
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 11 * p, p, p);
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 12 * p, p, p);
		g.fillRect(topLeft.x() + 9 * p, topLeft.y() + 12 * p, p, p);
		g.fillRect(topLeft.x() + 7 * p, topLeft.y() + 13 * p, 3 * p, p);
		
		g.setColor(oldColor);
	}
	
	// 17 * 17
	protected void drawSmileyDead(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color backgroundColor, @Nullable Color borderColor) {
		Color oldColor = g.getColor();
		
		this.drawSmileyBase(g, p, topLeft, backgroundColor, borderColor);
		
		if  (borderColor != null)
			g.setColor(borderColor);
		
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 4 * p, p, p);
		g.fillRect(topLeft.x() + 6  * p, topLeft.y() + 4 * p, p, p);
		g.fillRect(topLeft.x() + 10  * p, topLeft.y() + 4 * p, p, p);
		g.fillRect(topLeft.x() + 12  * p, topLeft.y() + 4 * p, p, p);
		g.fillRect(topLeft.x() + 5  * p, topLeft.y() + 5 * p, p, p);
		g.fillRect(topLeft.x() + 11  * p, topLeft.y() + 5 * p, p, p);
		g.fillRect(topLeft.x() + 4 * p, topLeft.y() + 6 * p, p, p);
		g.fillRect(topLeft.x() + 6  * p, topLeft.y() + 6 * p, p, p);
		g.fillRect(topLeft.x() + 10  * p, topLeft.y() + 6 * p, p, p);
		g.fillRect(topLeft.x() + 12  * p, topLeft.y() + 6 * p, p, p);
		g.fillRect(topLeft.x() + 6  * p, topLeft.y() + 10 * p, 5 * p, p);
		g.fillRect(topLeft.x() + 5  * p, topLeft.y() + 11 * p, p, p);
		g.fillRect(topLeft.x() + 11  * p, topLeft.y() + 11 * p, p, p);
		g.fillRect(topLeft.x() + 4  * p, topLeft.y() + 12 * p, p, p);
		g.fillRect(topLeft.x() + 12  * p, topLeft.y() + 12 * p, p, p);
		
		g.setColor(oldColor);
	}
	
	// 17 * 17
	protected void drawSmileyCool(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color backgroundColor, @Nullable Color borderColor) {
		Color oldColor = g.getColor();
		
		this.drawSmileyBase(g, p, topLeft, backgroundColor, borderColor);
		
		if  (borderColor != null)
			g.setColor(borderColor);
		
		g.fillRect(topLeft.x() + 4  * p, topLeft.y() + 5 * p, 4 * p, 3 * p);
		g.fillRect(topLeft.x() + 9  * p, topLeft.y() + 5 * p, 4 * p, 3 * p);
		g.fillRect(topLeft.x() + 8  * p, topLeft.y() + 5 * p, p, p);
		g.fillRect(topLeft.x() + 3  * p, topLeft.y() + 6 * p, p, p);
		g.fillRect(topLeft.x() + 13  * p, topLeft.y() + 6 * p, p, p);
		g.fillRect(topLeft.x() + 2  * p, topLeft.y() + 7 * p, p, p);
		g.fillRect(topLeft.x() + 14  * p, topLeft.y() + 7 * p, p, p);
		g.fillRect(topLeft.x() + p, topLeft.y() + 8 * p, p, p);
		g.fillRect(topLeft.x() + 5 * p, topLeft.y() + 8 * p, 2 * p, p);
		g.fillRect(topLeft.x() + 10 * p, topLeft.y() + 8 * p, 2 * p, p);
		g.fillRect(topLeft.x() + 15  * p, topLeft.y() + 8 * p, p, p);
		g.fillRect(topLeft.x() + 5  * p, topLeft.y() + 11 * p, p, p);
		g.fillRect(topLeft.x() + 11  * p, topLeft.y() + 11 * p, p, p);
		g.fillRect(topLeft.x() + 6  * p, topLeft.y() + 12 * p, 5 * p, p);
		
		g.setColor(new Color((g.getColor().getRGB() & 0x00FFFFFF) | 0x80000000, true));
		
		g.fillRect(topLeft.x() + 4  * p, topLeft.y() + 8 * p, p, p);
		g.fillRect(topLeft.x() + 12  * p, topLeft.y() + 8 * p, p, p);
		
		g.setColor(oldColor);
	}
	
	// 17 * 17
	protected void drawPause(@NotNull Graphics2D g, int p, @NotNull MyCoordinates topLeft, @Nullable Color backgroundColor, @Nullable Color borderColor) {
		Color oldColor = g.getColor();
		
		if (borderColor != null)
			g.setColor(borderColor);
			
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 3 * p, 13 * p, 3 * p);		// Top bar border
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 7 * p, 13 * p, 3 * p);		// Middle bar border
		g.fillRect(topLeft.x() + 2 * p, topLeft.y() + 11 * p, 13 * p, 3 * p);		// Bottom bar border
		
		if (backgroundColor != null)
			g.setColor(backgroundColor);
		
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 4 * p, 11 * p, p);		// Top bar core
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 8 * p, 11 * p, p);		// Middle bar core
		g.fillRect(topLeft.x() + 3 * p, topLeft.y() + 12 * p, 11 * p, p);		// Bottom bar core
		
		g.setColor(oldColor);
	}
	
	/* ---------- TEXTURES ---------- */
}

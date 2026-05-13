package com.kissene3.gui.components;

import com.kissene3.gui.utilities.MyUtilities.Coordinates;
import com.kissene3.gui.utilities.MyUtilities.Dimension;
import com.kissene3.gui.utilities.MyUtilities.Rectangle;
import com.kissene3.gui.Theme;
import com.kissene3.gui.io.AppLogger;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.MouseEvent;

@SuppressWarnings("unused")
public abstract class MinesweeperButton extends MyAbstractButton {
	
	/* ---------- ATTRIBUTES ---------- */
	
	private String label;
	private Font font;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- GUI ATTRIBUTES ---------- */
	
	private boolean pressed = false;
	private boolean hovered = false;
	
	/* ---------- GUI ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public MinesweeperButton(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height), null);
	}
	
	public MinesweeperButton(int x, int y, int width, int height, @Nullable String label) {
		this(new Rectangle(x, y, width, height), label);
	}
	
	public MinesweeperButton(@NotNull Coordinates topLeft, @NotNull Dimension dimensions) {
		this(new Rectangle(topLeft, dimensions));
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
	
	public abstract void onClick(@NotNull MouseEvent e);
	
	public abstract Theme getTheme();
	/* ---------- GETTERS/SETTERS ---------- */
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		if (this.getTheme() == null) {
			AppLogger.severe(String.format("Theme is null, cannot draw %s.", this.getClass().getSimpleName()));
			return;
		}
		
		int p = this.getP();
		
		this.drawBackground(g);
		
		if (this.pressed)
			this.drawBorder(g, p);
		else
			this.drawShades(g, p);
		
		g.setColor(this.getTheme().text());
		g.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		if (this.hovered)
			this.drawOverlay(g);
		
		if (this.label != null)
			this.drawLabel(g);
	}
	
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
	
	protected void drawBackground(@NotNull Graphics2D g) {
		g.setColor(this.getTheme().background());
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	protected void drawShades(@NotNull Graphics2D g, int p) {
		int p2 = 2 * p;
		int reducedW = this.getWidth() - p;
		int reducedW2 = this.getWidth() - p2;
		int reducedH = this.getHeight() - p;
		int reducedH2 = this.getHeight() - p2;
		
		g.setColor(this.getTheme().backgroundLighterShade());
		g.fillRect(this.getX(), this.getY(), reducedW, p);								// top top-line
		g.fillRect(this.getX(), this.getY(), reducedW2, p2);							// bottom top-line
		g.fillRect(this.getX(), this.getY(), p, reducedH);								// left left-line
		g.fillRect(this.getX(), this.getY(), p2, reducedH2);							// right left-line
		
		g.setColor(this.getTheme().backgroundDarkerShade());
		g.fillRect(this.getX() + reducedW, this.getY() + p, p, reducedH);		// right right-line
		g.fillRect(this.getX() + reducedW2, this.getY() + p2, p, reducedH2);	// left right-line
		g.fillRect(this.getX() + p, this.getY() + reducedH, reducedW, p);		// bottom bottom-line
		g.fillRect(this.getX() + p2, this.getY() + reducedH2, reducedW2, p);	// top bottom-line
	}
	
	protected void drawBorder(@NotNull Graphics2D g, int p) {
		g.setColor(this.getTheme().border());
		g.fillRect(this.getX(), this.getY(), this.getWidth(), p);						// top line
		g.fillRect(this.getX(), this.getY(), p, this.getHeight());						// left line
		g.fillRect(this.getX(), this.getBottomRight().y() - p, this.getWidth(), p);	// bottom line
		g.fillRect(this.getBottomRight().x() - p, this.getY(), p, this.getHeight());	// right line
	}
	
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
	
	@Override
	public void onEnter(@NotNull MouseEvent e) {
		this.hovered = true;
	}
	
	@Override
	public void onExit(@NotNull MouseEvent e) {
		this.hovered = false;
	}
}

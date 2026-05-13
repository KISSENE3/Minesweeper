package com.kissene3.gui.components;

import com.kissene3.gui.utilities.MyUtilities.*;
import com.kissene3.gui.utilities.MyUtilities.Dimension;
import com.kissene3.gui.utilities.MyUtilities.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/** A button personalizable in colors and in the label */
@SuppressWarnings("unused")
public class MyButton extends MyAbstractButton {
	
	/* ---------- ATTRIBUTES ---------- */
	
	/** The text of the button. */
	private String label;
	
	/** The font used for rendering the label. */
	private Font font;
	
	/** Color scheme for various component states. */
	private Color borderColor = Color.BLACK, backgroundColor = Color.LIGHT_GRAY, foregroundColor = Color.BLACK;
	private Color disabledBorderColor = Color.DARK_GRAY, disabledBackgroundColor = Color.LIGHT_GRAY, disabledForegroundColor = Color.DARK_GRAY;
	private Color hoverColor = new Color(255, 255, 255, 32);
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public MyButton(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height), null);
	}
	
	public MyButton(int x, int y, int width, int height, @Nullable String label) {
		this(new Rectangle(x, y, width, height), label);
	}
	
	public MyButton(@NotNull Coordinates topLeft, @NotNull Dimension dimensions) {
		this(new Rectangle(topLeft, dimensions), null);
	}
	
	public MyButton(@NotNull Coordinates topLeft, @NotNull Dimension dimensions, @Nullable String label) {
		this(new Rectangle(topLeft, dimensions), label);
	}
	
	public MyButton(@NotNull Rectangle bounds) {
		this(bounds, null);
	}
	
	public MyButton(@NotNull Rectangle bounds, @Nullable String label) {
		super(bounds);
		this.setLabel(label);
		this.font = new Font("SansSerif", Font.BOLD, (int)(this.getHeight() * 0.4));
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- TEXT GETTERS/SETTERS ---------- */
	
	public void setLabel(@Nullable String label) {
		this.label = label;
	}
	public @NotNull String getLabel() {
		return this.label == null ? "" : this.label;
	}
	
	public void setFont(@NotNull Font font) {
		this.font = font;
	}
	public @NotNull Font getFont() {
		return this.font;
	}
	
	/* ---------- TEXT GETTERS/SETTERS ---------- */
	/* ---------- COLORS GETTERS/SETTERS ---------- */
	
	public void setBorderColor(@NotNull Color color) {
		this.borderColor = color;
	}
	public @NotNull Color getBorderColor() {
		return this.borderColor;
	}
	
	public void setBackgroundColor(@NotNull Color color) {
		this.backgroundColor = color;
	}
	public @NotNull Color getBackgroundColor() {
		return this.backgroundColor;
	}
	
	public void setForegroundColor(@NotNull Color color) {
		this.foregroundColor = color;
	}
	public @NotNull Color getForegroundColor() {
		return this.foregroundColor;
	}
	
	public void setDisabledBorderColor(@NotNull Color color) {
		this.disabledBorderColor = color;
	}
	public @NotNull Color getDisabledBorderColor() {
		return this.disabledBorderColor;
	}
	
	public void setDisabledBackgroundColor(@NotNull Color color) {
		this.disabledBackgroundColor = color;
	}
	public @NotNull Color getDisabledBackgroundColor() {
		return this.disabledBackgroundColor;
	}
	
	public void setDisabledForegroundColor(@NotNull Color color) {
		this.disabledForegroundColor = color;
	}
	public @NotNull Color getDisabledForegroundColor() {
		return this.disabledForegroundColor;
	}
	
	public void setHoverColor(@NotNull Color color) {
		this.hoverColor = color;
	}
	public @NotNull Color getHoverColor() {
		return this.hoverColor;
	}
	
	/* ---------- COLORS GETTERS/SETTERS ---------- */
	/* ---------- @Override MyComponent ---------- */
	
	@Override
	public void onBoundsChanged() {
		this.font = new Font("SansSerif", Font.BOLD, (int)(this.getHeight() * 0.4));
	}
	
	/* ---------- @Override MyComponent ---------- */
	/* ---------- @Override Drawable ---------- */
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		if (!this.isVisible) return;
		
		Color background = this.isActive ? this.backgroundColor : this.disabledBackgroundColor;
		Color border = this.isActive ? this.borderColor : this.disabledBorderColor;
		
		g.setColor(background);
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		g.setColor(border);
		g.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		this.drawLabel(g);
		this.drawHover(g);
	}
	
	/* ---------- @Override Drawable ---------- */
	/* ---------- #Draw(Graphics) UTILITIES ---------- */
	
	protected void drawLabel(@NotNull Graphics2D g) {
		if (this.label == null || this.label.isEmpty())
			return;
		
		Color foreground = this.isActive ? this.foregroundColor : this.disabledForegroundColor;
		
		g.setFont(this.font);
		g.setColor(foreground);
		FontMetrics fm = g.getFontMetrics();
		
		int textWidth = fm.stringWidth(this.label);
		int textX = this.getX() + (this.getWidth() - textWidth) / 2;
		int textY = this.getY() + ((this.getHeight() - fm.getHeight()) / 2) + fm.getAscent();
		
		g.drawString(this.label, textX, textY);
	}
	
	protected void drawHover(@NotNull Graphics2D g) {
		if (!this.isHovered || !this.isActive)
			return;
		
		g.setColor(this.hoverColor);
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	/* ---------- #Draw(Graphics) UTILITIES ---------- */
}

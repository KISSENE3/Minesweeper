package com.kissene3.gui.components;

import com.kissene3.gui.MyUtilities.Coordinates;
import com.kissene3.gui.MyUtilities.Dimension;
import com.kissene3.gui.MyUtilities.Rectangle;
import com.kissene3.gui.interfaces.Clickable;
import com.kissene3.gui.interfaces.Drawable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.MouseEvent;

@SuppressWarnings("unused")
public class MyLabel extends MyComponent implements Drawable, Clickable {
	private String text;
	private Font font;
	private boolean isVisible = true;
	
	public MyLabel(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height), null);
	}
	
	public MyLabel(int x, int y, int width, int height, @Nullable String text) {
		this(new Rectangle(x, y, width, height), text);
	}
	
	public MyLabel(@NotNull Coordinates topLeft, @NotNull Dimension dimensions) {
		this(new Rectangle(topLeft, dimensions), null);
	}
	
	public  MyLabel(@NotNull Coordinates topLeft, @NotNull Dimension dimension, @Nullable String text) {
		this(new Rectangle(topLeft, dimension), text);
	}
	
	public MyLabel(@NotNull Rectangle bounds) {
		this(bounds, null);
	}
	
	public MyLabel(@NotNull Rectangle bounds, @Nullable String text) {
		super(bounds);
		this.text = text;
		this.font = new Font("SansSerif", Font.PLAIN, 12);
	}
	
	public @Nullable String getText() {
		return this.text;
	}
	
	public void setText(@Nullable String text) {
		this.text = text;
	}
	
	public @NotNull Font getFont() {
		return this.font;
	}
	
	public void setFont(@NotNull Font font) {
		this.font = font;
	}
	
	public @NotNull Color getColorWhenActive() {
		return Color.BLACK;
	}
	
	public @NotNull Color getColorWhenInactive() {
		return Color.GRAY;
	}
	
	@Override
	public boolean isVisible() {
		return this.isVisible;
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.isVisible = visible;
	}
	
	@Override
	public void onClick(@NotNull MouseEvent e) {}
	
	@Override
	public void onPress(@NotNull MouseEvent e) {}
	
	@Override
	public void onRelease(@NotNull MouseEvent e) {}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		if (!this.isVisible || this.text == null || this.text.isEmpty())
			return;
		
		g.setFont(this.font);
		g.setColor(this.isActive ? this.getColorWhenActive(): this.getColorWhenInactive());
		FontMetrics fm = g.getFontMetrics();
		
		int textWidth = fm.stringWidth(this.text);
		int textX = this.getX() + (this.getWidth() - textWidth) / 2;
		int textY = this.getY() + ((this.getHeight() - fm.getHeight()) / 2) + fm.getAscent();
		
		g.drawString(this.text, textX, textY);
	}
}

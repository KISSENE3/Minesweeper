package com.kissene3.gui.components;

import com.kissene3.gui.util.*;
import com.kissene3.gui.interfaces.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("unused")
public class MyLabel extends MyComponent implements Drawable, Clickable {
	private String text;
	private Font font;
	private boolean isVisible = true;
	
	public MyLabel(int x, int y, int width, int height) {
		this(new MyRectangle(x, y, width, height), null);
	}
	
	public MyLabel(int x, int y, int width, int height, @Nullable String text) {
		this(new MyRectangle(x, y, width, height), text);
	}
	
	public MyLabel(@NotNull MyCoordinates topLeft, @NotNull MyDimension dimensions) {
		this(new MyRectangle(topLeft, dimensions), null);
	}
	
	public  MyLabel(@NotNull MyCoordinates topLeft, @NotNull MyDimension dimension, @Nullable String text) {
		this(new MyRectangle(topLeft, dimension), text);
	}
	
	public MyLabel(@NotNull MyRectangle bounds) {
		this(bounds, null);
	}
	
	public MyLabel(@NotNull MyRectangle bounds, @Nullable String text) {
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
		g.setColor(this.isActive ? this.getColorWhenActive() : this.getColorWhenInactive());
		FontMetrics fm = g.getFontMetrics();
		
		String[] words = this.text.split("\\s+");
		java.util.List<String> lines = new java.util.ArrayList<>();
		StringBuilder currentLine = new StringBuilder();
		
		for (String word : words) {
			String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
			if (fm.stringWidth(testLine) <= this.getWidth()) {
				currentLine = new StringBuilder(testLine);
			} else {
				if (!currentLine.isEmpty()) {
					lines.add(currentLine.toString());
					currentLine = new StringBuilder(word);
				} else {
					lines.add(word);
					currentLine = new StringBuilder();
				}
			}
		}
		if (!currentLine.isEmpty()) {
			lines.add(currentLine.toString());
		}
		
		int lineHeight = fm.getHeight();
		int totalHeight = lines.size() * lineHeight;
		int startY = this.getY() + ((this.getHeight() - totalHeight) / 2) + fm.getAscent();
		
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			int textWidth = fm.stringWidth(line);
			int textX = this.getX() + (this.getWidth() - textWidth) / 2;
			int textY = startY + (i * lineHeight);
			
			g.drawString(line, textX, textY);
		}
	}
}

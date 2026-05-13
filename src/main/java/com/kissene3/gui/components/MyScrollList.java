package com.kissene3.gui.components;

import com.kissene3.gui.utilities.MyUtilities.Coordinates;
import com.kissene3.gui.utilities.MyUtilities.Dimension;
import com.kissene3.gui.Theme;
import com.kissene3.gui.interfaces.Clickable;
import com.kissene3.gui.interfaces.Drawable;
import com.kissene3.gui.interfaces.Scrollable;
import com.kissene3.gui.io.AppLogger;
import com.kissene3.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MyScrollList extends MyComponent implements Clickable, Scrollable, Drawable {
	
	private final List<MyLabel> items = new ArrayList<>();
	private int scrollOffset = 0;
	private int selectedIndex = -1;
	private final int itemHeight;
	private boolean isVisible = true;
	private Screen parentScreen;
	private final int p = 5;
	private final int p2 = 2 * this.p;
	
	public MyScrollList(int x, int y, int width, int height, int itemHeight) {
		super(new Coordinates(x, y), new Dimension(width, height));
		this.itemHeight = itemHeight;
	}
	
	public void addItem(String text) {
		int relativeY = this.items.size() * this.itemHeight + this.p2;
		MyLabel label = new MyLabel(this.p2, relativeY, this.getWidth() - (2 * this.p2), this.itemHeight, text) {
			@Override
			public @NotNull Color getColorWhenActive() {
				return MyScrollList.this.getTheme().text();
			}
			@Override
			public @NotNull Color getColorWhenInactive() {
				return MyScrollList.this.getTheme().text();
			}
		};
		label.setFont(new Font("Monospaced", Font.BOLD, (int)(this.itemHeight * 0.9)));
		this.items.add(label);
	}
	
	public void select(String text) {
		for (int i = 0; i < this.items.size(); i++) {
			String s = this.items.get(i).getText();
			assert s != null;
			if (s.equalsIgnoreCase(text)) {
				this.selectedIndex = i;
				this.onSelectionChanged(i);
				return;
			}
		}
		this.selectedIndex = 0;
		this.onSelectionChanged(0);
	}
	
	public List<MyLabel> getItems() {
		return this.items;
	}
	
	public Screen getParentScreen() {
		return this.parentScreen;
	}
	
	public void setParentScreen(Screen parentScreen) {
		this.parentScreen = parentScreen;
	}
	
	public @NotNull Theme getTheme() {
		return this.parentScreen.getManager().getSettings().getTheme();
	}
	
	public int getSize() {
		return this.items.size();
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		if (!this.isVisible) return;
		
		this.drawBackground(g);
		
		Shape oldClip = g.getClip();
		
		g.setClip(this.getX() + this.p2, this.getY() + this.p2, this.getWidth() - (2 * this.p2), this.getHeight() - (2 * this.p2));
		
		g.translate(this.getX(), this.getY() - this.scrollOffset);
		
		for (int i = 0; i < this.items.size(); i++) {
			MyLabel label = this.items.get(i);
			if (i == this.selectedIndex) {
				g.setColor(this.getTheme().selectedDifficulty());
				g.fillRect(label.getX(), label.getY(), label.getWidth(), label.getHeight());
			}
			label.draw(g);
		}
		
		g.translate(-this.getX(), -(this.getY() - this.scrollOffset));
		g.setClip(oldClip);
		
		this.drawShades(g);
	}
	
	protected void drawBackground(@NotNull Graphics2D g) {
		g.setColor(this.getTheme().background());
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	protected void drawShades(@NotNull Graphics2D g) {
		int reducedW = this.getWidth() - this.p;
		int reducedW2 = this.getWidth() - this.p2;
		int reducedH = this.getHeight() - this.p;
		int reducedH2 = this.getHeight() - this.p2;
		
		g.setColor(this.getTheme().backgroundLighterShade());
		g.fillRect(this.getX(), this.getY(), reducedW, this.p);			// top top-line
		g.fillRect(this.getX(), this.getY(), reducedW2, this.p2);		// bottom top-line
		g.fillRect(this.getX(), this.getY(), this.p, reducedH);			// left left-line
		g.fillRect(this.getX(), this.getY(), this.p2, reducedH2);		// right left-line
		
		g.setColor(this.getTheme().backgroundDarkerShade());
		g.fillRect(this.getX() + reducedW, this.getY() + this.p, this.p, reducedH);		// right right-line
		g.fillRect(this.getX() + reducedW2, this.getY() + this.p2, this.p, reducedH2);	// left right-line
		g.fillRect(this.getX() + this.p, this.getY() + reducedH, reducedW, this.p);		// bottom bottom-line
		g.fillRect(this.getX() + this.p2, this.getY() + reducedH2, reducedW2, this.p);	// top bottom-line
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
	public void onClick(@NotNull MouseEvent e) {
		if (!this.isActive || !this.isVisible) return;
		
		int screenY = this.parentScreen.getY();
		int listY = this.getY();
		
		int relativeMouseY = e.getY() - screenY - listY + this.scrollOffset - this.p2;
		int clickedIndex = relativeMouseY / this.itemHeight;
		
		if (clickedIndex >= 0 && clickedIndex < this.items.size()) {
			this.selectedIndex = clickedIndex;
			this.onSelectionChanged(clickedIndex);
		}
	}
	
	@Override
	public void onMouseWheel(@NotNull MouseWheelEvent e) {
		// e.getWheelRotation(): 1 scroll down, -1 scroll up
		this.scrollOffset += e.getWheelRotation() * 20;
		
		int viewableHeight = this.getHeight() - (2 * this.p2);
		int contentHeight = this.items.size() * this.itemHeight;
		
		int maxScroll = Math.max(0, contentHeight - viewableHeight);
		
		if (this.scrollOffset < 0)
			this.scrollOffset = 0;
		if (this.scrollOffset > maxScroll)
			this.scrollOffset = maxScroll;
	}
	
	public void onSelectionChanged(int newIndex) {
		AppLogger.info("Selected difficulty: " + this.items.get(newIndex).getText());
	}
	
	@Override public void onPress(@NotNull MouseEvent e) {}
	@Override public void onRelease(@NotNull MouseEvent e) {}
	
	public int getSelectedIndex() {
		return this.selectedIndex;
	}
}
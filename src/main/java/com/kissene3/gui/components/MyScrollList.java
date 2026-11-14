package com.kissene3.gui.components;

import com.kissene3.gui.models.Theme;
import com.kissene3.gui.util.*;
import com.kissene3.gui.interfaces.*;
import com.kissene3.gui.screens.*;
import com.kissene3.gui.util.*;
import com.kissene3.io.AppLogger;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

@SuppressWarnings("unused")
public class MyScrollList extends MyComponent implements Clickable, Scrollable, Drawable {
	
	private final List<MyLabel> items = new ArrayList<>();
	private final Textures textures;
	private int scrollOffset = 0;
	private int selectedIndex = -1;
	private final int itemHeight;
	private boolean isVisible = true;
	private Screen parentScreen;
	private final int p = 5;
	private final int p2 = 2 * this.p;
	
	public MyScrollList(int x, int y, int width, int height, int itemHeight) {
		super(new MyCoordinates(x, y), new MyDimension(width, height));
		this.itemHeight = itemHeight;
		this.textures = new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, CLOSED ->
							new MyDimension(MyScrollList.this.getWidth(), MyScrollList.this.getHeight());
					default -> throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull MyDimension dimension) {
				switch (key) {
					case BACKGROUND -> drawBackground(
							g,
							new MyCoordinates(0, 0),
							dimension,
							MyScrollList.this.getTheme().background()
					);
					case CLOSED -> drawClosed(
							g,
							MyScrollList.this.p,
							new MyCoordinates(0, 0),
							dimension,
							MyScrollList.this.getTheme().backgroundLighterShade(),
							MyScrollList.this.getTheme().backgroundDarkerShade()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
		TextureManager.addPack(this.textures);
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
		
		g.drawImage(this.textures.getAsset(Textures.Key.BACKGROUND), this.getX(), this.getY(), null);
		
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
		
		g.drawImage(this.textures.getAsset(Textures.Key.CLOSED), this.getX(), this.getY(), null);
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
	
	@Override
	public void dispose() {
		this.textures.dispose();
		this.parentScreen = null;
		this.items.clear();
		super.dispose();
	}
}
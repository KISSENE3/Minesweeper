package com.kissene3.gui.components;

import com.kissene3.gui.util.*;
import com.kissene3.gui.interfaces.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;

/** A basic component that implements the interfaces to draw on the screen and to handle mouse inputs. */
@SuppressWarnings("unused")
public abstract class  	MyAbstractButton extends MyComponent implements Clickable, Hoverable, Drawable {
	/* ---------- FLAGS ---------- */
	
	protected boolean isVisible = true;
	protected boolean isHovered = false;
	
	/* ---------- FLAGS ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public MyAbstractButton(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public MyAbstractButton(@NotNull MyCoordinates topLeft, @NotNull MyDimension dimensions) {
		super(topLeft, dimensions);
	}
	
	public MyAbstractButton(@NotNull MyRectangle bounds) {
		super(bounds);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- @Override Clickable ---------- */
	
	@Override
	public abstract void onClick(@NotNull MouseEvent e);
	@Override
	public void onPress(@NotNull MouseEvent e) {}
	@Override
	public void onRelease(@NotNull MouseEvent e) {}
	
	/* ---------- @Override Clickable ---------- */
	/* ---------- @Override Hoverable ---------- */
	
	@Override
	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}
	
	@Override
	public boolean isHovered() {
		return this.isHovered;
	}
	
	@Override
	public void onHover(@NotNull MouseEvent e) {}
	
	@Override
	public void onEnter(@NotNull MouseEvent e) {}
	
	@Override
	public void onExit(@NotNull MouseEvent e) {}
	
	/* ---------- @Override Hoverable ---------- */
	/* ---------- @Override Drawable ---------- */
	
	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	public boolean isVisible() {
		return this.isVisible;
	}
	
	@Override
	public abstract void draw(@NotNull Graphics2D g);

	/* ---------- @Override Drawable ---------- */
}

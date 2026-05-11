package com.kissene3.gui;

import com.kissene3.gui.components.*;
import com.kissene3.gui.interfaces.*;
import com.kissene3.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MyGUIManager extends MouseAdapter implements KeyListener {
	
	/* ---------- ATTRIBUTES ---------- */
	
	private final Screen parentScreen;
	
	private final List<MyComponent> components = new ArrayList<>();
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- ATTRIBUTES FOR HANDLING INTERFACES ---------- */
	
	/** The component currently focused. Used to handle keyboard input ({@link KeyListener KeyListener}). */
	private Focusable focusedComponent = null;
	
	/** The last component being hovered (it could also be the currently hovered component).
	 * Used to handle the mouse position ({@link MouseMotionListener MouseMotionListener}).
	 */
	private Hoverable lastHovered = null;
	
	/** The last component being pressed. Used to handle mouse input ({@link MouseListener MouseListener}). */
	private Clickable lastPressed = null;
	
	private MouseEvent lastMouseEvent = null;
	
	/* ---------- ATTRIBUTES FOR HANDLING INTERFACES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public MyGUIManager(Screen parentScreen) {
		this.parentScreen = parentScreen;
	}
	
	public MyGUIManager(Screen parentScreen, MyComponent component) {
		this(parentScreen);
		this.components.add(component);
	}
	
	public MyGUIManager(Screen parentScreen, List<MyComponent> components) {
		this(parentScreen);
		this.components.addAll(components);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	
	public void onTick() {
		for (MyComponent component : this.components) {
			if (component instanceof Tickable tickable)
				tickable.tick();
		}
	}
	/* ---------- @Override MouseListener ---------- */
	
	@Override
	public void mousePressed(@NotNull MouseEvent e) {
		this.lastMouseEvent = e;
		Point p = this.getAdjustedPoint(e);
		
		boolean hitSomething = false;
		
		for (int i = this.components.size() - 1; i >= 0; i--) {
			MyComponent component = this.components.get(i);
			
			if (!component.isActive() || !component.contains(p.x, p.y))
				continue;
			
			hitSomething = true;
			this.handleFocusChange(component);
			
			if (component instanceof Clickable clickable) {
				this.lastPressed = clickable;
				clickable.onPress(e);
			}
			
			break;
		}
		
		if (!hitSomething) {
			this.clearFocus();
		}
	}
	
	@Override
	public void mouseReleased(@NotNull MouseEvent e) {
		this.lastMouseEvent = e;
		Point p = this.getAdjustedPoint(e);
		
		if (this.lastPressed == null)
			return;
		
		this.lastPressed.onRelease(e);
		
		for (int i = this.components.size() - 1; i >= 0; i--) {
			MyComponent component = this.components.get(i);
			
			if (!component.isActive() || !component.contains(p.x, p.y))
				continue;
			
			if (component instanceof Clickable clickable && this.lastPressed == clickable)
				clickable.onClick(e);
			break;
		}
		
		this.lastPressed = null;
	}
	
	/* ---------- @Override MouseListener ---------- */
	/* ---------- @Override MouseWheelListener ---------- */
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		this.lastMouseEvent = e;
		Point p = this.getAdjustedPoint(e);
		
		for (int i = this.components.size() - 1; i >= 0; i--) {
			MyComponent component = this.components.get(i);
			
			if (component.isActive() && component.contains(p.x, p.y)) {
				if (component instanceof Scrollable scrollable) {
					scrollable.onMouseWheel(e);
					return;
				}
			}
		}
	}
	
	/* ---------- @Override MouseWheelListener ---------- */
	/* ---------- @Override MouseMotionListener ---------- */
	
	@Override
	public void mouseDragged(MouseEvent e) {
		this.mouseMoved(e);
	}
	
	@Override
	public void mouseMoved(@NotNull MouseEvent e) {
		this.lastMouseEvent = e;
		Hoverable currentHovered = null;
		
		for (int i = this.components.size() - 1; i >= 0; i--) {
			MyComponent component = this.components.get(i);
			Point p = this.getAdjustedPoint(e);
			
			if (component.isActive() && component instanceof Hoverable hoverable && component.contains(p.x, p.y)) {
				currentHovered = hoverable;
				break;
			}
		}
		
		if (currentHovered != this.lastHovered) {
			if (this.lastHovered != null) {
				this.lastHovered.setHovered(false);
				this.lastHovered.onExit(e);
			}
			if (currentHovered != null) {
				currentHovered.setHovered(true);
				currentHovered.onEnter(e);
			}
			this.lastHovered = currentHovered;
		}
		
		if (currentHovered != null) {
			currentHovered.onHover(e);
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if (this.lastHovered == null)
			return;
		
		this.lastHovered.setHovered(false);
		this.lastHovered.onExit(e);
		this.lastHovered = null;
	}
	
	/* ---------- @Override MouseMotionListener ---------- */
	/* ---------- @Override KeyListener ---------- */
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (this.focusedComponent == null)
			return;
		
		this.focusedComponent.onKeyPressed(e);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if (this.focusedComponent == null)
			return;
		
		this.focusedComponent.onKeyTyped(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (this.focusedComponent == null)
			return;
		
		this.focusedComponent.onKeyReleased(e);
	}
	
	/* ---------- @Override KeyListener ---------- */
	/* ---------- UTILITY ---------- */
	
	public void handleFocusChange(MyComponent clicked) {
		if (this.focusedComponent == clicked)
			return;
		
		this.clearFocus();
		
		if (!(clicked instanceof Focusable focusable))
			return;
		
		this.focusedComponent = focusable;
		focusable.setFocus(true);
	}
	
	private void clearFocus() {
		if (this.focusedComponent == null)
			return;
		
		this.focusedComponent.setFocus(false);
		this.focusedComponent = null;
	}
	
	public void clearManager() {
		this.clearFocus();
		this.mouseExited(this.lastMouseEvent);
	}
	
	/* ---------- UTILITY ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	/** Adds a component to the manager. */
	public void addComponent(@NotNull MyComponent c) {
		this.components.add(c);
	}
	
	/** Adds a {@link List} of components to the manager. */
	public void addComponents(@NotNull List<? extends MyComponent> components) {
		this.components.addAll(components);
	}
	
	/** Removes a component and cleans up its states. */
	public void removeComponent(@NotNull MyComponent component) {
		if (!this.components.remove(component))
			return;
		
		if (this.focusedComponent == component) {
			this.clearFocus();
		}
		if (this.lastHovered == component) {
			this.lastHovered.setHovered(false);
			this.lastHovered.onExit(this.lastMouseEvent);
			this.lastHovered = null;
		}
		if (this.lastPressed == component) {
			this.lastPressed = null;
		}
	}
	
	/** Moves a component to the back of the list so it's the first iterated. */
	public void bringToFront(@NotNull MyComponent component) {
		if (this.components.remove(component))
			this.components.add(component);
	}
	
	public List<MyComponent> getComponents() {
		return this.components;
	}
	
	public Dimension getPreferredSize() {
		int maxW = 0;
		int maxH = 0;
		for (MyComponent component : components) {
			if (!(component instanceof Drawable drawable) || !drawable.isVisible())
				continue;
			
			maxW = Math.max(maxW, component.getX() + component.getWidth());
			maxH = Math.max(maxH, component.getY() + component.getHeight());
		}
		return new Dimension(maxW, maxH);
	}
	
	public Screen getParentScreen() {
		return this.parentScreen;
	}
	
	private @NotNull Point getAdjustedPoint(@Nullable MouseEvent e) {
		if (e == null) {
			return new Point(-1, -1);
		}
		
		return new Point(e.getX() - this.parentScreen.getX(), e.getY() - this.parentScreen.getY());
	}
	
	/* ---------- GETTERS/SETTERS ---------- */
	/* ---------- DRAW ---------- */
	
	public void draw(Graphics2D g) {
		for (MyComponent component : this.components) {
			if (!(component instanceof Drawable drawable))
				continue;
			if (!drawable.isVisible())
				continue;
			drawable.draw(g);
		}
		
		if (this.lastHovered == null || this.lastMouseEvent == null || !(this.lastHovered instanceof Drawable drawable) || !drawable.isVisible())
			return;

		drawable.drawTooltip(g, this.lastMouseEvent);
	}
	
	/* ---------- DRAW ---------- */
}

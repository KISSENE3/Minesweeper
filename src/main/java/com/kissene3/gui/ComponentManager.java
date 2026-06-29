package com.kissene3.gui;

import com.kissene3.gui.interfaces.*;
import com.kissene3.gui.interfaces.Component;
import com.kissene3.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ComponentManager extends MouseAdapter implements KeyListener {
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	private final Screen parentScreen;
	private final List<Component> components = new ArrayList<>();
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- ATTRIBUTES FOR HANDLING INTERFACES ---------- */
	
	/**
	 * The component currently focused.<br />
	 * Used to handle keyboard input ({@link KeyListener KeyListener}).
	 */
	private Focusable focusedComponent = null;
	
	/**
	 * The last component being hovered (it could also be the currently hovered component).<br />
	 * Used to handle the mouse position ({@link MouseMotionListener MouseMotionListener}).
	 */
	private Hoverable lastHovered = null;
	
	/**
	 * The last component being pressed (it could also be the currently pressed component).<br />
	 * Used to handle mouse input ({@link MouseListener MouseListener}).
	 */
	private Clickable lastPressed = null;
	private MouseEvent lastMouseEvent = null;
	
	/* ---------- ATTRIBUTES FOR HANDLING INTERFACES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public ComponentManager(@NotNull Screen parentScreen) {
		this.parentScreen = parentScreen;
	}
	
	public ComponentManager(@NotNull Screen parentScreen, @NotNull Component component) {
		this(parentScreen);
		this.components.add(component);
	}
	
	public ComponentManager(@NotNull Screen parentScreen, @NotNull List<Component> components) {
		this(parentScreen);
		this.components.addAll(components);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	/** Adds a component to the manager. */
	public void addComponent(@NotNull Component c) {
		this.components.add(c);
	}
	
	/** Adds a {@link List} of components to the manager. */
	public void addComponents(@NotNull List<? extends Component> components) {
		this.components.addAll(components);
	}
	
	/** Removes a component and cleans up its states. */
	public void removeComponent(@NotNull Component component) {
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
			this.lastPressed.onRelease(this.lastMouseEvent);
			this.lastPressed = null;
		}
	}
	
	public void removeAllComponents() {
		if (this.focusedComponent != null) {
			this.clearFocus();
		}
		if (this.lastHovered != null) {
			this.lastHovered.setHovered(false);
			this.lastHovered.onExit(this.lastMouseEvent);
			this.lastHovered = null;
		}
		if (this.lastPressed != null) {
			this.lastPressed.onRelease(this.lastMouseEvent);
			this.lastPressed = null;
		}
		this.components.clear();
	}
	
	public List<Component> getComponents() {
		return this.components;
	}
	
	public Dimension getPreferredSize() {
		int maxW = 0;
		int maxH = 0;
		for (Component component : this.components) {
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
	/* ---------- @Override MouseListener ---------- */
	
	@Override
	public void mousePressed(@NotNull MouseEvent e) {
		this.lastMouseEvent = e;
		Point p = this.getAdjustedPoint(e);
		
		boolean hitSomething = false;
		
		for (int i = this.components.size() - 1; i >= 0; i--) {
			Component component = this.components.get(i);
			
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
			Component component = this.components.get(i);
			
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
			Component component = this.components.get(i);
			
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
			Component component = this.components.get(i);
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
	
	public void handleFocusChange(Component clicked) {
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
	
	/** Moves a component to the back of the list so it's the first iterated. */
	public void bringToFront(@NotNull Component component) {
		if (this.components.remove(component))
			this.components.add(component);
	}
	
	/* ---------- UTILITY ---------- */
	/* ---------- DRAW ---------- */
	
	public void draw(Graphics2D g) {
		for (Component component : this.components) {
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
	/* ---------- TPS ---------- */
	
	public void onTick() {
		for (Component component : this.components) {
			if (component instanceof Tickable tickable)
				tickable.tick();
		}
	}
	
	/* ---------- TPS ---------- */
}

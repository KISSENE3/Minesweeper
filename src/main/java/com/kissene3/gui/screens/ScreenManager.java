package com.kissene3.gui.screens;

import com.kissene3.gui.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class ScreenManager {
	private final Deque<Screen> screenStack = new ArrayDeque<>();
	private final Canvas canvas;
	private final Frame frame;
	private final Settings settings;
	
	public ScreenManager(Canvas canvas, Frame frame, Settings settings) {
		this.canvas = canvas;
		this.frame = frame;
		this.settings = settings;
	}

	public Settings getSettings() {
		return this.settings;
	}
	
	/** Adds a new {@link Screen} in the stack (without destroying the current one). */
	public void pushScreen(Screen newScreen) {
		if (!this.screenStack.isEmpty()) {
			removeListeners(this.screenStack.peek());
		}
		
		newScreen.setManager(this);
		
		this.screenStack.push(newScreen);
		applyScreen(newScreen);
	}
	
	/** Shows the previous {@link Screen} in the stack, destroying the current one. */
	public void popScreen() {
		if (this.screenStack.size() <= 1)
			return;
		removeListeners(this.screenStack.pop());
		assert this.screenStack.peek() != null;
		applyScreen(this.screenStack.peek());
	}
	
	private void applyScreen(@NotNull Screen screen) {
		Dimension size = new Dimension(screen.getWidth(), screen.getHeight());
		this.canvas.setPreferredSize(size);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		
		this.canvas.addMouseListener(screen.getComponentsManager());
		this.canvas.addMouseMotionListener(screen.getComponentsManager());
		this.canvas.addKeyListener(screen.getComponentsManager());
		
		this.canvas.createBufferStrategy(2);
	}
	
	private void removeListeners(@NotNull Screen screen) {
		this.canvas.removeMouseListener(screen.getComponentsManager());
		this.canvas.removeMouseMotionListener(screen.getComponentsManager());
		this.canvas.removeKeyListener(screen.getComponentsManager());
	}
	
	public void draw(Graphics2D g) {
		if (!this.screenStack.isEmpty()) {
			this.screenStack.peek().draw(g);
		}
	}
}

package com.kissene3.gui;

import com.kissene3.gui.models.Settings;
import com.kissene3.gui.screens.Screen;
import com.kissene3.io.AppLogger;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public class ScreenManager {
	protected final Deque<Screen> screenStack = new ArrayDeque<>();
	private final Queue<Runnable> drawPendingActions = new ConcurrentLinkedQueue<>();
	private final Queue<Runnable> tickPendingActions = new ConcurrentLinkedQueue<>();
	protected final Canvas canvas;
	protected final Frame frame;
	protected final Settings settings;
	
	public ScreenManager(@NotNull Canvas canvas, @NotNull Frame frame, @NotNull Settings settings) {
		this.canvas = canvas;
		this.frame = frame;
		this.settings = settings;
		AppLogger.info(this.getClass().getSimpleName() + " initialized.");
	}

	public Settings getSettings() {
		return this.settings;
	}
	
	/** Adds a new {@link Screen} in the stack (without destroying the current one). */
	public void pushScreen(@NotNull Screen newScreen) {
		if (!this.screenStack.isEmpty()) {
			removeListeners(this.screenStack.peek());
		}
		
		newScreen.setManager(this);
		
		this.screenStack.push(newScreen);
		this.applyScreen(newScreen);
		AppLogger.info("Pushed screen: " + newScreen.getClass().getSimpleName());
	}
	
	/** Shows the previous {@link Screen} in the stack, disposing the current one. */
	public void popScreen() {
		if (this.screenStack.size() <= 1)
			return;
		
		Screen toRemove = this.screenStack.pop();
		AppLogger.info("Popped screen: " + toRemove.getClass().getSimpleName());
		toRemove.dispose();
		
		assert this.screenStack.peek() != null;
		Screen currentScreen = this.screenStack.peek();
		
		this.applyScreen(currentScreen);
		AppLogger.info("Current screen: " + currentScreen.getClass().getSimpleName());
	}
	
	public void popAll() {
		while (!this.screenStack.isEmpty()) {
			Screen currentScreen = this.screenStack.pop();
			this.removeListeners(currentScreen);
			currentScreen.dispose();
		}
		AppLogger.info("Popped all screens.");
	}
	
	protected void applyScreen(@NotNull Screen screen) {
		for (Screen s : this.screenStack) {
			if (s != screen)
				this.removeListeners(s);
		}
		
		if (!screen.isTransparent()) {
			Dimension size = new Dimension(screen.getWidth(), screen.getHeight());
			this.canvas.setPreferredSize(size);
			this.canvas.setSize(size);
			this.frame.pack();
			this.frame.setLocationRelativeTo(null);
		}
		
		this.canvas.addMouseListener(screen.getComponentsManager());
		this.canvas.addMouseWheelListener(screen.getComponentsManager());
		this.canvas.addMouseMotionListener(screen.getComponentsManager());
		this.canvas.addKeyListener(screen.getComponentsManager());
		
		if (this.canvas.getBufferStrategy() == null) {
			this.canvas.createBufferStrategy(2);
		}
	}
	
	protected void removeListeners(@NotNull Screen screen) {
		this.canvas.removeMouseListener(screen.getComponentsManager());
		this.canvas.removeMouseWheelListener(screen.getComponentsManager());
		this.canvas.removeMouseMotionListener(screen.getComponentsManager());
		this.canvas.removeKeyListener(screen.getComponentsManager());
		screen.getComponentsManager().clearManager();
	}
	
	public @NotNull Canvas getCanvas() {
		return this.canvas;
	}
	
	public void runAfterDraw(@NotNull Runnable action) {
		this.drawPendingActions.add(action);
	}
	
	public void runAfterTick(@NotNull Runnable action) {
		this.tickPendingActions.add(action);
	}
	
	public void executeDrawPendingActions() {
		while (!this.drawPendingActions.isEmpty()) {
			this.drawPendingActions.poll().run();
		}
	}
	
	public void executeTickPendingActions() {
		while (!this.tickPendingActions.isEmpty()) {
			this.tickPendingActions.poll().run();
		}
	}
	
	public void draw(@NotNull Graphics2D g) {
		if (this.screenStack.isEmpty()) {
			return;
		}

		List<Screen> toDraw = new ArrayList<>();
		for (Screen screen : this.screenStack) {
			toDraw.addFirst(screen);
			if (!screen.isTransparent())
				break;
		}
		
		Composite originalComposite = g.getComposite();
		for (int i = 0; i < toDraw.size(); i++) {
			Screen screen = toDraw.get(i);
			
			int tx = screen.getX();
			int ty = screen.getY();
			g.translate(tx, ty);
			
			screen.draw(g);
			
			g.translate(-tx, -ty);
			
			if (i == toDraw.size() - 2) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
				
				g.setComposite(originalComposite);
			}
		}
		
		this.executeDrawPendingActions();
	}
	
	public void onTick() {
		if (this.screenStack.isEmpty())
			return;
		this.screenStack.peek().onTick();
		
		this.executeTickPendingActions();
	}
}

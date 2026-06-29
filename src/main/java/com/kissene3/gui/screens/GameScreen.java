package com.kissene3.gui.screens;

import com.kissene3.game.Difficulty;
import com.kissene3.gui.GUIGrid;
import com.kissene3.gui.Theme;
import com.kissene3.gui.components.MinesweeperButton;
import com.kissene3.gui.utilities.MyUtilities.Coordinates;
import com.kissene3.gui.io.AppLogger;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GameScreen extends Screen {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	private static final int MAX_SCREEN_WIDTH = 1280;
	private static final int MAX_SCREEN_HEIGHT = 768;
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	private final int HEADER_HEIGHT = 64;
	private final GUIGrid grid;
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public GameScreen(@NotNull ScreenManager manager, @NotNull Difficulty difficulty) {
		super(manager);
		this.grid = new GUIGrid(
				new Coordinates(0, this.HEADER_HEIGHT),
				difficulty,
				this.manager.getSettings()
		);
		this.grid.createCells();
		this.grid.setViewport(this.getWidth(), this.getHeight() - this.HEADER_HEIGHT);
		
		this.componentsManager.addComponent(new MinesweeperButton(8, 8, 128, 48, "temp") {
			@Override
			public @NotNull Theme getTheme() {
				return GameScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				GameScreen.this.manager.getSettings().cycleCellSize();
			}
		});
		
		this.componentsManager.addComponent(this.grid);
		
		AppLogger.info("GameScreen initialized with grid: " + difficulty.width() + " * " + difficulty.height() + " | Mines: " + difficulty.numMines());
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	@Override
	public boolean isTransparent() {
		return false;
	}
	
	@Override
	public int getX() {
		return 0;
	}
	
	@Override
	public int getY() {
		return 0;
	}
	
	@Override
	public int getWidth() {
		return Math.min(this.grid.getWidth(), MAX_SCREEN_WIDTH);
	}
	
	@Override
	public int getHeight() {
		int requiredHeight = this.grid.getHeight() + this.HEADER_HEIGHT;
		return Math.min(requiredHeight, MAX_SCREEN_HEIGHT);
	}
	
	/* ---------- GETTERS/SETTERS ---------- */
	
	private void drawHeader(@NotNull Graphics2D g) {
		g.setColor(this.manager.getSettings().getTheme().background());
		g.fillRect(0, 0, this.getWidth(), this.HEADER_HEIGHT);
		g.setColor(this.manager.getSettings().getTheme().backgroundLighterShade());
		g.fillRect(0, 0, this.getWidth(), 4);
		g.setColor(this.manager.getSettings().getTheme().backgroundDarkerShade());
		g.fillRect(0, this.HEADER_HEIGHT - 4, this.getWidth(), 4);
		// TODO: HEADER
	}
	
	@Override
	public void onTick() {
		this.componentsManager.onTick();
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		this.drawHeader(g);
		
		this.componentsManager.draw(g);
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}

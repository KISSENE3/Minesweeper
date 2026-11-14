package com.kissene3.gui.screens;

import com.kissene3.game.*;
import com.kissene3.game.Timer;
import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.components.*;
import com.kissene3.gui.game.GuiGrid;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.util.*;
import com.kissene3.io.AppLogger;
import com.kissene3.io.Save;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;

public class GameScreen extends Screen {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	protected static final int MAX_SCREEN_WIDTH = 1280;
	protected static final int MAX_SCREEN_HEIGHT = 768;
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- FINAL ATTRIBUTES ---------- */
	
	protected final int HEADER_HEIGHT = 64;
	protected final GuiGrid grid;
	
	/* ---------- FINAL ATTRIBUTES ---------- */
	/* ---------- ATTRIBUTES ---------- */
	
	protected Smiley smiley;
	protected Textures textures;
	protected Timer timer;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public GameScreen(@NotNull ScreenManager manager, @NotNull Difficulty difficulty) {
		super(manager);
		this.init();
		
		this.grid = new GuiGrid(
				new MyCoordinates(0, this.HEADER_HEIGHT),
				difficulty,
				this,
				this.smiley
		);
		this.grid.createCells();
		this.moveGrid();
		this.componentsManager.addComponent(this.grid);
		this.timer = new Timer();
		
		AppLogger.info(this.getClass().getSimpleName() + " initialized with grid: " + difficulty.width() + " * " + difficulty.height() + " | Mines: " + difficulty.numMines());
	}
	
	public GameScreen(@NotNull ScreenManager manager, @NotNull Save.SaveDataPackage savedData) {
		super(manager);
		this.init();
		
		this.grid = new GuiGrid(
				new MyCoordinates(0, this.HEADER_HEIGHT),
				savedData.columns(),
				savedData.rows(),
				savedData.gameData(),
				this,
				this.smiley
		);
		this.grid.loadCellsFromState();
		this.moveGrid();
		this.componentsManager.addComponent(this.grid);
		this.timer = new Timer(savedData.thirties());
		
		AppLogger.info(this.getClass().getSimpleName() + " initialized with grid: " + savedData.columns() + " * " + savedData.rows() + " | Mines: " + this.grid.getNumMines() + ". Loaded from save file.");
	}
	
	protected void init() {
		this.textures = new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case FLAG, ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE ->
							new MyDimension(48, 48);
					default -> throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull MyDimension dimension) {
				switch (key) {
					case FLAG -> this.drawFlag(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.getManager().getSettings().getTheme().flag(),
							GameScreen.this.getManager().getSettings().getTheme().flagBase()
					);
					case ZERO -> this.drawNumberZero(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case ONE -> this.drawNumberOne(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case TWO -> this.drawNumberTwo(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case THREE -> this.drawNumberThree(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case FOUR -> this.drawNumberFour(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case FIVE -> this.drawNumberFive(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case SIX -> this.drawNumberSix(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case SEVEN -> this.drawNumberSeven(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case EIGHT -> this.drawNumberEight(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					case NINE -> this.drawNumberNine(
							g,
							3,
							new MyCoordinates(0, 0),
							GameScreen.this.manager.getSettings().getTheme().flag()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
		TextureManager.addPack(this.textures);
		
		this.componentsManager.addComponent(new PauseButton(16, this.HEADER_HEIGHT / 2 - 23, 46, 46, this) {
			@Override
			public Theme getTheme() {
				return GameScreen.this.manager.getSettings().getTheme();
			}
		});
		
		this.smiley = new Smiley(this.getWidth() / 2 - 23,  this.HEADER_HEIGHT / 2 - 23, 46, 46, this) {
			@Override
			public @NotNull Theme getTheme() {
				return GameScreen.this.manager.getSettings().getTheme();
			}
		};
		this.componentsManager.addComponent(this.smiley);
		
		this.componentsManager.addComponent(new MinesweeperButton(this.getWidth() - 46 - 16, this.HEADER_HEIGHT / 2 - 23, 46, 46, "?") {
			@Override
			public @NotNull Theme getTheme() {
				return GameScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				GameScreen.this.manager.runAfterDraw(() ->
						GameScreen.this.getManager().pushScreen(new HelpScreen(GameScreen.this.manager))
				);
			}
		});
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	public GuiGrid getGrid() {
		return this.grid;
	}
	
	public long getThirties() {
		return this.timer.getThirties();
	}
	
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
		return MAX_SCREEN_WIDTH;
	}
	
	@Override
	public int getHeight() {
		return MAX_SCREEN_HEIGHT;
	}
	
	/* ---------- GETTERS/SETTERS ---------- */
	
	public void moveGrid() {
		int x = Math.max(0, this.getWidth() / 2 - this.grid.getWidth() / 2);
		int y = Math.max(this.HEADER_HEIGHT, (this.getHeight() - this.HEADER_HEIGHT) / 2 - this.grid.getHeight() / 2 + this.HEADER_HEIGHT);
		int width = Math.min(this.getWidth(), this.grid.getWidth());
		int height = Math.min(this.getHeight() - this.HEADER_HEIGHT, this.grid.getHeight());
		this.grid.setBounds(x, y, this.grid.getWidth(), this.grid.getHeight());
		this.grid.setViewport(width, height);
	}
	
	private void drawHeader(@NotNull Graphics2D g) {
		g.setColor(this.manager.getSettings().getTheme().background());
		g.fillRect(0, 0, this.getWidth(), this.HEADER_HEIGHT);
		g.setColor(this.manager.getSettings().getTheme().backgroundLighterShade());
		g.fillRect(0, 0, this.getWidth(), 4);
		g.setColor(this.manager.getSettings().getTheme().backgroundDarkerShade());
		g.fillRect(0, this.HEADER_HEIGHT - 4, this.getWidth(), 4);
		
		int flagCounterFinalX = this.smiley.getX() - 64;
		int flagX = flagCounterFinalX - 48;
		
		g.drawImage(this.textures.getAsset(Textures.Key.FLAG), flagX, this.HEADER_HEIGHT / 2 - 24, null);
		
		int flagsRemaining = this.grid.getFlagsRemaining();
		String scoreStr = String.valueOf(flagsRemaining);
		
		g.setColor(this.manager.getSettings().getTheme().flag());
		int currentX = flagX - scoreStr.length() * 48;
		for (char c : scoreStr.toCharArray()) {
			if (Character.isDigit(c))
				g.drawImage(
					this.textures.getAsset(
						switch (c) {
							case '0' -> Textures.Key.ZERO;
							case '1' -> Textures.Key.ONE;
							case '2' -> Textures.Key.TWO;
							case '3' -> Textures.Key.THREE;
							case '4' -> Textures.Key.FOUR;
							case '5' -> Textures.Key.FIVE;
							case '6' -> Textures.Key.SIX;
							case '7' -> Textures.Key.SEVEN;
							case '8' -> Textures.Key.EIGHT;
							case '9' -> Textures.Key.NINE;
							default -> throw new UnsupportedOperationException("This should never happen.");
						}
					),
					currentX,
					this.HEADER_HEIGHT / 2 - 24,
					null
				);
			else if (c == '-')
				g.fillRect(currentX + 8, this.HEADER_HEIGHT / 2 - 4, 32, 8);
			currentX += 48;
		}
		
		String timeStr = this.timer.toString();
		
		int currentTimeX = this.smiley.getBottomRight().x() + 64;
		for (char c : timeStr.toCharArray()) {
			if (Character.isDigit(c))
				g.drawImage(
						this.textures.getAsset(
							switch (c) {
								case '0' -> Textures.Key.ZERO;
								case '1' -> Textures.Key.ONE;
								case '2' -> Textures.Key.TWO;
								case '3' -> Textures.Key.THREE;
								case '4' -> Textures.Key.FOUR;
								case '5' -> Textures.Key.FIVE;
								case '6' -> Textures.Key.SIX;
								case '7' -> Textures.Key.SEVEN;
								case '8' -> Textures.Key.EIGHT;
								case '9' -> Textures.Key.NINE;
								default -> throw new UnsupportedOperationException("This should never happen.");
							}
						),
						currentTimeX,
						this.HEADER_HEIGHT / 2 - 24,
						null
				);
			else if (c == ':') {
				g.fillRect(currentTimeX + 22, this.HEADER_HEIGHT / 2 - 12, 4, 4);
				g.fillRect(currentTimeX + 22, this.HEADER_HEIGHT / 2 + 8, 4, 4);
			}
			currentTimeX += 48;
		}
	}
	
	@Override
	public void onTick() {
		this.componentsManager.onTick();
		
		if (this.grid == null)
			return;
		
		if (!this.grid.isGameStarted())
			return;
		
		if (this.grid.getGameStatus() == Status.RUNNING) {
			this.timer.increment();
		} else {
			Save.deleteSaveFile();
		}
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		g.setColor(this.backgroundColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		this.drawHeader(g);
		
		this.componentsManager.draw(g);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}

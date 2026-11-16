package com.kissene3.gui.screens;

import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.components.*;
import com.kissene3.gui.io.*;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.util.*;
import com.kissene3.io.AppLogger;
import com.kissene3.io.Save;
import com.kissene3.main.Main;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;

public class MenuScreen extends Screen {
	private final char[][] backgroundLayout = new char[][] {
			{'0', '0', '0', '1', '1', '1', '0', '0', '0', '1', 'M', ' ', 'M', ' ', ' ', ' '},
			{'0', '0', '0', '2', 'F', '2', '0', '0', '1', '2', ' ', ' ', 'M', ' ', 'M', 'M'},
			{'1', '2', '3', '5', 'F', '3', '0', '0', '1', 'F', ' ', ' ', 'M', ' ', ' ', 'M'},
			{'1', 'F', 'F', 'F', 'F', '3', '0', '1', '3', '3', ' ', 'M', ' ', ' ', 'M', ' '},
			{'3', '6', 'F', '8', 'F', '4', '1', '2', 'F', 'F', ' ', ' ', ' ', 'M', ' ', ' '},
			{'F', 'F', 'F', 'F', 'F', '5', 'F', '3', '2', '2', ' ', 'M', ' ', ' ', 'M', ' '},
			{' ', ' ', 'M', ' ', 'F', '7', 'F', '4', '2', '2', ' ', ' ', 'M', ' ', ' ', 'M'},
			{'M', ' ', ' ', 'M', 'F', 'F', 'F', 'X', 'B', 'M', ' ', ' ', 'M', 'M', ' ', ' '},
			{' ', 'M', ' ', ' ', ' ', 'M', ' ', ' ', 'M', ' ', ' ', 'M', ' ', ' ', ' ', 'M'},
			{'M', ' ', 'M', ' ', 'M', ' ', 'M', 'M', ' ', ' ', ' ', ' ', ' ', ' ', 'M', ' '},
			{'M', ' ', 'M', ' ', 'M', ' ', ' ', ' ', 'M', ' ', ' ', 'M', ' ', ' ', 'M', ' '},
			{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'M', 'M', 'M', ' ', ' ', ' ', ' ', 'M'}
	};
	private static final int CELL_SIZE = 64;
	private static final int P_TITLE = 8;
	private final Textures textures;
	
	public MenuScreen(@NotNull ScreenManager manager) {
		super(manager);
		this.init();
		int pCell = CELL_SIZE / 16;
		this.textures = new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, EXPLODED, CLOSED, REVEALED, MINE, FLAG, CROSS,
						 ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT ->  new MyDimension(CELL_SIZE, CELL_SIZE);
					case TITLE -> new MyDimension(121 * P_TITLE, 16 * P_TITLE);
					default -> throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull MyDimension dimension) {
				switch (key) {
					case BACKGROUND -> this.drawBackground(
							g,
							new MyCoordinates(0, 0),
							dimension,
							MenuScreen.this.manager.getSettings().getTheme().background()
					);
					case EXPLODED -> this.drawBackground(
							g,
							new MyCoordinates(0, 0),
							dimension,
							MenuScreen.this.manager.getSettings().getTheme().backgroundExplodedMine()
					);
					case CLOSED -> this.drawClosed(
							g, pCell,
							new MyCoordinates(0, 0),
							dimension,
							MenuScreen.this.manager.getSettings().getTheme().backgroundLighterShade(),
							MenuScreen.this.manager.getSettings().getTheme().backgroundDarkerShade()
					);
					case REVEALED -> this.drawRevealed(
							g, pCell,
							new MyCoordinates(0, 0),
							dimension,
							MenuScreen.this.manager.getSettings().getTheme().border()
					);
					case MINE -> this.drawMine(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().mine(),
							MenuScreen.this.manager.getSettings().getTheme().mineShade()
					);
					case FLAG -> this.drawFlag(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().flag(),
							MenuScreen.this.manager.getSettings().getTheme().flagBase()
					);
					case CROSS -> this.drawCross(
							g, pCell,
							new MyCoordinates(0, 0),
							dimension,
							MenuScreen.this.manager.getSettings().getTheme().cross()
					);
					case ONE -> this.drawNumberOne(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberOne()
					);
					case TWO -> this.drawNumberTwo(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberTwo()
					);
					case THREE -> this.drawNumberThree(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberThree()
					);
					case FOUR -> this.drawNumberFour(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberFour()
					);
					case FIVE -> this.drawNumberFive(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberFive()
					);
					case SIX -> this.drawNumberSix(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberSix()
					);
					case SEVEN -> this.drawNumberSeven(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberSeven()
					);
					case EIGHT -> this.drawNumberEight(
							g, pCell,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberEight()
					);
					case TITLE -> this.drawTitle(
							g, P_TITLE,
							new MyCoordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().title(),
							MenuScreen.this.manager.getSettings().getTheme().titleOutline()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
		TextureManager.addPack(this.textures);
	}
	
	private void init() {
		this.componentsManager.addComponent(this.getMinesweeperButton((this.getWidth() - 192) / 2, this.getHeight() - 96, "QUIT",
			(e) -> {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				SettingsRegistry.save(this.manager.getSettings());
				AppLogger.info("Quitting...\n\n");
				System.exit(0);
			}));
		
		this.componentsManager.addComponent(this.getMinesweeperButton((this.getWidth() - 192) / 2, this.getHeight() - 192, "SETTINGS",
			(e) -> {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				this.manager.runAfterDraw(() -> this.manager.pushScreen(new SettingsScreen(this.manager)));
			}));
		
		this.componentsManager.addComponent(this.getMinesweeperButton((this.getWidth() - 192) / 2, this.getHeight() - 288, "PLAY",
			(e) -> {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				
				if (Save.hasSaveFile())
					this.manager.pushScreen(new ConfirmScreen(
							this.manager,
							"Do you want to load your last save?",
							() -> {
								BigInteger gameData = Save.loadFile();
								if (gameData == null)
									return;
								this.manager.pushScreen(new GameScreen(this.manager, Save.parseSaveData(gameData)));
							},
							() -> this.manager.pushScreen(new DifficultyScreen(this.manager))));
				else
					this.manager.runAfterDraw(() -> this.manager.pushScreen(new DifficultyScreen(MenuScreen.this.manager)));
			}));
		
		this.componentsManager.addComponent(new GitHubButton(16, this.getHeight() - 64, 48) {
			@Override
			public Theme getTheme() {
				return MenuScreen.this.manager.getSettings().getTheme();
			}
		});
		
		AppLogger.info(this.getClass().getSimpleName() + " initialized.");
	}
	
	private @NotNull MinesweeperButton getMinesweeperButton(int x, int y, String label, Consumer<MouseEvent> action) {
		return new MinesweeperButton(x, y, 192, 64, label) {
			@Override
			public void onClick(@NotNull MouseEvent e) {
				action.accept(e);
			}
			
			@Override
			public Theme getTheme() {
				return MenuScreen.this.manager.getSettings().getTheme();
			}
		};
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
		return 64 * 16;
	}
	
	@Override
	public int getHeight() {
		return 64 * 12;
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		for (int y = 0; y < this.backgroundLayout.length; y++)
			for (int x = 0; x < this.backgroundLayout[y].length; x++)
				this.drawCell(g, this.backgroundLayout[y][x], x * CELL_SIZE, y * CELL_SIZE);
		
		g.setColor(new Color(
				this.manager.getSettings().getTheme().background().getRed(),
				this.manager.getSettings().getTheme().background().getGreen(),
				this.manager.getSettings().getTheme().background().getBlue(),
				192
		));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int startX = (this.getWidth() - 121 * P_TITLE) / 2;
		g.drawImage(this.getTexture(Textures.Key.TITLE), startX, 150, null);
		
		this.componentsManager.draw(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
		g2d.setColor(this.manager.getSettings().getTheme().text());
		
		String versionText = "v" + Main.version;
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(versionText);
		
		int xPos = this.getWidth() - textWidth - 4;
		int yPos = this.getHeight() - 4;
		
		g2d.drawString(versionText, xPos, yPos);
		g2d.dispose();
	}
	
	private void drawCell(@NotNull Graphics2D g, char c, int x, int y) {
		if (c == 'B') {
			g.drawImage(this.getTexture(Textures.Key.EXPLODED), x, y, null);
		} else {
			g.drawImage(this.getTexture(Textures.Key.BACKGROUND), x, y, null);
		}
		
		switch (c) {
			case '0', '1', '2', '3', '4', '5', '6', '7', '8', 'M', 'B' ->
				g.drawImage(this.getTexture(Textures.Key.REVEALED), x, y, null);
			default ->
				g.drawImage(this.getTexture(Textures.Key.CLOSED), x, y, null);
		}
		
		switch (c) {
			case '1' -> g.drawImage(this.getTexture(Textures.Key.ONE), x, y, null);
			case '2' -> g.drawImage(this.getTexture(Textures.Key.TWO), x, y, null);
			case '3' -> g.drawImage(this.getTexture(Textures.Key.THREE), x, y, null);
			case '4' -> g.drawImage(this.getTexture(Textures.Key.FOUR), x, y, null);
			case '5' -> g.drawImage(this.getTexture(Textures.Key.FIVE), x, y, null);
			case '6' -> g.drawImage(this.getTexture(Textures.Key.SIX), x, y, null);
			case '7' -> g.drawImage(this.getTexture(Textures.Key.SEVEN), x, y, null);
			case '8' -> g.drawImage(this.getTexture(Textures.Key.EIGHT), x, y, null);
			case 'M', 'B' ->
				g.drawImage(this.getTexture(Textures.Key.MINE), x, y, null);
			case 'F', 'X'->
				g.drawImage(this.getTexture(Textures.Key.FLAG), x, y, null);
		}
		
		if (c == 'X') {
			g.drawImage(this.getTexture(Textures.Key.CROSS), x, y, null);
		}
	}
	
	private BufferedImage getTexture(Textures.Key key) {
		return this.textures.getAsset(key);
	}
	
	@Override
	public void onTick() {
		this.componentsManager.onTick();
	}
	
	@Override
	public void dispose() {
		this.textures.dispose();
		super.dispose();
	}
}
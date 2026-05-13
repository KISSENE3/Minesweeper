package com.kissene3.gui.screens;

import com.kissene3.gui.*;
import com.kissene3.gui.components.*;
import com.kissene3.gui.io.AppLogger;
import com.kissene3.gui.io.SettingsRegistry;
import com.kissene3.gui.io.ThemeRegistry;
import com.kissene3.gui.utilities.MyUtilities.Coordinates;
import com.kissene3.gui.utilities.MyUtilities.Dimension;
import com.kissene3.gui.utilities.TextureManager;
import com.kissene3.gui.utilities.Textures;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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
	private static final int cellSize = 64;
	private final Textures textures;
	
	public MenuScreen(@NotNull ScreenManager manager) {
		super(manager);
		this.init();
		int pCell = cellSize / 16;
		int pTitle = 8;
		this.textures = new Textures() {
			@Override
			protected @NotNull Dimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, EXPLODED, CLOSED, REVEALED, MINE, FLAG, CROSS,
						 ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT ->  new Dimension(cellSize, cellSize);
					case TITLE -> new Dimension(121 * pTitle, 16 * pTitle);
					default -> throw new UnsupportedOperationException("This should never happen.");
				};
			}
			
			@Override
			protected void drawMapping(@NotNull Graphics2D g, @NotNull Key key, @NotNull Dimension dimension) {
				switch (key) {
					case BACKGROUND -> this.drawBackground(
							g,
							new Coordinates(0, 0),
							this.getAssetDimension(Key.BACKGROUND),
							MenuScreen.this.manager.getSettings().getTheme().background()
					);
					case EXPLODED -> this.drawBackground(
							g,
							new Coordinates(0, 0),
							this.getAssetDimension(Key.EXPLODED),
							MenuScreen.this.manager.getSettings().getTheme().backgroundExplodedMine()
					);
					case CLOSED -> this.drawClosed(
							g, pCell,
							new Coordinates(0, 0),
							this.getAssetDimension(Key.CLOSED),
							MenuScreen.this.manager.getSettings().getTheme().backgroundLighterShade(),
							MenuScreen.this.manager.getSettings().getTheme().backgroundDarkerShade()
					);
					case REVEALED -> this.drawRevealed(
							g, pCell,
							new Coordinates(0, 0),
							this.getAssetDimension(Key.REVEALED),
							MenuScreen.this.manager.getSettings().getTheme().border()
					);
					case MINE -> this.drawMine(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().mine(),
							MenuScreen.this.manager.getSettings().getTheme().mineShade()
					);
					case FLAG -> this.drawFlag(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().flag(),
							MenuScreen.this.manager.getSettings().getTheme().flagBase()
					);
					case CROSS -> this.drawCross(
							g, pCell,
							new Coordinates(0, 0),
							this.getAssetDimension(Key.CROSS),
							MenuScreen.this.manager.getSettings().getTheme().cross()
					);
					case ONE -> this.drawNumberOne(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberOne()
					);
					case TWO -> this.drawNumberTwo(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberTwo()
					);
					case THREE -> this.drawNumberThree(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberThree()
					);
					case FOUR -> this.drawNumberFour(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberFour()
					);
					case FIVE -> this.drawNumberFive(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberFive()
					);
					case SIX -> this.drawNumberSix(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberSix()
					);
					case SEVEN -> this.drawNumberSeven(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberSeven()
					);
					case EIGHT -> this.drawNumberEight(
							g, pCell,
							new Coordinates(0, 0),
							MenuScreen.this.manager.getSettings().getTheme().numberEight()
					);
					case TITLE -> this.drawTitle(
							g, pTitle,
							new Coordinates(0, 0),
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
		
		this.componentsManager.addComponent(this.getMinesweeperButton((this.getWidth() - 192) / 2, this.getHeight() - 192, "THEME",
			(e) -> {
				if (e.getButton() != MouseEvent.BUTTON1 && e.getButton() != MouseEvent.BUTTON3)
					return;
				
				List<String> options = new ArrayList<>(ThemeRegistry.getAvailableNames());
				Collections.sort(options);
				int index = options.indexOf(this.manager.getSettings().getThemeKey());
			
				if (e.getButton() == MouseEvent.BUTTON1)
					index = (index + 1) % options.size();
				else
					index = (index - 1 + options.size()) % options.size();
				
				String key = options.get(index);
				this.manager.getSettings().setTheme(key);
			}));
		
		this.componentsManager.addComponent(this.getMinesweeperButton((this.getWidth() - 192) / 2, this.getHeight() - 288, "PLAY",
			(e) -> {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				this.manager.pushScreen(new DifficultyScreen(MenuScreen.this.manager));
			}));
		
		AppLogger.info("MenuScreen initialized.");
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
		
		for (int y = 0; y < backgroundLayout.length; y++)
			for (int x = 0; x < backgroundLayout[y].length; x++)
				this.drawCell(g, backgroundLayout[y][x], x * cellSize, y * cellSize);
		
		g.setColor(new Color(
				this.manager.getSettings().getTheme().background().getRed(),
				this.manager.getSettings().getTheme().background().getGreen(),
				this.manager.getSettings().getTheme().background().getBlue(),
				192
		));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int pTitle = 8;
		int startX = (this.getWidth() - Title.getFullWidth(pTitle)) / 2;
		Title.draw(g, pTitle, startX, 150, this.manager.getSettings().getTheme());
		
		this.componentsManager.draw(g);
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
}

class Title {
	
	private Title() {}
	
	public static int getFullWidth(int p) {
		return 119 * p;
	}
	
	public static void draw(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		int offset = Math.max(1, p / 4);
		
		g.setColor(theme.titleOutline());
		
		renderFullText(g, p, x - offset, y);
		renderFullText(g, p, x + offset, y);
		renderFullText(g, p, x, y - offset);
		renderFullText(g, p, x, y + offset);
		renderFullText(g, p, x - offset, y - offset);
		renderFullText(g, p, x - offset, y + offset);
		renderFullText(g, p, x + offset, y - offset);
		renderFullText(g, p, x + offset, y + offset);
		
		
		g.setColor(theme.title());
		renderFullText(g, p, x, y);
	}
	
	private static void renderFullText(@NotNull Graphics2D g, int p, int x, int y) {
		drawM(g, p, x, y); x += 12 * p;
		drawI(g, p, x, y); x += 5 * p;
		drawN(g, p, x, y); x += 12 * p;
		drawE(g, p, x, y); x += 11 * p;
		drawS(g, p, x, y); x += 12 * p;
		drawW(g, p, x, y); x += 12 * p;
		drawE(g, p, x, y); x += 11 * p;
		drawE(g, p, x, y); x += 11 * p;
		drawP(g, p, x, y); x += 12 * p;
		drawE(g, p, x, y); x += 11 * p;
		drawR(g, p, x, y);
	}
	
	/** Size == 10p */
	private static void drawM(@NotNull Graphics2D g, int p, int x, int y) {
		g.fillRect(x, y + 3 * p, 2 * p, 10 * p);				// left column
		g.fillRect(x + 8 * p, y + 3 * p, 2 * p, 10 * p);		// right column
		g.fillRect(x + 2 * p, y + 3 * p, p, 3 * p);					// leftest segment
		g.fillRect(x + 7 * p, y + 3 * p, p, 3 * p);					// rightest segment
		g.fillRect(x + 3 * p, y + 4 * p, p, 3 * p);					// left segment
		g.fillRect(x + 6 * p, y + 4 * p, p, 3 * p);					// right segment
		g.fillRect(x + 4 * p, y + 5 * p, 2 * p, 3 * p);		// middle rectangle
	}
	
	/** Size == 3p */
	private static void drawI(@NotNull Graphics2D g, int p, int x, int y) {
		g.fillRect(x, y + 3 * p, 3 * p, 10 * p);		// I
	}
	
	/** Size == 10p */
	private static void drawN(@NotNull Graphics2D g, int p, int x, int y) {
		g.fillRect(x, y + 4 * p, 2 * p, 9 * p);					// left column
		g.fillRect(x + 4 * p, y + 4 * p, 2 * p, 8 * p);		// middle column
		g.fillRect(x + 8 * p, y + 3 * p, 2 * p, 9 * p);		// right column
		g.fillRect(x + p, y + 3 * p, 4 * p, 3 * p);			// top row
		g.fillRect(x + 5 * p, y + 10 * p, 4 * p, 3 * p);		// bottom row
	}
	
	/** Size == 9p */
	private static void drawE(@NotNull Graphics2D g, int p, int x, int y) {
		g.fillRect(x, y + 3 * p, 2 * p, 10 * p);				// vertical line
		g.fillRect(x + 2 * p, y + 3 * p, 7 * p, 2 * p);		// top row
		g.fillRect(x + 2 * p, y + 7 * p, 4 * p, 2 * p);		// middle row
		g.fillRect(x + 2 * p, y + 11 * p, 7 * p, 2 * p);		// bottom row
	}
	
	/** Size == 10p */
	private static void drawS(@NotNull Graphics2D g, int p, int x, int y) {
		g.fillRect(x + p, y + 3 * p, 9 * p, 2 * p);			// top row
		g.fillRect(x + p, y + 7 * p, 8 * p, 2 * p);			// middle row
		g.fillRect(x, y + 11 * p, 9 * p, 2 * p);				// bottom row
		g.fillRect(x, y + 4 * p, 3 * p, 4 * p);					// left column
		g.fillRect(x + 7 * p, y + 8 * p, 3 * p, 4 * p);		// right column
	}
	
	/** Size == 10p */
	private static void drawW(@NotNull Graphics2D g, int p, int x, int y) {
		g.fillRect(x, y + 3 * p, 2 * p, 9 * p);					// left row
		g.fillRect(x + 4 * p, y + 6 * p, 2 * p, 3 * p);		// middle row
		g.fillRect(x + 8 * p, y + 3 * p, 2 * p, 9 * p);		// right row
		g.fillRect(x + 3 * p, y + 9 * p, 4 * p, 2 * p);		// top column
		g.fillRect(x, y + 10 * p, 4 * p, 2 * p);				// left middle column
		g.fillRect(x + 6 * p, y + 10 * p, 4 * p, 2 * p);		// right middle column
		g.fillRect(x + p, y + 12 * p, 2 * p, p);					// left bottom column
		g.fillRect(x + 7 * p, y + 12 * p, 2 * p, p);				// right bottom column
	}
	
	/** Size == 10p */
	private static void drawP(@NotNull Graphics2D g, int p, int x, int y) {
		g.fillRect(x, y + 3 * p, 3 * p, 10 * p);				// left column
		g.fillRect(x + 7 * p, y + 4 * p, 3 * p, 4 * p);		// right column
		g.fillRect(x + 3 * p, y + 3 * p, 6 * p, 2 * p);		// top row
		g.fillRect(x + 3 * p, y + 7 * p, 6 * p, 2 * p);		// bottom row
	}
	
	/** Size == 10p */
	private static void drawR(@NotNull Graphics2D g, int p, int x, int y) {
		drawP(g, p, x, y);														// P base
		g.fillRect(x + 4 * p, y + 9 * p, 4 * p, p);					// leg's top row
		g.fillRect(x + 5 * p, y + 10 * p, 4 * p, p);				// leg's middle-top row
		g.fillRect(x + 6 * p, y + 11 * p, 4 * p, p);				// leg's middle-bottom row
		g.fillRect(x + 7 * p, y + 12 * p, 3 * p, p);				// leg's bottom row
	}
}
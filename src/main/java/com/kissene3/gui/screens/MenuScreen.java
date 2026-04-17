package com.kissene3.gui.screens;

import com.kissene3.gui.*;
import com.kissene3.gui.components.*;
import com.kissene3.gui.io.SettingsRegistry;
import com.kissene3.gui.io.ThemeRegistry;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	public MenuScreen(ScreenManager manager) {
		super(manager);
		this.init();
	}
	
	private void init() {
		
		this.componentsManager.addComponent(new MyButton((this.getWidth() - 200) / 2, this.getHeight() - 100, 200, 50, "QUIT") {
			@Override
			public void onClick(@NotNull MouseEvent e) {
				SettingsRegistry.save(MenuScreen.this.manager.getSettings());
				System.exit(0);
			}
		});
		this.componentsManager.addComponent(new MyButton((this.getWidth() - 200) / 2, this.getHeight() - 175, 200, 50, MenuScreen.this.manager.getSettings().getThemeKey().toUpperCase()) {
			@Override
			public void onClick(@NotNull MouseEvent e) {
				List<String> options = new ArrayList<>(ThemeRegistry.getAvailableNames());
				Collections.sort(options);
				int index = options.indexOf(MenuScreen.this.manager.getSettings().getThemeKey());
				index++;
				index %= options.size();
				String key = options.get(index);
				MenuScreen.this.manager.getSettings().setTheme(key);
				this.setLabel(key.toUpperCase());
			}
		});
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
				MenuCell.draw(g, x, y, backgroundLayout[y][x], this.manager.getSettings().getTheme());
		
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
}

class MenuCell {

	private MenuCell() {}
	
	public static void draw(@NotNull Graphics2D g, int x, int y, char drawing, Theme theme) {
		final int SIZE = 64;
		int p = SIZE / 16;
		x *= SIZE;
		y *= SIZE;
		
		if (Character.isDigit(drawing) || drawing == 'M') {
			drawCellBackground(g, x, y, SIZE, theme.background());
			drawCellBorder(g, p, x, y, SIZE, theme.border());
		}
		
		switch (drawing) {
			case '0' -> {}
			case '1' -> drawNumberOne(g, p, x, y, theme);
			case '2' -> drawNumberTwo(g, p, x, y, theme);
			case '3' -> drawNumberThree(g, p, x, y, theme);
			case '4' -> drawNumberFour(g, p, x, y, theme);
			case '5' -> drawNumberFive(g, p, x, y, theme);
			case '6' -> drawNumberSix(g, p, x, y, theme);
			case '7' -> drawNumberSeven(g, p, x, y, theme);
			case '8' -> drawNumberEight(g, p, x, y, theme);
			case 'F' -> drawFlag(g, p, x, y, SIZE, false, theme);
			case 'X' -> drawFlag(g, p, x, y, SIZE, true, theme);
			case 'M' -> drawMine(g, p, x, y, theme);
			case 'B' -> {
				drawCellBackground(g, x, y, SIZE, theme.backgroundExplodedMine());
				drawCellBorder(g, p, x, y, SIZE, theme.border());
				drawMine(g, p, x, y, theme);
			}
			default -> drawCellShades(g, p, x, y, SIZE, theme);
		}
	}
	
	private static void drawCellBackground(@NotNull Graphics2D g, int x, int y, int size, @NotNull Color color) {
		g.setColor(color);
		g.fillRect(x, y, size, size);
	}
	
	private static void drawCellShades(@NotNull Graphics2D g, int p, int x, int y, int size, @NotNull Theme theme) {
		drawCellBackground(g, x, y, size, theme.background());
		
		int p2 = 2 * p;
		int reducedSize = size - p;
		int reducedSize2 = size - p2;
		
		g.setColor(theme.backgroundLighterShade());
		g.fillRect(x, y, reducedSize, p);								// top top-line
		g.fillRect(x, y, reducedSize2, p2);								// bottom top-line
		g.fillRect(x, y, p, reducedSize);								// left left-line
		g.fillRect(x, y, p2, reducedSize2);								// right left-line
		
		g.setColor(theme.backgroundDarkerShade());
		g.fillRect(x + reducedSize, y + p, p, reducedSize);		// right right-line
		g.fillRect(x + reducedSize2, y + p2, p, reducedSize2);	// left right-line
		g.fillRect(x + p, y + reducedSize, reducedSize, p);		// bottom bottom-line
		g.fillRect(x + p2, y + reducedSize2, reducedSize2, p);	// top bottom-line
	}
	
	@SuppressWarnings("SameParameterValue")
	private static void drawFlag(@NotNull Graphics2D g, int p, int x, int y, int size, boolean isCrossed, @NotNull Theme theme) {
		drawCellShades(g, p, x, y, size, theme);
		
		g.setColor(theme.flag());
		g.fillRect(x + 7 * p, y + 3 * p, 2 * p, 5 * p);	// right column
		g.fillRect(x + 5 * p, y + 4 * p, 2 * p, 3 * p);	// middle column
		g.fillRect(x + 4 * p, y + 5 * p, p, p);						// left dot
		
		g.setColor(theme.flagBase());
		g.fillRect(x + 8 * p, y + 8 * p, p, 2 * p);				// vertical pole
		g.fillRect(x + 6 * p, y + 10 * p, 4 * p, p);			// top row
		g.fillRect(x + 4 * p, y + 11 * p, 8 * p, 2 * p);	// bottom row
		
		if (isCrossed)
			drawCross(g, p, x, y, size, theme.cross());
	}
	
	private static void drawCross(@NotNull Graphics2D g, int p, int x, int y, int size, @NotNull Color color) {
		g.setStroke(new BasicStroke(p));
		g.setColor(color);
		g.drawLine(x + p, y + p, x + size - p, y + size - p);	// top-left to bottom-right
		g.drawLine(x + size - p, y + p, x + p, y + size - p);	// top-right to bottom-left
		g.setStroke(new BasicStroke(1));
	}
	
	@SuppressWarnings("SameParameterValue")
	private static void drawCellBorder(@NotNull Graphics2D g, int p, int x, int y, int size, @NotNull Color color) {
		g.setColor(color);
		g.fillRect(x, y, size, p);	// top line
		g.fillRect(x, y, p, size);	// left line
	}
	
	private static void drawMine(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {g.setColor(theme.mine());
		g.fillRect(x + 6 * p, y + 4 * p, 5 * p, 9 * p);	// vertical rect
		g.fillRect(x + 4 * p, y + 6 * p, 9 * p, 5 * p);	// horizontal rect
		g.fillRect(x + 5 * p, y + 5 * p, 7 * p, 7 * p);	// centered square
		g.fillRect(x + 8 * p, y + 2 * p, p, 13 * p);			// vertical middle line
		g.fillRect(x + 2 * p, y + 8 * p, 13 * p, p);			// horizontal middle line
		g.fillRect(x + 4 * p, y + 4 * p, p, p);						// top-left dot
		g.fillRect(x + 12 * p, y + 4 * p, p, p);						// top-right dot
		g.fillRect(x + 4 * p, y + 12 * p, p, p);						// bottom-left dot
		g.fillRect(x + 12 * p, y + 12 * p, p, p);						// bottom-right dot
		
		g.setColor(theme.mineShade());
		g.fillRect(x + 6 * p, y + 6 * p, 2 * p, 2 * p);	// light dot
	}
	
	private static void drawNumberOne(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		g.setColor(theme.numberOne());
		g.fillRect(x + 8 * p, y + 3 * p, 2 * p, 8 * p); 	// Right column
		g.fillRect(x + 7 * p, y + 4 * p, p, 7 * p);				// Left column
		g.fillRect(x + 6 * p, y + 5 * p, p, p);						// Top appendix
		g.fillRect(x + 5 * p, y + 6 * p, 2 * p, p);				// Bottom appendix
		g.fillRect(x + 5 * p, y + 11 * p, 7 * p, 2 * p);	// Bottom row
	}
	
	private static void drawNumberTwo(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		g.setColor(theme.numberTwo());
		g.fillRect(x + 3 * p, y + 4 * p, 3 * p, 2 * p);	// top-left appendix
		g.fillRect(x + 4 * p, y + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(x + 10 * p, y + 4 * p, 3 * p, 3 * p);	// right square
		g.fillRect(x + 8 * p, y + 7 * p, 4 * p, p);				// first middle row
		g.fillRect(x + 6 * p, y + 8 * p, 5 * p, p);				// second middle row
		g.fillRect(x + 4 * p, y + 9 * p, 5 * p, p);				// third middle row
		g.fillRect(x + 3 * p, y + 10 * p, 4 * p, 3 * p);	// bottom-left corner
		g.fillRect(x + 7 * p, y + 11 * p, 6 * p, 2 * p);	// bottom row
	}
	private static void drawNumberThree(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		g.setColor(theme.numberThree());
		g.fillRect(x + 3 * p, y + 3 * p, 9 * p, 2 * p);	// top row
		g.fillRect(x + 6 * p, y + 7 * p, 6 * p, 2 * p);	// middle row
		g.fillRect(x + 3 * p, y + 11 * p, 9 * p, 2 * p);	// bottom row
		g.fillRect(x + 10 * p, y + 4 * p, 3 * p, 3 * p);	// top column
		g.fillRect(x + 10 * p, y + 9 * p, 3 * p, 3 * p);	// bottom column
	}
	private static void drawNumberFour(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		g.setColor(theme.numberFour());
		g.fillRect(x + 5 * p, y + 3 * p, 3 * p, 2 * p);	// top diagonal
		g.fillRect(x + 4 * p, y + 5 * p, 3 * p, 2 * p);	// bottom diagonal
		g.fillRect(x + 3 * p, y + 7 * p, 10 * p, 2 * p);	// horizontal line
		g.fillRect(x + 9 * p, y + 3 * p, 3 * p, 10 * p);	// vertical line
	}
	
	private static void drawNumberFive(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		g.setColor(theme.numberFive());
		g.fillRect(x + 3 * p, y + 3 * p, 10 * p, 2 * p);	// top row
		g.fillRect(x + 3 * p, y + 5 * p, 3 * p, 2 * p);	// left column
		g.fillRect(x + 3 * p, y + 7 * p, 9 * p, 2 * p);	// middle row
		g.fillRect(x + 10 * p, y + 8 * p, 3 * p, 4 * p);	// right column
		g.fillRect(x + 3 * p, y + 11 * p, 9 * p, 2 * p);	// bottom row
	}
	
	private static void drawNumberSix(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		g.setColor(theme.numberSix());
		g.fillRect(x + 4 * p, y + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(x + 3 * p, y + 4 * p, 3 * p, 8 * p);	// left column
		g.fillRect(x + 6 * p, y + 7 * p, 6 * p, 2 * p);	// middle row
		g.fillRect(x + 4 * p, y + 11 * p, 8 * p, 2 * p);	// bottom row
		g.fillRect(x + 10 * p, y + 8 * p, 3 * p, 4 * p);	// right column
	}
	
	private static void drawNumberSeven(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		g.setColor(theme.numberSeven());
		g.fillRect(x + 3 * p, y + 3 * p, 10 * p, 2 * p);	// row
		g.fillRect(x + 10 * p, y + 5 * p, 3 * p, 2 * p);	// first diagonal
		g.fillRect(x + 9 * p, y + 7 * p, 3 * p, 2 * p);	// second diagonal
		g.fillRect(x + 8 * p, y + 9 * p, 3 * p, 2 * p);	// third diagonal
		g.fillRect(x + 7 * p, y + 11 * p, 3 * p, 2 * p);	// fourth diagonal
	}
	
	private static void drawNumberEight(@NotNull Graphics2D g, int p, int x, int y, @NotNull Theme theme) {
		g.setColor(theme.numberEight());
		g.fillRect(x + 4 * p, y + 3 * p, 8 * p, 2 * p);	// top row
		g.fillRect(x + 4 * p, y + 7 * p, 8 * p, 2 * p);	// middle row
		g.fillRect(x + 4 * p, y + 11 * p, 8 * p, 2 * p);	// bottom row
		g.fillRect(x + 3 * p, y + 4 * p, 3 * p, 3 * p);	// top-left column
		g.fillRect(x + 10 * p, y + 4 * p, 3 * p, 3 * p);	// top-right column
		g.fillRect(x + 3 * p, y + 9 * p, 3 * p, 3 * p);	// bottom-left column
		g.fillRect(x + 10 * p, y + 9 * p, 3 * p, 3 * p);	// bottom-right column
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
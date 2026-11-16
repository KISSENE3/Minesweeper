package com.kissene3.gui.screens;

import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.components.*;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.util.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ConfirmScreen extends Screen {
	protected final Textures textures;
	protected final Runnable onConfirmAction;
	protected final Runnable onDeclineAction;
	protected final String text;
	protected MyLabel label;
	
	private final int screenWidth;
	private final int screenHeight;
	
	public ConfirmScreen(@NotNull ScreenManager manager, @NotNull Runnable onConfirmAction) {
		this(manager, "Are you sure?", onConfirmAction, null);
	}
	
	
	@SuppressWarnings("unused")
	public ConfirmScreen(@NotNull ScreenManager manager, @NotNull Runnable onConfirmAction, @Nullable Runnable onDeclineAction) {
		this(manager, "Are you sure?", onConfirmAction, onDeclineAction);
	}
	
	@SuppressWarnings("unused")
	public ConfirmScreen(@NotNull ScreenManager manager, @NotNull String text, @NotNull Runnable onConfirmAction) {
		this(manager, text, onConfirmAction, null);
	}
	
	public ConfirmScreen(@NotNull ScreenManager manager, @NotNull String text, @NotNull Runnable onConfirmAction, @Nullable Runnable onDeclineAction) {
		super(manager);
		this.text = text;
		this.onConfirmAction = onConfirmAction;
		this.onDeclineAction = onDeclineAction;
		
		Font defaultFont = new Font("SansSerif", Font.PLAIN, 32);
		
		this.screenWidth = calculateWidth(text, defaultFont);
		this.screenHeight = calculateHeight(text, defaultFont, this.screenWidth - 32);
		
		this.textures = new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, CLOSED ->
							new MyDimension(ConfirmScreen.this.getWidth(), ConfirmScreen.this.getHeight());
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
							ConfirmScreen.this.manager.getSettings().getTheme().background()
					);
					case CLOSED -> this.drawClosed(
							g,
							4,
							new MyCoordinates(0, 0),
							dimension,
							ConfirmScreen.this.manager.getSettings().getTheme().backgroundLighterShade(),
							ConfirmScreen.this.manager.getSettings().getTheme().backgroundDarkerShade()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
		
		TextureManager.addPack(this.textures);
		this.init();
	}
	
	private static int calculateWidth(@NotNull String text, @NotNull Font font) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		
		int textWidth = fm.stringWidth(text);
		g2d.dispose();
		
		// 32px total horizontal padding. Min width 256, Max width 450.
		int desiredWidth = textWidth + 32;
		return Math.max(256, Math.min(450, desiredWidth));
	}
	
	private static int calculateHeight(@NotNull String text, @NotNull Font font, int labelWidth) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		
		String[] words = text.split("\\s+");
		java.util.List<String> lines = new java.util.ArrayList<>();
		StringBuilder currentLine = new StringBuilder();
		
		for (String word : words) {
			String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
			if (fm.stringWidth(testLine) <= labelWidth) {
				currentLine = new StringBuilder(testLine);
			} else {
				if (!currentLine.isEmpty()) {
					lines.add(currentLine.toString());
					currentLine = new StringBuilder(word);
				} else {
					lines.add(word);
					currentLine = new StringBuilder();
				}
			}
		}
		if (!currentLine.isEmpty()) {
			lines.add(currentLine.toString());
		}
		
		int lineHeight = fm.getHeight();
		int totalTextHeight = lines.size() * lineHeight;
		g2d.dispose();
		
		// Layout breakdown: 16 (top) + text height + 16 (spacing) + 48 (button height) + 16 (bottom)
		return Math.max(152, totalTextHeight + 96);
	}
	
	protected void init() {
		int labelHeight = this.getHeight() - 96;
		this.label = new MyLabel(16, 16, this.getWidth() - 32, labelHeight, this.text) {
			@Override
			public @NotNull Color getColorWhenActive() {
				return ConfirmScreen.this.manager.getSettings().getTheme().text();
			}
			
			@Override
			public @NotNull Color getColorWhenInactive() {
				return ConfirmScreen.this.manager.getSettings().getTheme().text();
			}
		};
		this.label.setFont(new Font("SansSerif", Font.PLAIN, 32));
		this.componentsManager.addComponent(this.label);
		
		int buttonY = this.getHeight() - 48 - 16;
		
		this.componentsManager.addComponent(new MinesweeperButton(24, buttonY, 104, 48, "YES") {
			@Override
			public Theme getTheme() {
				return ConfirmScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				ConfirmScreen.this.manager.runAfterDraw(() -> {
					ConfirmScreen.this.manager.popScreen();
					ConfirmScreen.this.onConfirmAction.run();
				});
			}
		});
		
		this.componentsManager.addComponent(new MinesweeperButton(this.getWidth() - 104 - 24, buttonY, 104, 48, "NO") {
			@Override
			public Theme getTheme() {
				return ConfirmScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				ConfirmScreen.this.manager.runAfterDraw(() -> {
					ConfirmScreen.this.manager.popScreen();
					if (ConfirmScreen.this.onDeclineAction != null)
						ConfirmScreen.this.onDeclineAction.run();
				});
			}
		});
	}
	
	@SuppressWarnings("unused")
	public void setFont(@NotNull Font font) {
		this.label.setFont(font);
	}
	
	@Override
	public boolean isTransparent() {
		return true;
	}
	
	@Override
	public int getX() {
		return (this.manager.getCanvas().getWidth() - this.getWidth()) / 2;
	}
	
	@Override
	public int getY() {
		return (this.manager.getCanvas().getHeight() - this.getHeight()) / 2;
	}
	
	@Override
	public int getWidth() {
		return this.screenWidth;
	}
	
	@Override
	public int getHeight() {
		return this.screenHeight;
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		g.drawImage(this.textures.getAsset(Textures.Key.BACKGROUND), 0, 0, null);
		g.drawImage(this.textures.getAsset(Textures.Key.CLOSED), 0, 0, null);
		this.componentsManager.draw(g);
	}
	
	@Override
	public void onTick() {}
	
	@Override
	public void dispose() {
		this.textures.dispose();
		super.dispose();
	}
}

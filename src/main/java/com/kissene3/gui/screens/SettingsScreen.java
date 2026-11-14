package com.kissene3.gui.screens;

import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.components.MinesweeperButton;
import com.kissene3.gui.components.MyLabel;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.util.MyCoordinates;
import com.kissene3.gui.util.MyDimension;
import com.kissene3.gui.util.TextureManager;
import com.kissene3.gui.util.Textures;
import com.kissene3.io.AppLogger;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SettingsScreen extends Screen {
	
	protected final Textures textures;
	protected MyLabel cellSizeLabel;
	protected MyLabel themeLabel;
	
	SettingsScreen(ScreenManager manager) {
		super(manager);
		this.textures = new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, CLOSED ->
							new MyDimension(SettingsScreen.this.getWidth(), SettingsScreen.this.getHeight());
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
							SettingsScreen.this.manager.getSettings().getTheme().background()
					);
					case CLOSED -> this.drawClosed(
							g,
							5,
							new MyCoordinates(0, 0),
							dimension,
							SettingsScreen.this.manager.getSettings().getTheme().backgroundLighterShade(),
							SettingsScreen.this.manager.getSettings().getTheme().backgroundDarkerShade()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
		TextureManager.addPack(this.textures);
		this.init();
	}
	
	protected void init() {
		// Title Label
		MyLabel titleLabel = new MyLabel(24, 20, this.getWidth() - 48, 36, "SETTINGS") {
			@Override
			public @NotNull Color getColorWhenActive() {
				return SettingsScreen.this.manager.getSettings().getTheme().text();
			}
			
			@Override
			public @NotNull Color getColorWhenInactive() {
				return SettingsScreen.this.manager.getSettings().getTheme().text();
			}
		};
		titleLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
		this.componentsManager.addComponent(titleLabel);
		
		// --- Cell Size Setting ---
		MyLabel cellSizeTitle = createSettingLabel(32, 80, "Cell Size:");
		this.componentsManager.addComponent(cellSizeTitle);
		
		this.cellSizeLabel = createSettingValueLabel(240, 80, this.manager.getSettings().getCellSize().name());
		this.componentsManager.addComponent(this.cellSizeLabel);
		
		this.componentsManager.addComponent(new MinesweeperButton(360, 76, 120, 40, "CYCLE") {
			@Override
			public Theme getTheme() {
				return SettingsScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				SettingsScreen.this.manager.getSettings().cycleCellSize();
				SettingsScreen.this.cellSizeLabel.setText(SettingsScreen.this.manager.getSettings().getCellSize().name());
			}
		});
		
		// --- Theme Setting ---
		MyLabel themeTitle = createSettingLabel(32, 140, "Theme:");
		this.componentsManager.addComponent(themeTitle);
		
		this.themeLabel = createSettingValueLabel(240, 140, this.manager.getSettings().getThemeKey().toUpperCase());
		this.componentsManager.addComponent(this.themeLabel);
		
		this.componentsManager.addComponent(new MinesweeperButton(360, 136, 120, 40, "CYCLE") {
			@Override
			public Theme getTheme() {
				return SettingsScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				SettingsScreen.this.manager.getSettings().cycleTheme();
				SettingsScreen.this.themeLabel.setText(SettingsScreen.this.manager.getSettings().getThemeKey().toUpperCase());
			}
		});
		
		// --- Back / Close Button ---
		this.componentsManager.addComponent(new MinesweeperButton((this.getWidth() - 192) / 2, 220, 192, 52, "DONE") {
			@Override
			public Theme getTheme() {
				return SettingsScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				SettingsScreen.this.manager.runAfterDraw(() -> SettingsScreen.this.manager.popScreen());
			}
		});
		
		AppLogger.info(this.getClass().getSimpleName() + " initialized.");
	}
	
	protected MyLabel createSettingLabel(@SuppressWarnings("SameParameterValue") int x, int y, String text) {
		MyLabel label = new MyLabel(x, y, 200, 40, text) {
			@Override
			public @NotNull Color getColorWhenActive() {
				return SettingsScreen.this.manager.getSettings().getTheme().text();
			}
			@Override
			public @NotNull Color getColorWhenInactive() {
				return SettingsScreen.this.manager.getSettings().getTheme().text();
			}
		};
		label.setFont(new Font("Monospaced", Font.BOLD, 16));
		return label;
	}
	
	protected MyLabel createSettingValueLabel(@SuppressWarnings("SameParameterValue") int x, int y, String text) {
		MyLabel label = new MyLabel(x, y, 100, 40, text) {
			@Override
			public @NotNull Color getColorWhenActive() {
				return SettingsScreen.this.manager.getSettings().getTheme().text();
			}
			@Override
			public @NotNull Color getColorWhenInactive() {
				return SettingsScreen.this.manager.getSettings().getTheme().text();
			}
		};
		label.setFont(new Font("SansSerif", Font.PLAIN, 16));
		return label;
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
		return 512;
	}
	
	@Override
	public int getHeight() {
		return 300;
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

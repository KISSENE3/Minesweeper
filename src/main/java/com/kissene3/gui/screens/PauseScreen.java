package com.kissene3.gui.screens;

import com.kissene3.game.Status;
import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.components.*;
import com.kissene3.gui.game.GuiGrid;
import com.kissene3.io.Save;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.util.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;
import java.math.*;

public class PauseScreen extends Screen {
	
	protected final Textures textures;
	protected final GuiGrid grid;
	protected final long thirties;
	
	public PauseScreen(@NotNull ScreenManager manager, @Nullable GuiGrid grid, long thirties) {
		super(manager);
		this.textures = new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, CLOSED ->
							new MyDimension(PauseScreen.this.getWidth(), PauseScreen.this.getHeight());
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
							PauseScreen.this.manager.getSettings().getTheme().background()
					);
					case CLOSED -> this.drawClosed(
							g,
							5,
							new MyCoordinates(0, 0),
							dimension,
							PauseScreen.this.manager.getSettings().getTheme().backgroundLighterShade(),
							PauseScreen.this.manager.getSettings().getTheme().backgroundDarkerShade()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
		TextureManager.addPack(this.textures);
		this.grid = grid;
		this.thirties = thirties;
		this.init();
	}
	
	protected void init() {
		this.componentsManager.addComponent(new MinesweeperButton(this.getWidth() / 2 - 96, 24, 192, 64, "RESUME") {
			@Override
			public Theme getTheme() {
				return PauseScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				PauseScreen.this.manager.runAfterDraw(() -> PauseScreen.this.manager.popScreen());
			}
		});
		
		this.componentsManager.addComponent(new MinesweeperButton(this.getWidth() / 2 - 96, 104, 192, 64, "SETTINGS") {
			@Override
			public Theme getTheme() {
				return PauseScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				PauseScreen.this.manager.runAfterDraw(() -> PauseScreen.this.manager.pushScreen(new SettingsScreen(PauseScreen.this.manager)));
			}
		});
		
		this.componentsManager.addComponent(new MinesweeperButton(this.getWidth() / 2 - 96, 184, 192, 64, "ABANDON") {
			@Override
			public Theme getTheme() {
				return PauseScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				PauseScreen.this.manager.pushScreen(new ConfirmScreen(PauseScreen.this.manager, () -> {
					PauseScreen.this.manager.popAll();
					PauseScreen.this.manager.pushScreen(new MenuScreen(PauseScreen.this.manager));
				}));
			}
		});
		
		this.componentsManager.addComponent(new MinesweeperButton(this.getWidth() / 2 - 96, 264, 192, 64, "SAVE & EXIT") {
			@Override
			public Theme getTheme() {
				return PauseScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1 || PauseScreen.this.grid == null
						|| !PauseScreen.this.grid.isGameStarted() || PauseScreen.this.grid.getGameStatus() != Status.RUNNING)
					return;
				
				BigInteger gameData = Save.convertGameToBigInteger(PauseScreen.this.grid,  PauseScreen.this.thirties);
				Save.saveFile(gameData);
				
				PauseScreen.this.manager.runAfterDraw(() -> {
					PauseScreen.this.manager.popAll();
					PauseScreen.this.manager.pushScreen(new MenuScreen(PauseScreen.this.manager));
				});
			}
		});
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
		return 256;
	}
	
	@Override
	public int getHeight() {
		return 352;
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		g.drawImage(this.textures.getAsset(Textures.Key.BACKGROUND), 0, 0, null);
		g.drawImage(this.textures.getAsset(Textures.Key.CLOSED), 0, 0, null);
		this.componentsManager.draw(g);
	}
	
	@Override
	public void onTick() {
	
	}
	
	@Override
	public void dispose() {
		this.textures.dispose();
		super.dispose();
	}
}

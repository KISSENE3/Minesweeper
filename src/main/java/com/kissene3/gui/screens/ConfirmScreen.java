package com.kissene3.gui.screens;

import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.components.*;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.util.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;

public class ConfirmScreen extends Screen {
	protected final Textures textures;
	protected final Runnable onConfirmAction;
	protected final Runnable onDeclineAction;
	protected final String text;
	
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
	
	protected void init() {
		MyLabel label = new MyLabel(16, 16, this.getWidth() - 32, 56, this.text) {
			@Override
			public @NotNull Color getColorWhenActive() {
				return ConfirmScreen.this.manager.getSettings().getTheme().text();
			}
			
			@Override
			public @NotNull Color getColorWhenInactive() {
				return ConfirmScreen.this.manager.getSettings().getTheme().text();
			}
		};
		label.setFont(new Font("SansSerif", Font.PLAIN, 32));
		this.componentsManager.addComponent(label);
		
		this.componentsManager.addComponent(new MinesweeperButton(24, 80, 104, 48, "YES") {
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
		
		this.componentsManager.addComponent(new MinesweeperButton(this.getWidth() - 104 - 24, 80, 104, 48, "NO") {
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
		return 152;
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

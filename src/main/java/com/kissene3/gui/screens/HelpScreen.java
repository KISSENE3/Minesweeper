package com.kissene3.gui.screens;

import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.components.MinesweeperButton;
import com.kissene3.gui.components.MyLabel;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.util.MyCoordinates;
import com.kissene3.gui.util.MyDimension;
import com.kissene3.gui.util.TextureManager;
import com.kissene3.gui.util.Textures;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;

public class HelpScreen extends Screen {
	
	protected final Textures textures;
	
	public HelpScreen(ScreenManager manager) {
		super(manager);
		this.textures = new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, CLOSED ->
							new MyDimension(HelpScreen.this.getWidth(), HelpScreen.this.getHeight());
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
							HelpScreen.this.manager.getSettings().getTheme().background()
					);
					case CLOSED -> this.drawClosed(
							g,
							5,
							new MyCoordinates(0, 0),
							dimension,
							HelpScreen.this.manager.getSettings().getTheme().backgroundLighterShade(),
							HelpScreen.this.manager.getSettings().getTheme().backgroundDarkerShade()
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
		MyLabel titleLabel = new MyLabel(24, 20, this.getWidth() - 48, 36, "HOW TO PLAY") {
			@Override
			public @NotNull Color getColorWhenActive() {
				return HelpScreen.this.manager.getSettings().getTheme().text();
			}
			
			@Override
			public @NotNull Color getColorWhenInactive() {
				return HelpScreen.this.manager.getSettings().getTheme().text();
			}
		};
		titleLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
		this.componentsManager.addComponent(titleLabel);
		
		// Rules text / breakdown component or custom drawing/labels can go here.
		// Let's add a close/back button at the bottom center
		this.componentsManager.addComponent(new MinesweeperButton((this.getWidth() - 192) / 2, this.getHeight() - 76, 192, 52, "GOT IT") {
			@Override
			public Theme getTheme() {
				return HelpScreen.this.manager.getSettings().getTheme();
			}
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				HelpScreen.this.manager.runAfterDraw(() -> HelpScreen.this.manager.popScreen());
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
		return 480;
	}
	
	@Override
	public int getHeight() {
		return 420;
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		// Draw background and beveled border frame
		g.drawImage(this.textures.getAsset(Textures.Key.BACKGROUND), 0, 0, null);
		g.drawImage(this.textures.getAsset(Textures.Key.CLOSED), 0, 0, null);
		
		// Draw descriptive rule lines cleanly
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
		g2d.setColor(this.manager.getSettings().getTheme().text());
		
		int startX = 32;
		int startY = 75;
		int lineSpacing = 22;
		
		String[] rules = {
				"• Goal: Uncover all safe cells without",
				"  hitting a hidden mine.",
				"",
				"• Left-Click: Reveal a cell.",
				"  - Numbers indicate how many mines",
				"    touch that specific cell directly",
				"    in the eight adjacent cells.",
				"",
				"• Right-Click: Place or remove a flag",
				"  to mark suspected mine locations.",
				"",
				"• Winning: Clear every non-mine cell",
				"  to claim victory!"
		};
		
		for (int i = 0; i < rules.length; i++) {
			g2d.drawString(rules[i], startX, startY + (i * lineSpacing));
		}
		
		g2d.dispose();
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

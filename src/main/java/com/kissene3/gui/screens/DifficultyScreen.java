package com.kissene3.gui.screens;

import com.kissene3.game.*;
import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.models.Theme;
import com.kissene3.gui.components.*;
import com.kissene3.gui.util.*;
import com.kissene3.io.AppLogger;
import com.kissene3.io.DifficultyRegistry;
import com.kissene3.io.Save;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.*;

public class DifficultyScreen extends Screen {
	
	protected final MyTextBox[] textBoxes = new MyTextBox[3];
	protected final Textures textures;
	protected boolean isCustom = false;
	protected String difficulty;
	
	public DifficultyScreen(@NotNull ScreenManager manager) {
		super(manager);
		this.textures = new Textures() {
			@Override
			protected @NotNull MyDimension getAssetDimension(@NotNull Key key) {
				return switch (key) {
					case BACKGROUND, CLOSED ->
						new MyDimension(DifficultyScreen.this.getWidth(), DifficultyScreen.this.getHeight());
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
							DifficultyScreen.this.manager.getSettings().getTheme().background()
					);
					case CLOSED -> this.drawClosed(
							g,
							DifficultyScreen.this.getP(),
							new MyCoordinates(0, 0),
							dimension,
							DifficultyScreen.this.manager.getSettings().getTheme().backgroundLighterShade(),
							DifficultyScreen.this.manager.getSettings().getTheme().backgroundDarkerShade()
					);
					default -> throw new UnsupportedOperationException("This should never happen.");
				}
			}
		};
		TextureManager.addPack(this.textures);
		this.init();
	}
	
	public void init() {
		this.componentsManager.addComponent(this.getMinesweeperButton(16, 16, "BACK",
			(e) -> {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				DifficultyScreen.this.manager.runAfterDraw(() -> DifficultyScreen.this.manager.popScreen());
			}));
		
		this.componentsManager.addComponent(this.getMinesweeperButton((this.getWidth() - 192) / 2, this.getHeight() - 80, "START",
			(e) -> {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				
				if (this.isCustom) {
					try {
						Difficulty d = new Difficulty(
								Integer.parseInt(this.textBoxes[0].getText()),
								Integer.parseInt(this.textBoxes[1].getText()),
								Integer.parseInt(this.textBoxes[2].getText())
						);
						this.manager.getSettings().setDifficulty("custom", d);
					} catch (Exception _) {
						return;
					}
				} else {
					this.manager.getSettings().setDifficulty(this.difficulty, null);
				}
				
				Save.deleteSaveFile();
				
				DifficultyScreen.this.manager.runAfterDraw(() -> {
					DifficultyScreen.this.manager.popAll();
					DifficultyScreen.this.manager.pushScreen(new GameScreen(DifficultyScreen.this.manager, this.manager.getSettings().getCurrentDifficulty()));
				});
			}));
		
		this.textBoxes[0] = this.getMyTextbox(128);
		this.textBoxes[1] = this.getMyTextbox(228);
		this.textBoxes[2] = this.getMyTextbox(328);
		
		for (MyTextBox box : this.textBoxes) {
			this.componentsManager.addComponent(box);
		}
		
		this.componentsManager.addComponent(this.getMyLabel(92, "Width:"));
		this.componentsManager.addComponent(this.getMyLabel(192, "Height:"));
		this.componentsManager.addComponent(this.getMyLabel(292, "Mines:"));
		
		this.componentsManager.addComponent(getMyScrollList());
		
		AppLogger.info(this.getClass().getSimpleName() + " initialized.");
	}
	
	protected @NotNull MinesweeperButton getMinesweeperButton(int x, int y, String label, Consumer<MouseEvent> actionOnClick) {
		return new MinesweeperButton(x, y, 192, 64, label) {
			
			@Override
			public void onClick(@NotNull MouseEvent e) {
				actionOnClick.accept(e);
			}
			
			@Override
			public Theme getTheme() {
				return DifficultyScreen.this.manager.getSettings().getTheme();
			}
		};
	}
	
	protected @NotNull MyTextBox getMyTextbox(int y) {
		MyTextBox textBox = new MyTextBox(this.getWidth() - 224, y, 192, 48) {
			@Override
			protected boolean isValidChar(char c) {
				return Character.isDigit(c);
			}
		};
		textBox.setText("");
		textBox.setPlaceholder("0");
		textBox.setFont(new Font("Monospaced", Font.BOLD, (int)(textBox.getHeight() * 0.6)));
		textBox.setActive(false);
		return textBox;
	}
	
	protected @NotNull MyLabel getMyLabel(int y, String text) {
		MyLabel label = new MyLabel(new MyRectangle(this.getWidth() - 224, y, 128, 32), text) {
			@Override
			public @NotNull Color getColorWhenActive() {
				return DifficultyScreen.this.manager.getSettings().getTheme().text();
			}
			
			@Override
			public @NotNull Color getColorWhenInactive() {
				return DifficultyScreen.this.manager.getSettings().getTheme().text();
			}
		};
		
		label.setFont(new Font("Monospaced", Font.BOLD, (int)(label.getHeight() * 0.9)));
		
		return label;
	}
	
	protected @NotNull MyScrollList getMyScrollList() {
		MyScrollList scrollList = new MyScrollList(32, 128, 192, 256, 32) {
			@Override
			public void onSelectionChanged(int newIndex) {
				if (newIndex == this.getSize() - 1)
				{
					DifficultyScreen.this.setupCustomMode();
					return;
				}
				
				String selected = getItems().get(newIndex).getText();
				if (selected == null)
					return;
				
				DifficultyScreen.this.updateFields(selected);
			}
		};
		scrollList.setParentScreen(this);
		
		List<String> names = DifficultyRegistry.getAvailableNamesSorted();
		
		for (String difficulty : names) {
			difficulty = difficulty.substring(0, 1).toUpperCase() + difficulty.substring(1);
			scrollList.addItem(difficulty);
		}
		
		scrollList.addItem("Custom");
		
		scrollList.select(this.manager.getSettings().getDifficultyName());
		
		return scrollList;
	}
	
	protected void setupCustomMode() {
		this.difficulty = "custom";
		this.isCustom = true;
		
		Difficulty d = this.manager.getSettings().getCurrentDifficulty();
		
		this.textBoxes[0].setText(String.valueOf(d.width()));
		this.textBoxes[1].setText(String.valueOf(d.height()));
		this.textBoxes[2].setText(String.valueOf(d.numMines()));
		
		for (MyTextBox box : this.textBoxes) {
			box.setActive(true);
		}
		
		this.textBoxes[0].setCursorIndex(this.textBoxes[0].getText().length());
		this.componentsManager.handleFocusChange(this.textBoxes[0]);
	}
	
	protected void updateFields(@NotNull String difficultyKey) {
		this.difficulty = difficultyKey;
		this.isCustom = false;
		
		Difficulty d = DifficultyRegistry.get(difficultyKey);
		
		this.textBoxes[0].setText(String.valueOf(d.width()));
		this.textBoxes[1].setText(String.valueOf(d.height()));
		this.textBoxes[2].setText(String.valueOf(d.numMines()));
		
		for (MyTextBox box : this.textBoxes) {
			box.setActive(false);
		}
	}
	
	protected int getP() {
		return 5;
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
		return 512;
	}
	
	@Override
	public void onTick() {
		this.componentsManager.onTick();
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		g.drawImage(this.textures.getAsset(Textures.Key.BACKGROUND), 0, 0, null);
		g.drawImage(this.textures.getAsset(Textures.Key.CLOSED), 0, 0, null);
		this.componentsManager.draw(g);
	}
	
	@Override
	public void dispose() {
		this.textures.dispose();
		super.dispose();
	}
}

package com.kissene3.gui;

import com.kissene3.game.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class Settings implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String cellSizeKey;
	private transient CellSize cellSize;
	
	private String themeKey;
	private transient Theme theme;
	
	private String lastDifficultyName;
	private transient Difficulties lastDifficulty;
	
	
	public Settings() {
		this.cellSizeKey = "MEDIUM";
		this.cellSize = CellSize.MEDIUM;
		
		this.themeKey = "Light";
		this.theme = Theme.LIGHT_MODE;
		
		this.lastDifficultyName = "Easy";
		this.lastDifficulty = null;
	}
	
	public void setCellSize(@NotNull CellSize size) {
		this.cellSize = size;
		this.cellSizeKey = size.name();
	}
	public @NotNull CellSize getCellSize() {
		if (this.cellSize == null) {
			try {
				this.cellSize = CellSize.valueOf(this.cellSizeKey);
			} catch (IllegalArgumentException e) {
				this.cellSize = CellSize.MEDIUM;
			}
		}
		return this.cellSize;
	}
	
	public void cycleCellSize() {
		CellSize current = getCellSize();
		CellSize next = switch (current) {
			case SMALL -> CellSize.MEDIUM;
			case MEDIUM -> CellSize.BIG;
			case BIG -> CellSize.SMALL;
		};
		this.setCellSize(next);
	}
	
	public void setTheme(@NotNull String key) {
		this.themeKey = key;
		this.theme = Theme.getTheme(key);
	}
	public @NotNull Theme getTheme() {
		if (this.theme == null) {
			this.theme = Theme.getTheme(this.themeKey);
		}
		return this.theme;
	}
	public @NotNull String getThemeKey() {
		return this.themeKey;
	}
	public boolean isTheme(@NotNull String key) {
		return this.themeKey.equals(key);
	}
	
	public void setDifficulty(@NotNull String name, @Nullable Difficulties customIfAny) {
		this.lastDifficultyName = name;
		if (name.equals("Custom") && customIfAny != null) {
			this.lastDifficulty = customIfAny;
		}
	}
	public @NotNull Difficulties getCurrentDifficulty() {
		return Difficulties.fromName(this.lastDifficultyName, this.lastDifficulty);
	}
	public String getDifficultyName() {
		return this.lastDifficultyName;
	}
	
	@Serial
	private void writeObject(@NotNull ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		
		if ("Custom".equalsIgnoreCase(this.lastDifficultyName) && this.lastDifficulty != null) {
			oos.writeInt(this.lastDifficulty.width());
			oos.writeInt(this.lastDifficulty.height());
			oos.writeInt(this.lastDifficulty.numMines());
		}
	}
	
	@Serial
	private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		this.theme = Theme.getTheme(this.themeKey);
		
		if ("Custom".equalsIgnoreCase(this.lastDifficultyName)) {
			int w = ois.readInt();
			int h = ois.readInt();
			int m = ois.readInt();
			this.lastDifficulty = new Difficulties(w, h, m);
		}
	}
}

package com.kissene3.gui;

import com.google.gson.annotations.SerializedName;
import com.kissene3.game.*;
import com.kissene3.gui.io.ThemeRegistry;
import com.kissene3.gui.utilities.TextureManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class Settings {
	/* ---------- ATTRIBUTES ---------- */
	
	@SerializedName("cell_size") protected String cellSizeKey;
	@SerializedName("theme") protected String themeKey;
	@SerializedName("difficulty") protected String lastDifficultyName;
	@SerializedName("custom_difficulty") protected Difficulty customDifficultyData;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- TRANSIENT ATTRIBUTES ---------- */
	
	protected transient Theme theme;
	protected transient CellSize cellSize;
	
	/* ---------- TRANSIENT ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public Settings() {
		this.setCellSize("MEDIUM");
		
		this.themeKey = "light";
		this.theme = null;
		
		this.lastDifficultyName = "easy";
		this.customDifficultyData = null;
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	public void setCellSize(@NotNull String key) {
		try {
			this.cellSize = CellSize.valueOf(key);
			this.cellSizeKey = key;
		} catch (IllegalArgumentException e) {
			this.cellSizeKey = "MEDIUM";
			this.cellSize = CellSize.MEDIUM;
		}
	}
	
	protected void setCellSize(@NotNull CellSize cellSize) {
		this.cellSize = cellSize;
		this.cellSizeKey = cellSize.name();
		
		TextureManager.invalidateAll();
	}
	
	public @NotNull CellSize getCellSize() {
		if (this.cellSize == null) {
			this.setCellSize(this.cellSizeKey != null ? this.cellSizeKey : "MEDIUM");
		}
		return this.cellSize;
	}
	
	public void setTheme(@NotNull String key) {
		this.themeKey = key.toLowerCase();
		this.theme = null;
		
		TextureManager.invalidateAll();
	}
	
	public @NotNull Theme getTheme() {
		if (this.theme == null) {
			this.theme = ThemeRegistry.get(this.themeKey != null ? this.themeKey : "light");
		}
		return this.theme;
	}
	
	public @NotNull String getThemeKey() {
		return this.themeKey != null ? this.themeKey : "light";
	}
	
	/**
	 * Gets the current difficulty.<br />
	 * If is {@code custom}, returns the data saved locally on the settings.<br />
	 * Otherwise, interrogates the registry to get default difficulties or the ones loaded from the JSON.
	 */
	public @NotNull Difficulty getCurrentDifficulty() {
		if ("custom".equalsIgnoreCase(this.lastDifficultyName)) {
			return (this.customDifficultyData != null)
					? this.customDifficultyData
					: DifficultyRegistry.get("easy");
		}
		
		return DifficultyRegistry.get(this.lastDifficultyName);
	}
	
	/**
	 * Sets the chosen difficulty.<br />
	 * If it's custom, we save the Difficulty object in the JSON.
	 */
	public void setDifficulty(@NotNull String name, @Nullable Difficulty customIfAny) {
		this.lastDifficultyName = name.toLowerCase();
		
		if ("custom".equalsIgnoreCase(name) && customIfAny != null) {
			this.customDifficultyData = customIfAny;
		} else {
			this.customDifficultyData = null;
		}
	}
	
	public String getDifficultyName() {
		return capitalize(this.lastDifficultyName);
	}
	
	public boolean isCustomDifficulty() {
		return "custom".equalsIgnoreCase(this.lastDifficultyName);
	}
	
	/* ---------- GETTERS/SETTERS ---------- */
	/* ---------- UTILITIES ---------- */
	
	public void cycleCellSize() {
		CellSize current = this.getCellSize();
		CellSize next = switch (current) {
			case SMALL -> CellSize.MEDIUM;
			case MEDIUM -> CellSize.BIG;
			case BIG -> CellSize.SMALL;
		};
		this.setCellSize(next);
	}
	
	public boolean isTheme(@NotNull String key) {
		return getThemeKey().equalsIgnoreCase(key);
	}
	
	@Contract(pure = true)
	protected @NotNull String capitalize(@Nullable String s) {
		if (s == null || s.isEmpty())
			return "Easy";
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	/* ---------- UTILITIES ---------- */
}

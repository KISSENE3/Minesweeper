package com.kissene3.gui.io;

import com.google.gson.*;
import com.kissene3.gui.Settings;
import com.kissene3.main.Main;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.*;

public class SettingsRegistry {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	protected static final String FILE_PATH = Main.BASE_PATH + "saves" + File.separator + "settings.json";
	protected static final String TEMP_PATH = FILE_PATH + ".tmp";
	protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- I/O METHODS ---------- */
	
	/**
	 * Only saves the differences from the default.<br />
	 * If there are no differences deletes the file.
	 */
	public static void save(Settings settings) {
		JsonObject delta = getDelta(settings);
		
		File finalFile = new File(FILE_PATH);
		
		if (delta.isEmpty()) {
			if (finalFile.exists())
				//noinspection ResultOfMethodCallIgnored
				finalFile.delete();
			return;
		}
		
		File tempFile = new File(TEMP_PATH);
		if (finalFile.getParentFile() != null)
			//noinspection ResultOfMethodCallIgnored
			finalFile.getParentFile().mkdirs();
		
		try (Writer writer = new FileWriter(tempFile)) {
			GSON.toJson(delta, writer);
			writer.flush();
		} catch (IOException e) {
			AppLogger.severe("Error writing temp file: " + e.getMessage());
			return;
		}
		
		try {
			Files.move(tempFile.toPath(), finalFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			try {
				Files.move(tempFile.toPath(), finalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				AppLogger.severe("Error moving temp file to final location: " + ex.getMessage());
			}
		}
		
		AppLogger.info("Settings saved.");
	}
	
	/**
	 * Loads the settings.<br />
	 * If the file doesn't exist or is empty {}, default values are given.
	 */
	public static @NotNull Settings load() {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			AppLogger.info("Settings file not found, using default.");
			return new Settings();
		}
		
		try (Reader reader = new FileReader(file)) {
			Settings loaded = GSON.fromJson(reader, Settings.class);
			if (loaded != null) {
				AppLogger.info("Settings loaded.");
				return loaded;
			} else {
				AppLogger.warning("Failed to load settings, using default.");
				return new Settings();
			}
			
		} catch (Exception e) {
			AppLogger.warning("Error loading settings (using default): " + e.getMessage());
			return new Settings();
		}
	}
	
	/* ---------- I/O METHODS ---------- */
	/* ---------- UTILITIES ---------- */
	
	/**
	 * Compares entered settings with the default ones and returns
	 * a {@code JsonObject} containing the differences.
	 */
	protected static @NotNull JsonObject getDelta(@NotNull Settings current) {
		Settings def = new Settings();
		JsonObject json = new JsonObject();
		
		// Theme
		if (!current.getThemeKey().equals(def.getThemeKey())) {
			json.addProperty("theme", current.getThemeKey());
		}
		
		// CellBrutta Size
		if (current.getCellSize() != def.getCellSize()) {
			json.addProperty("cell_size", current.getCellSize().name());
		}
		
		// Difficulty Name
		if (!current.getDifficultyName().equalsIgnoreCase(def.getDifficultyName())) {
			json.addProperty("difficulty", current.getDifficultyName());
		}
		
		// Custom Difficulty
		current.getCurrentDifficulty();
		if ("custom".equalsIgnoreCase(current.getDifficultyName())) {
			json.add("custom_difficulty", GSON.toJsonTree(current.getCurrentDifficulty()));
		}
		
		return json;
	}
	
	/* ---------- UTILITIES ---------- */
}
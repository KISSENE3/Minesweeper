package com.kissene3.gui.io;

import com.google.gson.*;
import com.kissene3.gui.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.*;

public class SettingsRegistry {
	private static final String FILE_PATH = "data/saves/settings.json";
	private static final String TEMP_PATH = FILE_PATH + ".tmp";
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
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
			System.err.println("Error writing temp: " + e.getMessage());
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
				System.err.println("Error moving: " + ex.getMessage());
			}
		}
	}
	
	/**
	 * Loads the settings. If the file doesn't exist or is empty {},
	 * default values are given.
	 */
	public static @NotNull Settings load() {
		File file = new File(FILE_PATH);
		if (!file.exists())
			return new Settings();
		
		try (Reader reader = new FileReader(file)) {
			Settings loaded = GSON.fromJson(reader, Settings.class);
			return
				(loaded != null) ? loaded : new Settings();
		} catch (Exception e) {
			System.err.println("Error loading settings (using default): " + e.getMessage());
			return new Settings();
		}
	}
	
	/**
	 * Compares entered settings with the default ones and returns
	 * a {@code JsonObject} containing the differences.
	 */
	private static @NotNull JsonObject getDelta(@NotNull Settings current) {
		Settings def = new Settings();
		JsonObject json = new JsonObject();
		
		// Theme
		if (!current.getThemeKey().equals(def.getThemeKey())) {
			json.addProperty("theme", current.getThemeKey());
		}
		
		// Cell Size
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
}
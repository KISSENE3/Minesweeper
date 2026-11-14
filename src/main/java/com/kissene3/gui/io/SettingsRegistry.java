package com.kissene3.gui.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kissene3.gui.models.Settings;
import com.kissene3.io.AppLogger;
import com.kissene3.main.Main;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class SettingsRegistry {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	protected static final Path FILE_PATH = Main.BASE_PATH.resolve("saves", "settings.json");
	protected static final Path TEMP_PATH = Main.BASE_PATH.resolve("saves", "settings.json.tmp");
	protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- I/O METHODS ---------- */
	
	/**
	 * - Only saves the differences from the default.
	 * <br />
	 * - If there are no differences deletes the file.
	 */
	public static void save(Settings settings) {
		JsonObject delta = getDelta(settings);
		
		if (delta.isEmpty()) {
			try {
				if (Files.exists(FILE_PATH))
					Files.delete(FILE_PATH);
			} catch (IOException e) {
				AppLogger.severe("Error deleting empty settings file: " + e.getMessage());
			}
			return;
		}
		
		try {
			if(FILE_PATH.getParent() != null)
				Files.createDirectories(FILE_PATH.getParent());
		} catch (IOException e) {
			AppLogger.severe("Error creating directories for settings: " + e.getMessage());
			return;
		}
		
		try (Writer writer = Files.newBufferedWriter(TEMP_PATH, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			GSON.toJson(delta, writer);
		} catch (IOException e) {
			AppLogger.severe("Error writing temp file: " + e.getMessage());
			return;
		}
		
		try {
			Files.move(TEMP_PATH, FILE_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException e) {
			AppLogger.severe("Error moving temp file to final location: " + e.getMessage());
		}
		
		AppLogger.info("Settings saved.");
	}
	
	/**
	 * - Loads the settings.
	 * <br />
	 * - If the file doesn't exist or is empty {@code {}}, default values are given.
	 */
	public static @NotNull Settings load() {
		if (!Files.exists(FILE_PATH)) {
			AppLogger.info("Settings file not found, using default.");
			return new Settings();
		}
		
		try (Reader reader = Files.newBufferedReader(FILE_PATH, StandardCharsets.UTF_8)) {
			Settings loaded = GSON.fromJson(reader, Settings.class);
			if (loaded != null) {
				AppLogger.info("Settings loaded.");
				return loaded;
			} else {
				AppLogger.warning("Failed to load settings, using default.");
				return new Settings();
			}
			
		} catch (IOException e) {
			AppLogger.warning("Error loading settings (using default): " + e.getMessage());
			return new Settings();
		}
	}
	
	/* ---------- I/O METHODS ---------- */
	/* ---------- UTILITIES ---------- */
	
	/**
	 * -Compares entered settings with the default ones and returns
	 * a {@code JsonObject} containing the differences.
	 */
	protected static @NotNull JsonObject getDelta(@NotNull Settings current) {
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
	
	/* ---------- UTILITIES ---------- */
}
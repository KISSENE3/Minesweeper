package com.kissene3.game;

import com.google.gson.Gson;
import com.kissene3.gui.io.AppLogger;
import com.kissene3.main.Main;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class DifficultyRegistry {
	private static final Gson GSON = new Gson();
	
	/**
	 * Map that associates difficulty name with file path.<br />
	 * {@code true} is internal (from Resources) while {@code false} is external.
	 */
	private static final Map<String, Boolean> CACHE = new HashMap<>();
	
	// Constants
	private static final String EXTERNAL_DIR = Main.BASE_PATH + "difficulties" + File.separator;
	private static final String INTERNAL_DIR = "/difficulties/";
	private static final String[] DEFAULTS = {"easy", "medium", "hard"};
	
	static {
		init();
	}
	
	/**
	 * Scans every file and populate the {@code Map}.
	 * To be called at the start of the application.
	 */
	private static void init() {
		CACHE.clear();
		
		// 1. Loads the defaults from Resources.
		for (String name : DEFAULTS) {
			Difficulty d = loadFromResources(name);
			if (d == null) {
				AppLogger.warning("Failed to load difficulty from resources: " + name);
				continue;
			}
			CACHE.put(name.toLowerCase(), true);
			AppLogger.info("Loaded difficulty from resources: " + name);
		}
		
		// 2. Override or add from external files.
		File dir = new File(EXTERNAL_DIR);
		if (!dir.exists() || !dir.isDirectory())
			return;
		
		File[] files = dir.listFiles((_, name) -> name.endsWith(".json"));
		if (files == null)
			return;
		
		for (File file : files) {
			String name = file.getName().replace(".json", "").toLowerCase();
			Difficulty custom = loadFromFile(file);
			
			// VALIDATION: Adds to the Map only if it's correct.
			if (isValid(custom)) {
				CACHE.put(name, false);
				AppLogger.info("Loaded difficulty: " + name);
			} else {
				AppLogger.warning("Failed to load difficulty: " + name);
			}
		}
	}
	
	public static @NotNull Difficulty get(@NotNull String name) {
		String key = CACHE.containsKey(name.toLowerCase()) ? name.toLowerCase() : "easy";
		Boolean isInternal = CACHE.getOrDefault(key, true);
		
		Difficulty d = isInternal ? loadFromResources(key) : loadFromFile(new File(EXTERNAL_DIR + key + ".json"));
		
		return Objects.requireNonNullElseGet(d, () -> new Difficulty(9, 9, 10));
	}
	
	public static @NotNull Set<String> getAvailableNames() {
		return CACHE.keySet();
	}
	
	public static @NotNull @Unmodifiable List<String> getAvailableNamesSorted() {
		return CACHE.keySet().stream().sorted().toList();
	}
	
	/* \/ \/ Private Helper Methods \/ \/ */
	
	private static boolean isValid(@Nullable Difficulty d) {
		return d != null && d.width() * d.height() - 9 > 0;
	}
	
	private static @Nullable Difficulty loadFromFile(@NotNull File file) {
		try (Reader r = new FileReader(file)) {
			return GSON.fromJson(r, Difficulty.class);
		} catch (Exception e) {
			AppLogger.warning("Failed to load difficulty: " + file.getName() + ".\n" + e.getMessage());
			return null;
		}
	}
	
	private static @Nullable Difficulty loadFromResources(String name) {
		try (InputStream is = DifficultyRegistry.class.getResourceAsStream(INTERNAL_DIR + name + ".json")) {
			if (is == null) {
				AppLogger.warning("Failed to load difficulty from resources: " + name + ".json");
				return null;
			}
			return GSON.fromJson(new InputStreamReader(is), Difficulty.class);
		} catch (Exception e) {
			AppLogger.warning("Failed to load difficulty from resources: " + name + ".json.\n" + e.getMessage());
			return null;
		}
	}
}
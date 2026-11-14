package com.kissene3.io;

import com.google.gson.Gson;
import com.kissene3.game.Difficulty;
import com.kissene3.main.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class DifficultyRegistry {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	protected static final Gson GSON = new Gson();
	
 	/**
	 * - Map that associates difficulty name with file path.
	 * <br />
	 * - {@code true} is internal (from Resources) while {@code false} is external.
	 */
	protected static final Map<String, Boolean> CACHE = new HashMap<>();
	protected static final Path EXTERNAL_DIR = Main.BASE_PATH.resolve("difficulties");
	protected static final String INTERNAL_DIR = "/difficulties/";
	protected static final String[] DEFAULTS = {"easy", "medium", "hard"};
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	static {
		init();
	}
	
	/**
	 * - Scans every file and populate the {@code Map}.
	 */
	protected static void init() {
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
		
		// 2. Overrides or adds from external files.
		if (!Files.isDirectory(EXTERNAL_DIR))
			return;
		
		try (Stream<Path> stream = Files.list(EXTERNAL_DIR)) {
			stream.filter(Files::isRegularFile)
				  .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
				  .forEach(file -> {
					  String fileName = file.getFileName().toString();
					  String name = fileName.substring(0, fileName.length() - 5).toLowerCase();
					  Difficulty custom = loadFromFile(file);
					  
					  if (isValid(custom)) {
						  CACHE.put(name, false);
						  AppLogger.info("Loaded difficulty: " + name);
					  } else
						  AppLogger.warning("Failed to load difficulty: " + name);
				  });
		} catch (IOException e) {
			AppLogger.warning("Failed to scan external difficulties directory: " + e.getMessage());
		}
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS ---------- */
	
	public static @NotNull Difficulty get(@NotNull String name) {
		String key = CACHE.containsKey(name.toLowerCase()) ? name.toLowerCase() : "easy";
		Boolean isInternal = CACHE.getOrDefault(key, true);
		
		Difficulty d = isInternal ? loadFromResources(key) : loadFromFile(EXTERNAL_DIR.resolve(key + ".json"));
		
		if (!isValid(d)) {
			return new Difficulty(9, 9, 10);
		}
		
		return d;
	}
	
	public static @NotNull Set<String> getAvailableNames() {
		return CACHE.keySet();
	}
	
	public static @NotNull @Unmodifiable List<String> getAvailableNamesSorted() {
		return getAvailableNames().stream().sorted().toList();
	}

	/* ---------- GETTERS ---------- */
	/* ---------- HELPER METHODS ---------- */
	
	protected static boolean isValid(@Nullable Difficulty d) {
		return d != null;
	}
	
	protected static @Nullable Difficulty loadFromFile(@NotNull Path file) {
		try (Reader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			return GSON.fromJson(r, Difficulty.class);
		} catch (Exception e) {
			AppLogger.warning("Failed to load difficulty: " + file.getFileName() + ".\n" + e.getMessage());
			return null;
		}
	}
	
	protected static @Nullable Difficulty loadFromResources(String name) {
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
	
	/* ---------- HELPER METHODS ---------- */
}
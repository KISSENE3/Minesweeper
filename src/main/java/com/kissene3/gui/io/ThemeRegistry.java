package com.kissene3.gui.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.kissene3.gui.models.Theme;
import com.kissene3.io.AppLogger;
import com.kissene3.main.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class ThemeRegistry {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	protected static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Color.class, (JsonDeserializer<Color>) (json, _, _) -> {
				String hex = json.getAsString();
				if (hex.startsWith("#")) {
					hex = hex.substring(1);
				} else if (hex.startsWith("0x") || hex.startsWith("0X")) {
					hex = hex.substring(2);
				}
				
				// Using Long to prevent 32-bit signed Integer overflow to -1.
				long value = Long.parseLong(hex, 16);
				
				if (hex.length() <= 6)
				{
					// RRGGBB: adds 100% Alpha (FF) to the front.
					return new Color((int) (value | 0xFF000000L), true);
				} else
				{
					// AARRGGBB: alpha already considered.
					return new Color((int) value, true);
				}
			})
			.create();
	
	/**
	 * - Map that associates theme name with file path.
	 * <br />
	 * - {@code true} is internal (from Resources) while {@code false} is external.
	 */
	protected static final Map<String, Boolean> CACHE = new HashMap<>();
	protected static final Path EXTERNAL_DIR = Main.BASE_PATH.resolve("themes");
	protected static final String INTERNAL_DIR = "/themes/";
	protected static final String[] DEFAULTS = {"light", "dark"};
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	static {
		init();
	}
	
	/**
	 * - Scans every file and populates the {@code Map}.
	 */
	protected static void init() {
		CACHE.clear();
		
		// 1. Loads the defaults from Resources.
		for (String name : DEFAULTS) {
			Theme t = loadFromResources(name);
			if (t == null) {
				AppLogger.warning("Failed to load theme from resources: " + name);
				continue;
			}
			AppLogger.info("Loaded theme from resources: " + name);
			CACHE.put(name.toLowerCase(), true);
		}
		
		// 2. Override or add from external files.
		if (!Files.isDirectory(EXTERNAL_DIR))
			return;
		
		try (Stream<Path> stream = Files.list(EXTERNAL_DIR)) {
			stream.filter(Files::isRegularFile)
				  .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
				  .forEach(file -> {
					  String fileName = file.getFileName().toString();
					  String name = fileName.substring(0, fileName.length() - 5).toLowerCase();
					  Theme custom = loadFromFile(file);
					  
					  if (isValid(custom)) {
						  AppLogger.info("Loaded theme: " + name);
						  CACHE.put(name, false);
					  } else
						  AppLogger.warning("Failed to load theme: " + name + ". Ignored.");
				  });
		} catch (IOException e) {
			AppLogger.warning("Failed to scan external themes directory: " + e.getMessage());
		}
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS ---------- */
	
	public static @NotNull Theme get(@NotNull String name) {
		String key = CACHE.containsKey(name.toLowerCase()) ? name.toLowerCase() : "light";
		
		Boolean isInternal = CACHE.getOrDefault(key, true);
		Theme t = isInternal ? loadFromResources(key) : loadFromFile(EXTERNAL_DIR.resolve(key + ".json"));
		
		return Objects.requireNonNullElseGet(t, () -> new Theme(
				new Color(192, 192, 192, 255),		// Background
				new Color(255, 255, 255, 255),		// Background Lighter Shade
				new Color(128, 128, 128, 255),		// Background Darker Shade
				new Color(128, 128, 128, 255),		// Border
				new Color(255,   0,   0, 255),		// Background Exploded Mine
				new Color(255, 255, 255,  96),		// Overlay
				new Color(170,   0,   0, 255),		// Cross
				new Color(255,   0,   0, 255),		// Flag
				new Color(  0,   0,   0, 255),		// Flag Base
				new Color(  0,   0,   0, 255),		// Mine
				new Color(255, 255, 255, 255),		// Mine Shade
				new Color(  0,   0, 255, 255),		// Number One
				new Color(  0, 128,   0, 255),		// Number Two
				new Color(255,   0,   0, 255),		// Number Three
				new Color(  0,   0, 128, 255),		// Number Four
				new Color(128,   0,   0, 255),		// Number Five
				new Color(  0, 128, 128, 255),		// Number Six
				new Color(  0,   0,   0, 255),		// Number Seven
				new Color(128, 128, 128, 255),		// Number Eight
				new Color(255,   0,   0, 255),		// Title
				new Color(  0,   0,   0, 255),		// Title Outline
				new Color(  0,   0,   0, 255),		// Text
				new Color( 64, 144, 255, 255),		// Selected Difficulty
				new Color(  0,   0,   0, 102),		// Scroll Bar
				new Color(255, 255,   0, 255),		// Smiley Base
				new Color(  0,   0,   0, 255),		// Smiley Border
				new Color( 80,  80,  80, 255),		// Settings Base
				new Color(  0,   0,   0, 255)		// Settings Border
		));
	}
	
	public static @NotNull Set<String> getAvailableNames() {
		return CACHE.keySet();
	}
	
	public static @NotNull List<String> getAvailableNamesSorted() {
		return getAvailableNames().stream().sorted().toList();
	}
	
	/* ---------- GETTERS ---------- */
	/* ---------- I/O METHODS ---------- */
	
	protected static @Nullable Theme loadFromFile(@NotNull Path file) {
		try (Reader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			return GSON.fromJson(r, Theme.class);
		} catch (Exception e) {
			AppLogger.warning("Failed to load theme: " + file.getFileName() + ".\n" + e.getMessage());
			return null;
		}
	}
	
	protected static @Nullable Theme loadFromResources(String name) {
		try (InputStream is = ThemeRegistry.class.getResourceAsStream(INTERNAL_DIR + name + ".json")) {
			if (is == null) {
				AppLogger.warning("Failed to load theme from resources: " + name + ".json");
				return null;
			}
			return GSON.fromJson(new InputStreamReader(is), Theme.class);
		} catch (Exception e) {
			AppLogger.warning("Failed to load theme from resources: " + name + ".json.\n" + e.getMessage());
			return null;
		}
	}
	
	/* ---------- I/O METHODS ---------- */
	/* ---------- UTILITIES ---------- */
	
	protected static boolean isValid(@Nullable Theme t) {
		return t != null;
	}
	
	/* ---------- UTILITIES ---------- */
}

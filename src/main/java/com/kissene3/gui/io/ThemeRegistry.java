package com.kissene3.gui.io;

import com.google.gson.*;
import com.kissene3.gui.Theme;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ThemeRegistry {
	@SuppressWarnings("unused")
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Color.class, (JsonDeserializer<Color>) (json, typeOfT, context) -> {
				String hex = json.getAsString();
				if (hex.startsWith("#")) {
					hex = hex.substring(1);
				} else if (hex.startsWith("0x") || hex.startsWith("0X")) {
					hex = hex.substring(2);
				}
				
				// Using Long to prevent 32-bit signed Integer overflow to -1.
				long value = Long.parseLong(hex, 16);
				
				if (hex.length() <= 6) //noinspection SpellCheckingInspection
				{
					// RRGGBB: adds 100% Alpha (FF) to the front.
					return new Color((int) (value | 0xFF000000L), true);
				} else //noinspection SpellCheckingInspection
				{
					// AARRGGBB: alpha already considered.
					return new Color((int) value, true);
				}
			})
			.create();
	
	/**
	 * Map that associates theme name with file path.<br />
	 * {@code true} is internal (from Resources) while {@code false} is external.
	 */
	private static final Map<String, Boolean> CACHE = new HashMap<>();
	
	// Constants
	private static final String EXTERNAL_DIR = "data/themes/";
	private static final String INTERNAL_DIR = "/themes/";
	private static final String[] DEFAULTS = {"light", "dark"};
	
	/**
	 * Scans every file and populate the {@code Map}.
	 * To be called at the start of the application.
	 */
	public static void init() {
		CACHE.clear();
		
		// 1. Loads the defaults from Resources.
		for (String name : DEFAULTS) {
			Theme t = loadFromResources(name);
			if (t == null)
				continue;
			CACHE.put(name.toLowerCase(), true);
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
			Theme custom = loadFromFile(file);
			
			// VALIDATION: Adds to the Map only if it's correct.
			if (isValid(custom)) {
				CACHE.put(name, false);
			} else {
				System.err.println("File " + file.getName() + " corrupted or partial. Ignored.");
			}
		}
	}
	
	public static @NotNull Theme get(@NotNull String name) {
		String key = CACHE.containsKey(name.toLowerCase()) ? name.toLowerCase() : "light";
		
		Boolean isInternal = CACHE.getOrDefault(key, true);
		Theme t = isInternal ? loadFromResources(key) : loadFromFile(new File(EXTERNAL_DIR + key + ".json"));
		
		return Objects.requireNonNullElseGet(t, () -> new Theme(
				new Color(192, 192, 192, 255),		// Background
				new Color(255, 255, 255, 255),		// Background lighter shade
				new Color(128, 128, 128, 255),		// Background darker shade
				new Color(128, 128, 128, 255),		// Border
				new Color(255,   0,   0, 255),		// Background exploded mine
				new Color(255, 255, 255,  96),		// Overlay
				new Color(170,   0,   0, 255),		// Cross
				new Color(255,   0,   0, 255),		// Flag
				new Color(  0,   0,   0, 255),		// Flag base
				new Color(  0,   0,   0, 255),		// Mine
				new Color(255, 255, 255, 255),		// Mine Shade
				new Color(  0,   0, 255, 255),		// Number one
				new Color(  0, 128,   0, 255),		// Number two
				new Color(255,   0,   0, 255),		// Number three
				new Color(  0,   0, 128, 255),		// Number four
				new Color(128,   0,   0, 255),		// Number five
				new Color(  0, 128, 128, 255),		// Number six
				new Color(  0,   0,   0, 255),		// Number seven
				new Color(128, 128, 128, 255),		// Number eight
				new Color(255,   0,   0, 255),		// Title
				new Color(  0,   0,   0, 255)		// Title outline
		));
	}
	
	@Contract(pure = true)
	public static @NotNull Set<String> getAvailableNames() {
		return CACHE.keySet();
	}
	
	/* \/ \/ Private Helper Methods \/ \/ */
	
	private static boolean isValid(@Nullable Theme t) {
		return t != null;
	}
	
	private static @Nullable Theme loadFromFile(@NotNull File file) {
		try (Reader r = new FileReader(file)) {
			return GSON.fromJson(r, Theme.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static @Nullable Theme loadFromResources(String name) {
		try (InputStream is = ThemeRegistry.class.getResourceAsStream(INTERNAL_DIR + name + ".json")) {
			if (is == null)
				return null;
			return GSON.fromJson(new InputStreamReader(is), Theme.class);
		} catch (Exception e) {
			return null;
		}
	}
}

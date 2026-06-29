package com.kissene3.gui.utilities;


import com.kissene3.gui.io.AppLogger;

import java.util.ArrayList;
import java.util.List;
import java.lang.ref.WeakReference;

public class TextureManager {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	protected static final List<WeakReference<Textures>> ACTIVE_PACKS = new ArrayList<>();
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- UTILITIES ---------- */
	
	public static void addPack(Textures texture) {
		ACTIVE_PACKS.removeIf(ref -> ref.get() == null);
		ACTIVE_PACKS.add(new WeakReference<>(texture));
	}
	
	public static void unregister(Textures pack) {
		ACTIVE_PACKS.removeIf(ref -> {
			Textures current = ref.get();
			return current == null || current == pack;
		});
	}
	
	public static void invalidateAll() {
		ACTIVE_PACKS.removeIf(ref -> {
			Textures pack = ref.get();
			if (pack != null) {
				pack.clear();
				return false;
			}
			return true;
		});
		AppLogger.info("All active texture caches invalidated.");
	}
	
	/* ---------- UTILITIES ---------- */
}

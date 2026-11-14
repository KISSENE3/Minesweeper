package com.kissene3.gui.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/** Represents the spatial extent of a component. */
public record MyDimension(int width, int height) {
	/* ---------- CONSTRUCTORS ---------- */
	
	/** @throws IllegalArgumentException if width or height are negative. */
	public MyDimension {
		if (width < 0 || height < 0)
			throw new IllegalArgumentException(
					String.format("Width (entered %d) and height (entered %d) cannot be negative.", width, height)
			);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS ---------- */
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyDimension withWidth(int width) {
		return new MyDimension(width, this.height);
	}
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyDimension withHeight(int height) {
		return new MyDimension(this.width, height);
	}
	
	/* ---------- GETTERS ---------- */
}

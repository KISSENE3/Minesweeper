package com.kissene3.gui.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a point in a 2D plane.
 * @param x horizontal coordinate
 * @param y vertical coordinate
 */
public record MyCoordinates(int x, int y) {
	/* ---------- GETTERS ---------- */
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyCoordinates withX(int x) {
		return new MyCoordinates(x, this.y);
	}
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyCoordinates withY(int y) {
		return new MyCoordinates(this.x, y);
	}
	
	/* ---------- GETTERS ---------- */
}

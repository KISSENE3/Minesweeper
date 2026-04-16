package com.kissene3.game;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

public record Difficulties(int width, int height, int numMines) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	public static final Difficulties EASY = new Difficulties(9, 9, 10);
	public static final Difficulties MEDIUM = new Difficulties(16, 16, 40);
	public static final Difficulties HARD = new Difficulties(30, 16, 99);
	
	@Contract(pure = true)
	public static @NotNull Difficulties fromName(@NotNull String name, @Nullable Difficulties customFallback) {
		return switch (name.toLowerCase()) {
			case "easy" -> EASY;
			case "medium" -> MEDIUM;
			case "hard" -> HARD;
			default -> customFallback == null ? EASY : customFallback;
		};
	}
}

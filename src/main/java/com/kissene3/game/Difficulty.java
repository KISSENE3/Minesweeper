package com.kissene3.game;

import com.google.gson.annotations.*;
import org.jetbrains.annotations.*;

public record Difficulty(
		@SerializedName("width") @Range(from = 3, to = Integer.MAX_VALUE) int width,
		@SerializedName("height") @Range(from = 3, to = Integer.MAX_VALUE) int height,
		@SerializedName("mines") @Range(from = 1, to = Integer.MAX_VALUE) int numMines
) {
	@SuppressWarnings("ConstantValue")
	public Difficulty {
		if (width < 3 || height < 3) {
			throw new IllegalArgumentException("The grid is too small, it must be greater than a 3*3.");
		}
		if (numMines < 1) {
			throw new IllegalArgumentException("There must be at least one mine.");
		}
		if (numMines >= (width * height) - 9) {
			throw new IllegalArgumentException("Too many mines for this grid.");
		}
	}
}

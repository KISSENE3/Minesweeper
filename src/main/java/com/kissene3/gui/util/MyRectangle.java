package com.kissene3.gui.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/** Represents a rectangle in a 2D plane. */
public record MyRectangle(@NotNull MyCoordinates topLeft, @NotNull MyDimension dimensions) {
	/* ---------- CONSTRUCTORS ---------- */
	
	public MyRectangle(int x, int y, int width, int height) {
		this(new MyCoordinates(x, y), new MyDimension(width, height));
	}
	
	public MyRectangle(@NotNull MyCoordinates topLeft, int width, int height) {
		this(topLeft, new MyDimension(width, height));
	}
	
	public MyRectangle(int x, int y, @NotNull MyDimension dimensions) {
		this(new MyCoordinates(x, y), dimensions);
	}
	
	/** @throws NullPointerException if topLeft or dimensions are null. */
	public MyRectangle {
		requireNonNull(topLeft, "Top-left corner cannot be null");
		requireNonNull(dimensions, "Dimensions cannot be null");
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- GETTERS ---------- */
	
	@Contract(value = " -> new", pure = true)
	public @NotNull MyCoordinates bottomRight() {
		return new MyCoordinates(this.topLeft.x() + this.dimensions.width(), this.topLeft.y() + this.dimensions.height());
	}
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyRectangle withTopLeft(@NotNull MyCoordinates topLeft) {
		return new MyRectangle(topLeft, this.dimensions);
	}
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyRectangle withDimensions(@NotNull MyDimension dimensions) {
		return new MyRectangle(this.topLeft, dimensions);
	}
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyRectangle withX(int x) {
		return new MyRectangle(this.topLeft.withX(x), this.dimensions);
	}
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyRectangle withY(int y) {
		return new MyRectangle(this.topLeft.withY(y), this.dimensions);
	}
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyRectangle withWidth(int width) {
		return new MyRectangle(this.topLeft, this.dimensions.withWidth(width));
	}
	
	@Contract(value = "_ -> new", pure = true)
	public @NotNull MyRectangle withHeight(int height) {
		return new MyRectangle(this.topLeft, this.dimensions.withHeight(height));
	}
	
	@Contract(pure = true)
	public int x() {
		return this.topLeft.x();
	}
	
	@Contract(pure = true)
	public int y() {
		return this.topLeft.y();
	}
	
	@Contract(pure = true)
	public int width() {
		return this.dimensions.width();
	}
	
	@Contract(pure = true)
	public int height() {
		return this.dimensions.height();
	}
	
	/* ---------- GETTERS ---------- */
	/* ---------- UTILITIES ---------- */
	
	/**
	 * Checks if the specified (x, y) coordinates are contained within this rectangle.
	 * @param x the horizontal coordinate to check.
	 * @param y the vertical coordinate to check.
	 * @return {@code true} if the coordinates are within the rectangle's bounds, {@code false} otherwise.
	 * @see #contains(MyCoordinates) contains(MyCoordinates)
	 */
	@Contract(pure = true)
	public boolean contains(int x, int y) {
		return this.contains(new MyCoordinates(x, y));
	}
	
	/**
	 * Checks if the specified point is contained within this rectangle.<br />
	 * @param point the {@link MyCoordinates MyCoordinates} to check.
	 * @return {@code true} if the point is within the rectangle's bounds, {@code false} otherwise.
	 * @throws NullPointerException if {@code point} is {@code null}.
	 */
	@Contract(pure = true)
	public boolean contains(@NotNull MyCoordinates point) {
		MyCoordinates bottomRight = this.bottomRight();
		return point.x() >= this.topLeft.x()
				&& point.x() < bottomRight.x()
				&& point.y() >= this.topLeft.y()
				&& point.y() < bottomRight.y();
	}
	
	/**
	 * Checks if the specified rectangle is entirely contained within this rectangle.
	 * @param other the {@link MyRectangle MyRectangle} to check.
	 * @return {@code true} if the other rectangle is completely inside this one, {@code false} otherwise.
	 * @throws NullPointerException if {@code other} is {@code null}.
	 */
	@Contract(pure = true)
	public boolean contains(@NotNull MyRectangle other) {
		requireNonNull(other);
		return other.x() >= this.x() &&
				other.y() >= this.y() &&
				(other.x() + other.width()) <= (this.x() + this.width()) &&
				(other.y() + other.height()) <= (this.y() + this.height());
	}
	
	/* ---------- UTILITIES ---------- */
}

package com.kissene3.gui.utilities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/** A container class containing records and methods for handling the code. */
@SuppressWarnings("unused")
public final class MyUtilities {
	
	/**
	 * Represents a point in a 2D plane.
	 * @param x horizontal coordinate
	 * @param y vertical coordinate
	 */
	public record Coordinates(int x, int y) {
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Coordinates withX(int x) {
			return new Coordinates(x, this.y);
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Coordinates withY(int y) {
			return new Coordinates(this.x, y);
		}
	}
	
	/** Represents the spatial extent of a component. */
	public record Dimension(int width, int height) {
		
		/** @throws IllegalArgumentException if width or height are negative. */
		public Dimension {
			if (width < 0 || height < 0)
				throw new IllegalArgumentException(
						String.format("Width (entered %d) and height (entered %d) cannot be negative.", width, height)
				);
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Dimension withWidth(int width) {
			return new Dimension(width, this.height);
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Dimension withHeight(int height) {
			return new Dimension(this.width, height);
		}
	}
	
	/** Represents a rectangle in a 2D plane. */
	public record Rectangle(@NotNull Coordinates topLeft, @NotNull Dimension dimensions) {
		
		/** @throws NullPointerException if topLeft or dimensions are null. */
		public Rectangle {
			requireNonNull(topLeft, "Top-left corner cannot be null");
			requireNonNull(dimensions, "Dimensions cannot be null");
		}
		
		public Rectangle(int x, int y, int width, int height) {
			this(new Coordinates(x, y), new Dimension(width, height));
		}
		
		/**
		 * Checks if the specified point is contained within this rectangle.
		 * <p>
		 * The check is inclusive of the top-left corner and exclusive of the
		 * bottom-right corner.
		 *</p>
		 * @param point the {@link Coordinates Coordinates} to check.
		 * @return {@code true} if the point is within the rectangle's bounds, {@code false} otherwise.
		 * @throws NullPointerException if {@code point} is {@code null}.
		 */
		@Contract(pure = true)
		public boolean contains(@NotNull Coordinates point) throws NullPointerException {
			Coordinates bottomRight = this.bottomRight();
			return point.x() >= this.topLeft.x()
					&& point.x() < bottomRight.x()
					&& point.y() >= this.topLeft.y()
					&& point.y() < bottomRight.y();
		}
		
		/**
		 * Checks if the specified (x, y) coordinates are contained within this rectangle.
		 *
		 * @param x the horizontal coordinate to check.
		 * @param y the vertical coordinate to check.
		 * @return {@code true} if the coordinates are within the rectangle's bounds, {@code false} otherwise.
		 * @see #contains(Coordinates) contains(Coordinates)
		 */
		@Contract(pure = true)
		public boolean contains(int x, int y) {
			return this.contains(new Coordinates(x, y));
		}
		
		/**
		 * Checks if the specified rectangle is entirely contained within this rectangle.
		 *
		 * @param other the {@link Rectangle Rectangle} to check.
		 * @return {@code true} if the other rectangle is completely inside this one, {@code false} otherwise.
		 * @throws NullPointerException if {@code other} is {@code null}.
		 */
		@Contract(pure = true)
		public boolean contains(@NotNull Rectangle other) throws NullPointerException {
			requireNonNull(other, "The rectangle to check cannot be null");
			return this.contains(other.topLeft()) && this.contains(other.bottomRight().x() - 1, other.bottomRight().y() - 1);
		}
		
		@Contract(value = " -> new", pure = true)
		public @NotNull Coordinates bottomRight() {
			return new Coordinates(this.topLeft.x() + this.dimensions.width(), this.topLeft.y() + this.dimensions.height());
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Rectangle withTopLeft(@NotNull Coordinates topLeft) {
			return new Rectangle(topLeft, this.dimensions);
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Rectangle withDimensions(@NotNull Dimension dimensions) {
			return new Rectangle(this.topLeft, dimensions);
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Rectangle withX(int x) {
			return new Rectangle(this.topLeft.withX(x), this.dimensions);
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Rectangle withY(int y) {
			return new Rectangle(this.topLeft.withY(y), this.dimensions);
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Rectangle withWidth(int width) {
			return new Rectangle(this.topLeft, this.dimensions.withWidth(width));
		}
		
		@Contract(value = "_ -> new", pure = true)
		public @NotNull Rectangle withHeight(int height) {
			return new Rectangle(this.topLeft, this.dimensions.withHeight(height));
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
	}
	
	
	private MyUtilities() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}

package com.kissene3.gui.components;

import com.kissene3.gui.MyUtilities.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * <p>The basic component that all other GUI components extend.</p>
 * <p>It provides basic functionalities like position, size and activity.</p>
 * <p>It also includes a utility method to check if a point is within its bounds.</p>
 */
@SuppressWarnings("unused")
public abstract class MyComponent {
	
	/* ---------- ATTRIBUTES ---------- */
	
	private Rectangle bounds;
	
	/** The flag that indicates if the component is active and can be interacted with. */
	protected boolean isActive = true;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public MyComponent(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height));
	}
	
	public MyComponent(@NotNull Coordinates topLeft, @NotNull Dimension dimensions) {
		this(new Rectangle(topLeft, dimensions));
	}
	
	public MyComponent(@NotNull Rectangle bounds) {
		this.setBounds(bounds);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- SPATIAL GETTERS/SETTERS ---------- */
	
	@Contract(pure = true)
	public final @NotNull Rectangle getBounds() {
		return this.bounds;
	}
	
	/** The master setter for the component's bounds. All other spatial setters delegate to this method. */
	public final void setBounds(@NotNull Rectangle bounds) {
		this.bounds = requireNonNull(bounds);
		this.onBoundsChanged();
	}
	
	public final void setBounds(@NotNull Coordinates topLeft, @NotNull Dimension dimensions) {
		this.setBounds(new Rectangle(topLeft, dimensions));
	}
	
	public final void setBounds(int x, int y, int width, int height) {
		this.setBounds(new Rectangle(x, y, width, height));
	}
	
	@Contract(pure = true)
	public final int getX() {
		return this.bounds.x();
	}
	public final void setX(int x) {
		this.setBounds(this.bounds.withX(x));
	}
	
	@Contract(pure = true)
	public final int getY() {
		return this.bounds.y();
	}
	public final void setY(int y) {
		this.setBounds(this.bounds.withY(y));
	}
	
	@Contract(pure = true)
	public final int getWidth() {
		return this.bounds.width();
	}
	public final void setWidth(int width) {
		this.setBounds(this.bounds.withWidth(width));
	}
	
	@Contract(pure = true)
	public final int getHeight() {
		return this.bounds.height();
	}
	public final void setHeight(int height) {
		this.setBounds(this.bounds.withHeight(height));
	}
	
	@Contract(pure = true)
	public final @NotNull Coordinates getTopLeft() {
		return this.bounds.topLeft();
	}
	public final void setTopLeft(@NotNull Coordinates topLeft) {
		this.setBounds(this.bounds.withTopLeft(topLeft));
	}
	public final void setTopLeft(int x, int y) {
		this.setTopLeft(new Coordinates(x, y));
	}
	
	@Contract(pure = true)
	public final @NotNull Coordinates getBottomRight() {
		return this.bounds.bottomRight();
	}
	
	@Contract(pure = true)
	public final @NotNull Dimension getDimensions() {
		return this.bounds.dimensions();
	}
	public final void setDimensions(@NotNull Dimension dimensions) {
		this.setBounds(this.bounds.withDimensions(dimensions));
	}
	public final void setDimensions(int width, int height) {
		this.setDimensions(new Dimension(width, height));
	}
	
	/* ---------- SPATIAL/SETTERS ---------- */
	/* ---------- FLAGS GETTERS/SETTERS ---------- */
	
	@Contract(pure = true)
	public boolean isActive() {
		return this.isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	/* ---------- FLAGS GETTERS/SETTERS ---------- */
	/* ---------- UTILITY ---------- */
	
	/**
	 * Checks if the specified coordinates ({@code mx} and {@code my}) are contained within the component's bounds.
	 * @param mx The x-coordinate to check.
	 * @param my The y-coordinate to check.
	 * @return {@code true} if the coordinates are within the component's bounds, {@code false} otherwise.
	 */
	@Contract(pure = true)
	public boolean contains(int mx, int my) {
		return this.bounds.contains(mx, my);
	}
	
	/**
	 * Checks if the specified {@code coordinates} are contained within the component's bounds.
	 * @param coordinates The {@link Coordinates} to check.
	 * @return {@code true} if the coordinates are within the component's bounds, {@code false} otherwise.
	 */
	@Contract(pure = true)
	public boolean contains(@NotNull Coordinates coordinates) {
		return this.bounds.contains(coordinates);
	}
	
	/**
	 * Checks if the specified {@code rectangle} is contained within the component's bounds.
	 * @param rectangle The {@link Rectangle} to check.
	 * @return {@code true} if the rectangle is within the component's bounds, {@code false} otherwise.
	 */
	@Contract(pure = true)
	public boolean contains(@NotNull Rectangle rectangle) {
		return this.bounds.contains(rectangle);
	}
	
	/**
	 * Checks if the specified component ({@code other}) is contained within the component's bounds.
	 * @param other The {@link MyComponent} to check.
	 * @return {@code true} if the other component is within this component's bounds, {@code false} otherwise.
	 */
	@Contract(pure = true)
	public boolean contains(@NotNull MyComponent other) {
		return this.bounds.contains(other.getBounds());
	}
	
	protected void onBoundsChanged() {}
	
	/* ---------- UTILITY ---------- */
}
